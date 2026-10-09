package weather.ui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import weather.service.WeatherService;

/**
 * TODO: Complete the programmatic version of the assignment here.
 * Do NOT use FXML in this class.
 */
public class ProgrammaticWeatherView extends BorderPane {
    private final WeatherService weatherService = new WeatherService();

    public ProgrammaticWeatherView(Runnable goHome) {
        Label title = new Label("Programmatic Weather View");
        title.getStyleClass().add("title");

        Label instructions = new Label(
                "TODO: Build the required dashboard using JavaFX controls created in Java."
        );
        instructions.setWrapText(true);

        Button refreshButton = new Button("Refresh Weather");
        refreshButton.getStyleClass().add("refresh-button");

        // TODO: Lambda event handler requirement.
        refreshButton.setOnAction(event -> {
            // 1. Call weatherService.getCurrentWeather().
            // 2. Update labels AND graphical representations.
            // 3. Display a user-visible error if retrieval fails.
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> goHome.run());

        HBox actions = new HBox(10, refreshButton, backButton);
        VBox starterContent = new VBox(15, title, instructions, actions);
        starterContent.getStyleClass().add("content-panel");
        setCenter(starterContent);

        // TODO: Add city controls/selection.
        // TODO: Add at least two graphical representations.
        // Hint: BarChart<String, Number>, ProgressBar, or javafx.scene.shape classes.
    }
}
