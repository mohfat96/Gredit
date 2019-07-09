package gui.sample;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Optional;

public class AlertWindow {

    public enum Pressed {
        Yes, No, Cancel
    }

    public static void error(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }


    public static Pressed confirm(String title, String message) {


        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(message);
        ButtonType buttonTypeYes = new ButtonType("Yes");
        ButtonType buttonTypeNo = new ButtonType("No");
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo, buttonTypeCancel);


        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeYes) {
            return Pressed.Yes;
        } else if (result.get() == buttonTypeNo) {
            return Pressed.No;
        } else {
            return Pressed.Cancel;
        }

    }


    public static void info(String header , String message, ArrayList<Node> nodes){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText(header);
        FlowPane fp = new FlowPane();
        Label lbl = new Label(message);
        fp.getChildren().addAll( lbl);
        if (nodes != null){
            fp.getChildren().addAll(nodes);
        }
        alert.getDialogPane().contentProperty().set( fp );
        alert.showAndWait();

    }


}

