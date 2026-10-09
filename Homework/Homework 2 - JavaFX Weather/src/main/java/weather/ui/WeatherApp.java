package weather.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Starter application shell.
 *
 * The small launcher/navigation shell is provided. Students complete BOTH
 * required weather dashboard views.
 */
public class WeatherApp extends Application {

    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("CS 342 Weather Dashboard");
        showHome();
        stage.show();
    }

    private void showHome() {
        Label title = new Label("Weather Dashboard Homework");
        title.getStyleClass().add("title");

        Label directions = new Label(
                "Implement the weather dashboard twice: once programmatically and once with FXML."
        );
        directions.setWrapText(true);

        Button programmaticButton = new Button("Programmatic View");
        programmaticButton.setOnAction(event -> showProgrammaticView());

        Button fxmlButton = new Button("FXML View");
        fxmlButton.setOnAction(event -> showFxmlView());

        HBox buttons = new HBox(12, programmaticButton, fxmlButton);
        VBox center = new VBox(18, title, directions, buttons);
        center.getStyleClass().add("home-panel");

        BorderPane root = new BorderPane(center);
        setScene(root);
    }

    private void showProgrammaticView() {
        ProgrammaticWeatherView view = new ProgrammaticWeatherView(this::showHome);
        setScene(view);
    }

    private void showFxmlView() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    WeatherApp.class.getResource("/weather/weather-view.fxml")
            );
            Parent root = loader.load();
            setScene(root);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Could not load FXML view");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void setScene(Parent root) {
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(
                WeatherApp.class.getResource("/weather/weather.css").toExternalForm()
        );
        stage.setScene(scene);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
