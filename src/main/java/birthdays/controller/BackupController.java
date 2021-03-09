package birthdays.controller;

import birthdays.model.BDayUnit;
import birthdays.utils.io.XmlReader;
import birthdays.utils.io.XmlWriter;

import java.util.ArrayList;

public class BackupController {

    public static BackupController instance = null;

    private final XmlReader reader;
    private final XmlWriter writer;

    public BackupController(String resourcePath) {
        this.reader = new XmlReader(resourcePath);
        this.writer = new XmlWriter(resourcePath);
    }

    public static synchronized BackupController getInstance(String resourcePath) {

        if (instance == null) {
            instance = new BackupController(resourcePath);
        }
        return instance;
    }

    public static synchronized BackupController getInstance() {
        return instance;
    }

    public void save(ArrayList<BDayUnit> inputList) throws Exception {
        writer.saveBdays(inputList);
    }

    public ArrayList<BDayUnit> load() throws Exception {
        return reader.readBdays();
    }

    public void saveToDirectory(String filePath, ArrayList<BDayUnit> inputList) throws Exception {
        XmlWriter writer = new XmlWriter(filePath);
        writer.saveBdays(inputList);
    }

    public ArrayList<BDayUnit> loadFromDirectory(String filePath) throws Exception {
        XmlReader reader = new XmlReader(filePath);
        return reader.readBdays();
    }

}