package birthdays.view.elements;

import birthdays.controller.ServiceController;
import birthdays.model.BDayUnit;
import birthdays.model.Status;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

public class DataManage extends BorderPane {

    ServiceController serviceController;

    private int labelsSize = 80;
    private int borderPrefSize = 94;
    private int descriptionFieldSize = 50;

    private Label firstNameLabel = new Label("FirstName: ");
    private Label LastNameLabel = new Label("LastName: ");
    private Label birthdayLabel = new Label("Birthday: ");
    private Label phoneLabel = new Label("Phone: ");

    private TextField inputFirstName = new TextField();
    private TextField inputLastName = new TextField();
    private DatePicker inputBirthday = new DatePicker();
    private TextField inputPhone = new TextField();

    private TextArea inputDescription = new TextArea("Description");

    private Button actionButton = new Button("action");
    private Button cancelButton = new Button("Cancel");
    private Button clearButton = new Button("Clear");

    private CheckBox stayOnManage = new CheckBox("Stay On Manage");
    private CheckBox clearWhenAction = new CheckBox("Clear When Action");

    private BDayUnit selectedUnit;

    private int firstNameSize = 30;
    private int lastNameSize = 30;
    private int phoneSize = 40;
    private int descriptionSize = 500;
    private int spacing = 1;
    private int fieldSize = 300;

    public DataManage() {
        serviceController = ServiceController.getInstance();
        initDatePicker();
        initActionButton();
        initCancelButton();
        initSizes();
        initPosition();
        initCancelButton();
        initFieldsValidation();
        initClearButoon();
    }

    private void initPosition() {

        HBox firstName = new HBox(spacing, firstNameLabel, inputFirstName);
        HBox lastName = new HBox(spacing, LastNameLabel, inputLastName);
        HBox birthday = new HBox(spacing, birthdayLabel, inputBirthday);
        HBox phone = new HBox(spacing, phoneLabel, inputPhone);


        VBox left = new VBox(spacing, phone, birthday);
        VBox center = new VBox(spacing, firstName, lastName);
        VBox right = new VBox(spacing, stayOnManage, clearWhenAction);
        HBox top = new HBox(spacing, actionButton, clearButton, cancelButton);

        setMargin(center, new Insets(2));
        setMargin(right, new Insets(2));
        setMargin(left, new Insets(2));

        super.setLeft(left);
        super.setCenter(center);
        super.setRight(right);
        super.setTop(top);
        super.setBottom(inputDescription);

    }

    private void initSizes() {
        super.setPrefHeight(borderPrefSize);

        firstNameLabel.setMinWidth(labelsSize);
        LastNameLabel.setMinWidth(labelsSize);
        birthdayLabel.setMinWidth(labelsSize);
        phoneLabel.setMinWidth(labelsSize);

        firstNameLabel.setAlignment(Pos.CENTER);
        LastNameLabel.setAlignment(Pos.CENTER);
        birthdayLabel.setAlignment(Pos.CENTER);
        phoneLabel.setAlignment(Pos.CENTER);

        inputFirstName.setMaxWidth(fieldSize);
        inputLastName.setMaxWidth(fieldSize);
        inputBirthday.setMaxWidth(fieldSize);
        inputPhone.setMaxWidth(fieldSize);

        HBox.setHgrow(inputFirstName, Priority.ALWAYS);
        HBox.setHgrow(inputLastName, Priority.ALWAYS);
        HBox.setHgrow(inputBirthday, Priority.ALWAYS);
        HBox.setHgrow(inputPhone, Priority.ALWAYS);

        actionButton.setMaxWidth(Double.MAX_VALUE);
        cancelButton.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(actionButton, Priority.ALWAYS);
        HBox.setHgrow(cancelButton, Priority.ALWAYS);

        stayOnManage.setMaxWidth(Double.MAX_VALUE);

        inputDescription.setPrefHeight(descriptionFieldSize);
    }

    private void initDatePicker(){
        inputBirthday.setValue(LocalDate.of(2005, 6, 15));
        inputBirthday.setShowWeekNumbers(true);
    }


    private void initActionButton() {
        actionButton.setText("Add");
        actionButton.setOnAction(event -> {
            BDayUnit newUnit = prepareUnit();
            if (validate(newUnit)) {
                serviceController.create(newUnit);
            } else {
                stayOnManage.setSelected(true);
            }
            if (!stayOnManage.isSelected()) {
                clearWhenAction.setSelected(true);
            }
            hide();
            if (clearWhenAction.isSelected()) {
                clear();
            }
        });
    }

    private void initActionButton(BDayUnit unit) {
        actionButton.setText("Save");
        initUnitData(unit);
        actionButton.setOnAction(event -> {
            BDayUnit newUnit = prepareUnit();
            if (validate(newUnit)) {
                newUnit.setId(selectedUnit.getId());
                serviceController.update(newUnit);
                serviceController.refresh();
            }
            hide();
            if (clearWhenAction.isSelected()) {
                clear();
            }
        });
    }

    private void initCancelButton() {
        cancelButton.setOnAction(event -> {
            serviceController.showRootMenu();
            clear();
        });
    }

    private void initClearButoon() {
        clearButton.setOnAction(event -> clear());
    }

    private void initFieldsValidation() {
        validateField(inputFirstName, firstNameLabel.getText(), firstNameSize);
        validateField(inputLastName, LastNameLabel.getText(), lastNameSize);
        validateField(inputPhone, phoneLabel.getText(), phoneSize);

        inputDescription.setOnKeyReleased(event -> {
            if (inputDescription.getText().length() > descriptionSize) {
                ServiceController.getInstance().alert(Status.INFO, "Description: field must contain no more than " + descriptionSize + " characters");
            }
        });

    }

    private BDayUnit prepareUnit() {
        BDayUnit newUnit = new BDayUnit();

        newUnit.setFirstName(inputFirstName.getText());
        newUnit.setLastName(inputLastName.getText());
        newUnit.setDate(inputBirthday.getEditor().getText());
        newUnit.setPhoneNumber(inputPhone.getText());
        newUnit.setDescription(inputDescription.getText());

        newUnit.setFullName();

        return newUnit;
    }

    private boolean validate(BDayUnit newUnit){

        String fieldsMessage = "";
        if (inputFirstName.getText().length() > firstNameSize) {
            fieldsMessage += fieldsMessage + firstNameLabel.getText() + " field must contain no more than " + firstNameSize + " characters" + "\n";
        }
        if (inputLastName.getText().length() > lastNameSize) {
            fieldsMessage += fieldsMessage + LastNameLabel.getText() + " field must contain no more than " + lastNameSize + " characters" + "\n";
        }
        if (inputPhone.getText().length() > phoneSize) {
            fieldsMessage += fieldsMessage + phoneLabel.getText() + " field must contain no more than " + phoneSize + " characters" + "\n";
        }
        if (inputDescription.getText().length() > descriptionSize) {
            fieldsMessage += fieldsMessage + "Description: field must contain no more than " + descriptionSize + " characters" + "\n";
        }
        if (!fieldsMessage.equals("")) {
            serviceController.alert(Status.INFO, fieldsMessage);
            return false;
        }

        String message = "Found problems with: ";
        if (newUnit.getFirstName() == null || newUnit.getFirstName().equals("")) {
            message += "-Full Name";
        }
        if (newUnit.getLastName() == null || newUnit.getLastName().equals("")) {
            message += "-LastName";
        }
        if (!message.equals("Found problems with: ")) {
            serviceController.alert(Status.INFO, message);
            return false;
        }

        //check new password to existing id db and ask user what he must to do
        if (!actionButton.getText().equals("Save")) {
            ArrayList<BDayUnit> units = serviceController.selectAll();
            if (units != null && units.size() > 0) {
                for (BDayUnit unit : units) {
                    if (unit.getDate().equals(newUnit.getDate()) &&
                            unit.getFirstName().equals(newUnit.getFirstName())&&
                            unit.getLastName().equals(newUnit.getLastName())
                    ) {
                        exisitingUnitAlert(newUnit);
                        break;
                    }
                }
            }
        }
        return true;
    }

    private void validateField(TextField input, String fieldName, int size) {
        input.setOnKeyReleased(event -> {
            if (input.getText().length() > size) {
                ServiceController.getInstance().alert(Status.INFO, fieldName + " Field must contain no more than " + size + " characters");
            }
        });
    }

    private void initUnitData(BDayUnit unit) {
            DateTime dateTime = unit.getDateTime();
        inputFirstName.setText(unit.getFirstName());
        inputLastName.setText(unit.getLastName());
        inputBirthday.setValue(LocalDate.of(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth()));
        inputPhone.setText(unit.getPhoneNumber());
        inputDescription.setText(unit.getDescription());
    }

    public void exisitingUnitAlert(BDayUnit newUnits) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Unit already exist");
        alert.setHeaderText("Are you sure you want to add a new unit with a parametrs that already exists in the database?");
        alert.setContentText("new Bboy: " + newUnits.getFirstName() + " " +
                newUnits.getLastName() + " " +
                newUnits.getDate());

        Optional<ButtonType> option = alert.showAndWait();

        if (option.get() == ButtonType.OK) {
            serviceController.create(newUnits);
            serviceController.refresh();
            serviceController.setInfo(Status.READY);
        } else if (option.get() == ButtonType.CANCEL) {
            serviceController.setInfo(Status.CANCELED);
        } else {
            serviceController.setInfo(Status.READY);
        }
    }

    private void clear() {
        inputFirstName.clear();
        inputFirstName.clear();
        inputLastName.clear();
            initDatePicker();
        inputPhone.clear();
        inputDescription.clear();
    }

    public void hide() {
        if (!stayOnManage.isSelected()) {
            serviceController.showRootMenu();
        }
    }

    public void show() {
        initActionButton();
    }

    public void show(BDayUnit unit) {
        selectedUnit = unit;
        initActionButton(unit);
    }

}