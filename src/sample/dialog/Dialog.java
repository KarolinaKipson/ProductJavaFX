package sample.dialog;

public interface Dialog {
    public void show(String message);
    public void showAndWait(String message) throws InterruptedException;
}
