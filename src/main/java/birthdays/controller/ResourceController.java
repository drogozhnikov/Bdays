package birthdays.controller;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class ResourceController {

    public static ResourceController instance = null;
    private String moduleResourcesPath;

    public ResourceController(String moduleResourcesPath) {
        this.moduleResourcesPath = moduleResourcesPath;
    }

    public static synchronized ResourceController getInstance(String moduleResourcesPath) {
        if (instance == null) {
            instance = new ResourceController(moduleResourcesPath);
        }
        return instance;
    }

    public String getFilePathNoStatic(String fileName) {
        String resourcePath = System.getProperty("user.dir") + moduleResourcesPath;
        File root = new File(resourcePath);
        String result = "";
        try {
            boolean recursive = true;

            Collection<File> files = FileUtils.listFiles(root, null, recursive);

            for (Iterator<File> iterator = files.iterator(); iterator.hasNext(); ) {
                File file =  iterator.next();
                if (file.getName().equals(fileName))
                    result = file.getAbsolutePath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
