package sample.dialog;

import javafx.scene.control.Alert;

public class SuccessDialog implements Dialog{


    @Override
    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(message);
        alert.show();
    }
}
