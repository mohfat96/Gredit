package core;


import gui.sample.AlertWindow;
import gui.sample.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import nu.pattern.OpenCV;

import java.io.File;
import java.nio.file.Paths;


public class Main extends Application {
    public static Stage primaryStage;
    public static String resourcesPath;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Main.primaryStage = primaryStage;
        File directory = new File(Paths.get("./autosaves").toAbsolutePath().toString());
        if (! directory.exists()){
            directory.mkdir();
        }
        Main.resourcesPath = Paths.get("./autosaves").toAbsolutePath().toString();

        OpenCV.loadShared();
        System.loadLibrary(org.opencv.core.Core.NATIVE_LIBRARY_NAME);

        startUp();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sample.fxml"));
        Parent root = loader.load();

        Controller controller = loader.getController();
        primaryStage.setTitle("Gredit");
        Scene scene = new Scene(root, 600, 400);
        scene.getStylesheets().add("/fxml/Design.css");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(625);
        primaryStage.setMinHeight(425);
        primaryStage.setMaximized(true);
        primaryStage.show();

        scene.setOnKeyPressed(controller::deleteBtnHandler);
        primaryStage.setOnCloseRequest(event -> {
                    try {
                        DataManager.getInstance().getConfig().saveAll("");
                    } catch (Exception e) {
                        AlertWindow.error("There was a problem automatically saving the current state. \n" +
                                "All Progress since the last save is lost. \n" + e.getMessage());
                    }
                }
        );
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes the Datamanager used to represent the Project opened with the File-Chooser-Dialog
     *
     * @author Simon Raffeck
     */
    private void startUp() {

        String path = resourcesPath + "/backUp.yaml";
        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            DataManager dataManager = new DataManager(Paths.get(path));
        }else {
            DataManager dataManager = new DataManager();
        }
    }
}