package weather.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import weather.service.WeatherService;

/** Controller for weather-view.fxml. */
public class WeatherController {
    private final WeatherService weatherService = new WeatherService();

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        statusLabel.setText("Select Refresh Weather to load data.");
    }

    /** Named FXML event-handler requirement. */
    @FXML
    private void handleRefresh() {
        // TODO:
        // 1. Call weatherService.getCurrentWeather().
        // 2. Update ALL FXML controls and visualizations.
        // 3. Show a meaningful user-visible error on failure.
        statusLabel.setText("TODO: load and display current weather");
    }
}
