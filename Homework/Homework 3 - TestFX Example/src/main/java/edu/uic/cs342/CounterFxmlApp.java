package edu.uic.cs342;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class CounterFxmlApp extends Application {
    @Override public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CounterView.fxml"));
        stage.setScene(new Scene(loader.load(), 340, 210));
        stage.setTitle("FXML Counter"); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
