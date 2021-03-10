package birthdays.view;

import birthdays.controller.ServiceController;
import birthdays.model.BDayUnit;
import birthdays.model.Status;
import birthdays.view.elements.DataManage;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.util.Optional;

public class BirthdaysRootView extends BorderPane {

    private ServiceController serviceController = ServiceController.getInstance("");

    //Context
    private ContextMenu contextMenu = new ContextMenu();
    private MenuItem contextMenuItemDelete = new MenuItem(" Delete ");
    private MenuItem contextMenuItemUpdate = new MenuItem(" Update ");
    private MenuItem descriptionMenuItem = new MenuItem(" ShowDescription ");

    //Menu
    private HBox controlMenu = new HBox();
    private DataManage dataManage = new DataManage();

    private Button addButton = new Button("Add");
    private Button loadButton = new Button("Load");
    private Button saveButton = new Button("Save");

    private TextField searchField = new TextField();

    private FileChooser xmlFile = new FileChooser();

    //Table
    public static TableView<BDayUnit> birthdaysTable = new TableView<>();

    //Info
    private Text info = new Text("Ready");

    public BirthdaysRootView() {
        info.setText(serviceController.getNearestBirthdayMan());
        initTable();
        initAddButton();
        initContextMenu();
        initSaveButton();
        initLoadButton();
        initSearchField();
        initMenuButtons();
        initSizes();
        setStyles();

        initkeyCombinations();

        super.setTop(controlMenu);
        super.setBottom(info);
        super.setCenter(birthdaysTable);
    }

    private void initTable() {
        birthdaysTable = serviceController.getBirthdaysTable();
    }

    private void initMenuButtons() {
        controlMenu.getChildren().addAll(addButton, saveButton, searchField, loadButton);
    }

    private void initSearchField() {
        searchField.setOnKeyReleased(event -> serviceController.search(event.getCode(), searchField.getText()));
    }

    private void initAddButton() {
        addButton.setOnAction(event -> {
            dataManage.show();
            super.setTop(dataManage);
        });
    }

    private void initSaveButton() {
        saveButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                serviceController.save();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Saving");
                alert.setHeaderText("Save successful");
                alert.showAndWait();
            }
        });
    }

    private void initLoadButton() {
        loadButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete fields on DB and load from XMl");
                alert.setHeaderText("Are you sure want to clear accounts database and load data from xml-backup file?");
                Optional<ButtonType> option = alert.showAndWait();

                if (option.get() == ButtonType.OK) {
                    serviceController.load();
                    setInfo(Status.READY);
                } else if (option.get() == ButtonType.CANCEL) {
                    setInfo(Status.CANCELED);
                } else {
                    setInfo(Status.READY);
                }
            }
        });
    }

    private void initkeyCombinations() {
//saveToPath
        super.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.S,
                    KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    xmlFile.setTitle("Save XML Panda-BackUp file");
                    xmlFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
                    xmlFile.setInitialFileName("XMLBackUp.xml");
                    String filePath = null;
                    try {
                        filePath = xmlFile.showSaveDialog(null).getAbsolutePath();
                    } catch (NullPointerException e) {
                        setInfo(Status.CANCELED);
                    }
                    if (filePath != null && !filePath.equals("")) {
                        serviceController.saveToDirectory(filePath);
                    }
                }
            }
        });
//loadFromPath

        super.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(KeyCode.L,
                    KeyCombination.CONTROL_DOWN);

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    xmlFile.setTitle("Load XML Panda-BackUp file");
                    xmlFile.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML", "*.xml"));
                    String filePath = null;
                    try {
                        filePath = xmlFile.showOpenDialog(null).getAbsolutePath();
                    } catch (NullPointerException e) {
                        setInfo(Status.CANCELED);
                    }
                    if (filePath != null && !filePath.equals("")) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Delete fields on DB and load from XMl");
                        alert.setHeaderText("Are you sure want to clear accounts database and load data from xml-backup file?");
                        Optional<ButtonType> option = alert.showAndWait();

                        if (ButtonType.OK == option.get()) {
                            serviceController.loadFromDirectory(filePath);
                            setInfo(Status.READY);
                        } else if (option.get() == ButtonType.CANCEL) {
                            setInfo(Status.CANCELED);
                        } else {
                            setInfo(Status.READY);
                        }
                    }
                }
            }
        });
        birthdaysTable.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                BDayUnit unit = birthdaysTable.getSelectionModel().getSelectedItem();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Delete Account");
                alert.setHeaderText("Are you sure want to delete this unit data?");
                alert.setContentText(unit.toString());

                Optional<ButtonType> option = alert.showAndWait();

                if (ButtonType.OK == option.get()) {
                    serviceController.delete(unit);
                    serviceController.refresh();
                } else if (option.get() == ButtonType.CANCEL) {
                    setInfo(Status.CANCELED);
                } else {
                    setInfo(Status.READY);
                }
            }
        });
    }

    private void initContextMenu() {
        contextMenuItemDelete.setOnAction(event -> {
            BDayUnit unit = birthdaysTable.getSelectionModel().getSelectedItem();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Account");
            alert.setHeaderText("Are you sure want to delete this unit data?");
            alert.setContentText(unit.toString());

            Optional<ButtonType> option = alert.showAndWait();

            if (ButtonType.OK == option.get()) {
                serviceController.delete(unit);
                serviceController.refresh();
            } else if (option.get() == ButtonType.CANCEL) {
                setInfo(Status.CANCELED);
            } else {
                setInfo(Status.READY);
            }
            serviceController.setInfo(Status.INFO, serviceController.getNearestBirthdayMan());
        });

        contextMenuItemUpdate.setOnAction(event -> {
            BDayUnit unit = birthdaysTable.getSelectionModel().getSelectedItem();
            dataManage.show(unit);
            super.setTop(dataManage);
        });
        descriptionMenuItem.setOnAction(event -> {
            BDayUnit unit = birthdaysTable.getSelectionModel().getSelectedItem();
            TextArea descrArea = new TextArea(unit.getDescription());
            descrArea.setEditable(false);
            descrArea.setPrefHeight(20);
            VBox temp = new VBox();
            temp.getChildren().addAll(controlMenu, descrArea);
            super.setTop(temp);
        });

        contextMenu.getItems().addAll(contextMenuItemUpdate, descriptionMenuItem, contextMenuItemDelete);
        birthdaysTable.setOnContextMenuRequested(event -> contextMenu.show(birthdaysTable, event.getScreenX(), event.getScreenY()));
    }

    private void initSizes() {
        addButton.setMaxWidth(Double.MAX_VALUE);
        saveButton.setMaxWidth(Double.MAX_VALUE);
        loadButton.setMaxWidth(Double.MAX_VALUE);
        searchField.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(addButton, Priority.ALWAYS);
        HBox.setHgrow(saveButton, Priority.ALWAYS);
        HBox.setHgrow(loadButton, Priority.ALWAYS);
        HBox.setHgrow(searchField, Priority.ALWAYS);
    }

    public void setInfo(Status status, String message) {
        info.setText(status.getMessage() + message);
        info.setFill(status.getColor());
    }

    public void setInfo(Status status) {
        info.setText(status.getMessage());
        info.setFill(status.getColor());
    }

    public void alert(Status status, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(status.getMessage());
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public void showRootMenu() {
        super.setTop(controlMenu);
    }

    private void setStyles() {
        setAlignment(info, Pos.BOTTOM_CENTER);
        setMargin(dataManage, new Insets(2));
    }
}
