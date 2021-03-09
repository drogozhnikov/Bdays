package birthdays;

import birthdays.controller.ServiceController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BirthdaysRoot extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        try {

            ServiceController serviceController = ServiceController.getInstance("\\src");

            String bdaysIcoPath = serviceController.resourceController.getFilePathNoStatic("Bdays.png");
            primaryStage.getIcons().add(new Image("file:" + bdaysIcoPath));

            Scene birthdays = new Scene(ServiceController.start("\\src"), 900, 600);

            primaryStage.setTitle("Birthdays");
            primaryStage.setScene(birthdays);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void setCSS(ServiceController serviceController, Scene scene) {
        String cssPath = "file:///" + serviceController.resourceController.getFilePathNoStatic("FlatBee.css");
        cssPath = cssPath.replace("\\", "/");
        scene.getStylesheets().add(cssPath);
    }

}
