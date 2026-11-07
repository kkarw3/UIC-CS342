package edu.uic.cs342;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class CounterApp extends Application {
    @Override public void start(Stage stage) {
        Counter counter = new Counter();
        Label count = new Label("0"); count.setId("countLabel");
        Button add = new Button("Increment"); add.setId("incrementButton");
        add.setOnAction(e -> count.setText(Integer.toString(counter.increment())));
        VBox root = new VBox(12, count, add);
        root.setStyle("-fx-padding: 24; -fx-alignment: center;");
        stage.setScene(new Scene(root, 320, 180));
        stage.setTitle("Programmatic Counter"); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
