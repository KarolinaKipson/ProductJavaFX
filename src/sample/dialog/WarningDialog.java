package sample.dialog;

import javafx.scene.control.Alert;

public class WarningDialog  {

    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(message);
        alert.show();
    }

}
