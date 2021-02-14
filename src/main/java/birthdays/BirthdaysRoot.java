package birthdays;

import birthdays.controller.ServiceController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class BirthdaysRoot extends Application {

    String cssPath = "file:///D:/Css/FlatBee-master/src/main/java/com/github/karlthebee/flatbee/FlatBee.css";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        try {

            ServiceController serviceController = ServiceController.getInstance("\\src");
//            String pandaPath = serviceController.resourceController.getFilePathNoStatic("panda.png");
//            primaryStage.getIcons().add(new Image("file:"+pandaPath));

            Scene panda = new Scene(ServiceController.start("\\src"), 900, 600);
//                panda.getStylesheets().add(cssPath);

            primaryStage.setTitle("Birthdays");
            primaryStage.setScene(panda);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
