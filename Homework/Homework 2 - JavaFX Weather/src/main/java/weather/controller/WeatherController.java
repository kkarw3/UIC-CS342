package weather.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import weather.model.WeatherReading;
import weather.service.WeatherService;

/** Controller for weather-view.fxml. */
public class WeatherController
{
    @FXML private HBox chicagoUICard;
    @FXML private Label chicagoTemperatureLabel;

    @FXML private HBox losAngelesUICard;
    @FXML private Label losAngelesTemperatureLabel;

    @FXML private HBox newYorkCityUICard;
    @FXML private Label newYorkCityTemperatureLabel;

    @FXML private ComboBox<String> citySelector;
    @FXML private Label statusLabel;

    private final WeatherService weatherService;

    @FXML
    private void initialize()
    {
        chicagoUICard.getStyleClass().add("city-card");
        losAngelesUICard.getStyleClass().add("city-card");
        newYorkCityUICard.getStyleClass().add("city-card");
    }

    // Injects the shared app layer into the controller
    public void setWeatherService(WeatherService weatherService)
    {
        this.weatherService = weatherService;
        handleRefresh();
    }

    /** Named FXML event-handler requirement. */
    @FXML
    private void handleRefresh()
    {
        // TODO:
        // 1. Call weatherService.getCurrentWeather().
        // 2. Update ALL FXML controls and visualizations.
        // 3. Show a meaningful user-visible error on failure.

        if (weatherService == null) { return; }

        try
        {
            List<WeatherReading> weatherReadings = weatherService.getCurrentWeather();

            for (WeatherReading reading : weatherReadings)
            {
                String city = reading.location().city();
                String formattedTemperature = String.format("%.1f °F", reading.temperatureF());

                switch (city)
                {
                    case "Chicago" -> chicagoTemperatureLabel.setText(formattedTemperature);
                    case "Los Angeles" -> losAngelesTemperatureLabel.setText(formattedTemperature);
                    case "New York" -> newYorkCityTemperatureLabel.setText(formattedTemperature);
                }
            }
            statusLabel.setText("Weather updated successfully.");
        }
        catch (WeatherService.WeatherRetrievalException e)
        {
            statusLabel.setText("Error fetching weather data.");
            showErrorAlert(e.getMessage());
        }
    }

    // Handles displaying a selected city
    @FXML
    public void handleCitySelection()
    {
        String selectedCity = citySelector.getValue();

        chicagoUICard.getStyleClass().remove("highlighted-card");
        losAngelesUICard.getStyleClass().remove("highlighted-card");
        newYorkCityUICard.getStyleClass().remove("highlighted-card");

        // Highlights the selected city
        if ("Chicago".equals(selectedCity))
        {
            chicagoUICard.getStyleClass().add("highlighted-card");
        }
        else if ("Los Angeles".equals(selectedCity))
        {
            losAngelesUICard.getStyleClass().add("highlighted-card");
        }
        else if ("New York".equals(selectedCity))
        {
            newYorkCityUICard.getStyleClass().add("highlighted-card");
        }
    }

    private void  showErrorAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Weather Retrieval Error");
        alert.setHeaderText("Unable to Fetch Weather Data");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
