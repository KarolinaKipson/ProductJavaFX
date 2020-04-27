package sample.dialog;

import javafx.scene.control.Alert;

public class WarningDialog implements Dialog {

    @Override
    public void show(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(message);
        alert.show();
    }

    @Override
    public void showAndWait(String message) throws InterruptedException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning!");
        alert.setHeaderText(message);
        alert.show();
        alert.wait(1000L);
    }
}
