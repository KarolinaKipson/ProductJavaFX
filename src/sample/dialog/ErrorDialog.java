package sample.dialog;


import javafx.scene.control.Alert;

public class ErrorDialog  {

    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(message);
        alert.show();
    }

}
