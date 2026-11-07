package edu.uic.cs342;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
public class AmountFormApp extends Application {
    @Override public void start(Stage stage) {
        TextField amount = new TextField(); amount.setId("amountField");
        Button calculate = new Button("Calculate"); calculate.setId("calculateButton");
        Label error = new Label(); error.setId("errorLabel");
        AmountValidator validator = new AmountValidator();
        calculate.setOnAction(e -> error.setText(validator.isValid(amount.getText())
            ? "Amount accepted" : "Enter a valid amount"));
        VBox root = new VBox(12, new Label("Amount"), amount, calculate, error);
        root.setStyle("-fx-padding: 24;");
        stage.setScene(new Scene(root, 350, 220)); stage.setTitle("Amount Form"); stage.show();
    }
    public static void main(String[] args) { launch(args); }
}
