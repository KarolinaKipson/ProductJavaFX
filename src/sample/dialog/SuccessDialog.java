package sample.dialog;

import javafx.scene.control.Alert;

public class SuccessDialog {

    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(message);
        alert.show();
    }


}
