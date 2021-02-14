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

    public static synchronized PropertiesController getInstance(String filePath){
        if (instance == null) {
            instance = new PropertiesController(filePath);
        }
        return instance;
    }

    public static synchronized PropertiesController getInstance(){
        return instance;
    }

    public Options initOptions() throws IOException {
        try {
            fileInputStream = new FileInputStream(path);
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw e;
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Options options = new Options();
        try {
            options.setFullScreenHeigth((int) screenSize.getHeight());
            options.setFullScreenWidth((int) screenSize.getWidth());

            options.setCdHeigth(getIntValue("changeDataPanelHeigth"));
            options.setCdWidth(getIntValue("changeDataPanelWidth"));

            options.setScreehHeight(getIntValue("screenH"));
            options.setScreenWidth(getIntValue("screenW"));

            options.setPgLength(getIntValue("PG_length"));
            options.setPgLowCase(getStrValue("PG_lowCase"));
            options.setPgUpperCase(getStrValue("PG_upCase"));
            options.setPgNums(getStrValue("PG_numses"));

            options.setInstance(options);
        } catch (NumberFormatException e) {
        }

        return options;
    }

    public int getIntValue(String valueName) {
        return Integer.parseInt(properties.getProperty(valueName));
    }

    public String getStrValue(String valueName) {
        return (properties.getProperty(valueName));
    }

}
