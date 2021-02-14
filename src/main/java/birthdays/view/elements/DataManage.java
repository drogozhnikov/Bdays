package birthdays.view.elements;

import birthdays.controller.ServiceController;
import birthdays.model.BDayUnit;
import birthdays.model.Status;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    private TextField inputBirthday = new TextField();
    private TextField inputPhone = new TextField();

    private TextArea inputDescription = new TextArea("Description");

    private Button actionButton = new Button("action");
    private Button cancelButton = new Button("Cancel");
    private Button clearButton = new Button("Clear");

    private CheckBox stayOnManage = new CheckBox("Stay On Manage");
    private CheckBox clearWhenAction = new CheckBox("Clear When Action");

    private BDayUnit selectedUnit;

    private int nameSize = 90;
    private int ownerSize = 50;
    private int linkSize = 500;
    private int mailSize = 100;
    private int accountSize = 100;
    private int passwordSize = 50;
    private int descriptionSize = 500;

    public DataManage() {
        serviceController = ServiceController.getInstance();
        initActionButton();
        initCancelButton();
        initSizes();
        initPosition();
        initCancelButton();
        initFieldsValidation();
        initClearButoon();
    }

    private void initPosition() {
        HBox name = new HBox(firstNameLabel, inputFirstName);
        HBox owner = new HBox(LastNameLabel, inputLastName);
        HBox link = new HBox(birthdayLabel, inputBirthday);
        HBox mail = new HBox(phoneLabel, inputPhone);

        VBox left = new VBox(name, stayOnManage);
        VBox center = new VBox(mail, link, clearWhenAction);
        VBox right = new VBox(owner);
        HBox top = new HBox(actionButton, clearButton, cancelButton);

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

        inputFirstName.setMaxWidth(Double.MAX_VALUE);
        inputLastName.setMaxWidth(Double.MAX_VALUE);
        inputBirthday.setMaxWidth(Double.MAX_VALUE);
        inputPhone.setMaxWidth(Double.MAX_VALUE);

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
        validateField(inputFirstName, firstNameLabel.getText(), nameSize);
        validateField(inputLastName, LastNameLabel.getText(), ownerSize);
        validateField(inputBirthday, birthdayLabel.getText(), linkSize);
        validateField(inputPhone, phoneLabel.getText(), mailSize);

        inputDescription.setOnKeyReleased(event -> {
            if (inputDescription.getText().length() > descriptionSize) {
                ServiceController.getInstance().alert(Status.INFO, "Description: field must contain no more than " + descriptionSize + " characters");
            }
        });

    }

    private BDayUnit prepareUnit() {
        BDayUnit newUnit = new BDayUnit();
        DateTimeFormatter formatter = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        DateTime dt = formatter.parseDateTime(inputBirthday.getText());

        newUnit.setFirstName(inputFirstName.getText());
        newUnit.setLastName(inputLastName.getText());
        newUnit.setDate(dt);
        newUnit.setPhoneNumber(inputPhone.getText());
        newUnit.setDescription(inputDescription.getText());

        return newUnit;

    }

    private boolean validate(BDayUnit newUnit) {

        String fieldsMessage = "";
        if (inputFirstName.getText().length() > nameSize) {
            fieldsMessage += fieldsMessage + firstNameLabel.getText() + " field must contain no more than " + nameSize + " characters" + "\n";
        }
        if (inputLastName.getText().length() > ownerSize) {
            fieldsMessage += fieldsMessage + LastNameLabel.getText() + " field must contain no more than " + ownerSize + " characters" + "\n";
        }
        if (inputBirthday.getText().length() > linkSize) {
            fieldsMessage += fieldsMessage + birthdayLabel.getText() + " field must contain no more than " + linkSize + " characters" + "\n";
        }
        if (inputPhone.getText().length() > mailSize) {
            fieldsMessage += fieldsMessage + phoneLabel.getText() + " field must contain no more than " + mailSize + " characters" + "\n";
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
            message += "-name";
        }
        if (newUnit.getLastName() == null || newUnit.getLastName().equals("")) {
            message += "-owner";
        }
        if (newUnit.getDate() == null || newUnit.getDate().equals("")) {
            message += "-link";
        }
        if (newUnit.getPhoneNumber() == null || newUnit.getPhoneNumber().equals("")) {
            message += "-mail";
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
                    if (unit.getDate().equals(newUnit.getDate())) {
                        passwordAlert(newUnit, units);
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
        inputFirstName.setText(unit.getFirstName());
        inputLastName.setText(unit.getLastName());
        inputBirthday.setText(new DateTime(unit.getDate()).toString());
        inputPhone.setText(unit.getPhoneNumber());
        inputDescription.setText(unit.getDescription());
    }

    public void passwordAlert(BDayUnit newUnits, ArrayList<BDayUnit> existingUnits) {
        StringBuilder existingAccountsList = new StringBuilder();
        for (BDayUnit unit : existingUnits) {
            existingAccountsList.append(unit.toString()).append("\n");
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Password already exist");
        alert.setHeaderText("Are you sure you want to add a new account with a password that already exists in the database?");
        alert.setContentText("new Account: " + newUnits.toString() + "\n" + " \n" + existingAccountsList);

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
        inputBirthday.clear();
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