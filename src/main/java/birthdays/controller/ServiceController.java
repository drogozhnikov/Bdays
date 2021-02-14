package birthdays.controller;

import birthdays.model.BDayUnit;
import birthdays.model.Options;
import birthdays.model.Status;
import birthdays.view.BirthdaysRootView;
import birthdays.view.elements.BirthdayTable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class ServiceController {

    public static final Logger log = Logger.getLogger("Panda");

    public BackupController backupController;
    public DatabaseController databaseController;
    public ResourceController resourceController;
    public PropertiesController propertiesController;
    public Options options;

    private static BirthdaysRootView birthdaysRootView;
    private BirthdayTable birthdaysTable;

    public static ServiceController instance = null;
    public static synchronized ServiceController getInstance(String resourcePath) {

        if (instance == null) {
            instance = new ServiceController(resourcePath);
        }
        return instance;
    }
    public static synchronized ServiceController getInstance() {
        return instance;
    }

    public ServiceController(String moduleResourcesPath) {
        try {
            resourceController = ResourceController.getInstance(moduleResourcesPath);
            propertiesController = PropertiesController.getInstance(resourceController.getFilePathNoStatic("Properties.properties"));
            databaseController = DatabaseController.getInstance(resourceController.getFilePathNoStatic("bdays.db"));
            backupController = BackupController.getInstance(resourceController.getFilePathNoStatic("XMLBackUp.xml"));
            options = propertiesController.initOptions();
        } catch (SQLException sql) {
//            setInfo(Status.ERROR, "Database did something bad. Init Failed");
        } catch (IOException ioe) {
//            setInfo(Status.ERROR, "Init Error. Some files or paths is corrupted");
        }
        options.setScreehHeight(checkDimension(true));
        options.setScreenWidth(checkDimension(false));

        birthdaysTable = new BirthdayTable();
        refresh();
    }

    public ArrayList<BDayUnit> selectAll(){
        ArrayList<BDayUnit> result = null;
        try{
            result = databaseController.selectAll();
        }catch (SQLException e){
            setInfo(Status.ERROR, "Database did something bad. Create Failed");
        }
        return result;
    }

    //CRUID Fields

    public void create(BDayUnit unit) {
        try {
            databaseController.addUnit(unit);
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad. Create Failed");
        }
        refresh();
    }

    public void update(BDayUnit unit) {
        try {
            databaseController.updateFullUnit(unit);
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad. Update Failed");
        }
    }

    public void delete(BDayUnit unit) {
        try {
            databaseController.deleteUnit(unit.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        try {
            ArrayList<BDayUnit> temp = databaseController.selectAll();
            birthdaysTable.refreshTable(temp);
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad. Refresh Failed");
        }
    }

    // Save-Load operations

    public void save() {
        try {
            backupController.save(databaseController.selectAll());
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad. Save Error");
        } catch (Exception e) {
            e.printStackTrace();
            setInfo(Status.ERROR, "Save Error. Smth with file");
        }
    }

    public void saveToDirectory(String filePath) {
        try {
            backupController.saveToDirectory(filePath, databaseController.selectAll());
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad. Save Error");
        } catch (Exception e) {
            e.printStackTrace();
            setInfo(Status.ERROR, "Save Error. Smth with file");
        }
    }

    public void load() {
        try {
            ArrayList<BDayUnit> xmlList = backupController.load();
            if (xmlList.size() > 0) {
                databaseController.clearBase();
                for (BDayUnit unit : xmlList) {
                    databaseController.addUnit(unit);
                }
            } else {
                setInfo(Status.WARNING, "XML-file broken or empty");
            }
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad or file is corrupted");
        } catch (Exception e) {
            setInfo(Status.ERROR, "Database did something bad or file is corrupted");
        }
        refresh();
    }

    public void loadFromDirectory(String filepath) {
        try {
            ArrayList<BDayUnit> xmlList = backupController.loadFromDirectory(filepath);
            if (xmlList.size() > 0) {
                databaseController.clearBase();
                for (BDayUnit unit : xmlList) {
                    databaseController.addUnit(unit);
                }
            } else {
                setInfo(Status.WARNING, "XML-file broken or empty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            setInfo(Status.ERROR, "Database did something bad or file is corrupted");
        } catch (Exception e) {
            e.printStackTrace();
            setInfo(Status.ERROR, "Database did something bad or file is corrupted");
        }
        refresh();
    }



    //special

    public void search(KeyCode code, String value) {
        ArrayList<BDayUnit> foundedFields = new ArrayList<>();
        try {
            foundedFields = databaseController.search(value);
            switch (code.toString()) {
                case ("BACK_SPACE"):
                    foundedFields = databaseController.search(value);
                case ("DELETE"):
                    foundedFields = databaseController.search(value);
            }
            if (value.equals("")) {
                foundedFields = databaseController.selectAll();
            }
        } catch (SQLException e) {
            setInfo(Status.ERROR, "Database did something bad. Search Failed");
        }
        birthdaysTable.refreshTable(foundedFields);
        birthdaysRootView.setInfo(Status.READY);

    }


    //outher

    public int checkDimension(boolean isheight) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (isheight) {
            return (int) screenSize.getHeight();
        } else {
            return (int) screenSize.getWidth();
        }
    }

    public void setInfo(Status status) {
        switch (status) {
            case WARNING:
                log.warning(status.getMessage());
                break;
            case ERROR:
                log.warning(status.getMessage());
                break;
        }
        birthdaysRootView.setInfo(status);
    }

    public void setInfo(Status status, String message) {
        switch (status) {
            case WARNING:
                log.warning(status.getMessage() + message);
                break;
            case ERROR:
                log.warning(status.getMessage() + message);
                break;
        }
        birthdaysRootView.setInfo(status, message);
    }

    public void alert(Status status, String message) {
        birthdaysRootView.alert(status, message);
        setInfo(status, message);
    }

    public BirthdayTable getBirthdaysTable() {
        return birthdaysTable;
    }

    public void showRootMenu() {
        birthdaysRootView.showRootMenu();
    }

    public static BorderPane start(String moduleResourcesPath) {
        ServiceController.getInstance(moduleResourcesPath);
        birthdaysRootView = new BirthdaysRootView();
        return birthdaysRootView;
    }
}
