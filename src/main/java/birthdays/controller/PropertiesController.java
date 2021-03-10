package birthdays.controller;

import birthdays.model.Options;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;

public class PropertiesController {

    public static PropertiesController instance = null;
    public final String path;

    java.util.Properties properties = new java.util.Properties();
    FileInputStream fileInputStream;

    public PropertiesController(String filePath) {
        this.path = filePath;
    }

    public static synchronized PropertiesController getInstance(String filePath) {
        if (instance == null) {
            instance = new PropertiesController(filePath);
        }
        return instance;
    }

    public Options initOptions() throws IOException, NullPointerException {
        try {
            fileInputStream = new FileInputStream(path);
            properties.load(fileInputStream);
        } finally {
            fileInputStream.close();
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Options options = new Options();
        options.setFullScreenHeigth((int) screenSize.getHeight());
        options.setFullScreenWidth((int) screenSize.getWidth());

        options.setCdHeigth(getIntValue("changeDataPanelHeigth"));
        options.setCdWidth(getIntValue("changeDataPanelWidth"));

        options.setScreehHeight(getIntValue("screenH"));
        options.setScreenWidth(getIntValue("screenW"));

        Options.setInstance(options);
        return options;
    }

    public int getIntValue(String valueName) {
        return Integer.parseInt(properties.getProperty(valueName));
    }

}
