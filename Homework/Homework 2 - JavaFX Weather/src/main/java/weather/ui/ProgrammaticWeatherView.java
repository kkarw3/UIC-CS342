package weather.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import weather.model.TargetLocation;
import weather.model.WeatherReading;
import weather.service.WeatherService;


public class ProgrammaticWeatherView extends BorderPane
{
    private final WeatherService weatherService;
    private final Map<String, CityUICard> cityUICards = new HashMap<>();
    private final ComboBox<String> citySelector = new ComboBox<>();
    private final Label statusLabel = new Label("Status: Ready");

    public ProgrammaticWeatherView(WeatherService weatherService)
    {
        this.weatherService = weatherService;
        initializeUI();
        refreshData();
    }

    // Sets up the UI for "The Weather Channel"
    private void initializeUI()
    {
        Label titleLabel = new Label("The Weather Channel");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button refreshButton = new Button("Refresh Weather");
        refreshButton.setOnAction(e -> refreshData());

        HBox topBar = new HBox(15, titleLabel, refreshButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 15, 0));
        setTop(topBar);

        VBox cardsContainer = new VBox(10);
        cardsContainer.setAlignment(Pos.TOP_CENTER);

        List<String> cities = List.of("New York", "Los Angeles", "Chicago");
        for (String city : cities)
        {
            CityUICard card = new CityUICard(city);
            cityUICards.put(city, card);
            cardsContainer.getChildren().add(card);
        }
        setCenter(cardsContainer);

        citySelector.getItems().addAll(cities);
        citySelector.setPromptText("Select City to Highlight");
        citySelector.setOnAction(e -> handleCitySelection());

        HBox bottomBar = new HBox(15, new Label("Current city:"), citySelector, statusLabel);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(15, 0, 0, 0));
        setBottom(bottomBar);
    }

    // This class is used for everything relating to the UI elements for cities
    private static class CityUICard extends HBox
    {
        private final Label nameLabel;
        private final Label temperatureLabel;

        // The actual UI element for each city
        public CityUICard(String cityName)
        {
            super(20);
            setPadding(new Insets(12));
            setAlignment(Pos.CENTER_LEFT);

            nameLabel = new Label(cityName);
            nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            temperatureLabel = new Label("-- °F");
            temperatureLabel.setStyle("-fx-font-size: 14px;");

            getChildren().addAll(nameLabel, temperatureLabel);
            setHighlighted(false);
        }

        // Updates the temperature of the current city
        public void updateTemperature(double temp)
        {
            temperatureLabel.setText(String.format("%.1f °F", temp));
        }

        // Highlights the current city in the UI
        public void setHighlighted(boolean highlighted)
        {
            if (highlighted)
            {
                setStyle("-fx-background-color: #e0f7fa; -fx-border-color: #00796b; " + "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            }
            else
            {
                setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #cccccc; " + "-fx-border-width: 1px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
            }
        }
    }

    // Helper function - Handles the code for the refresh button
    // Also gets data from the weatherService component
    public void refreshData()
    {
        statusLabel.setText("Retrieving Weather...");

        try
        {
            List<WeatherReading> weatherReadings = weatherService.getCurrentWeather();

            for (WeatherReading reading : weatherReadings)
            {
                // Get the city name from the reading and find the matching UI card
                CityUICard card = cityUICards.get(reading.location().city());

                if (card != null)
                {
                    // Update the card's temperature
                    card.updateTemperature(reading.temperatureF());
                }
            }

            statusLabel.setText("Weather updated successfully.");
        }
        catch (WeatherService.WeatherRetrievalException e)
        {
            statusLabel.setText("Error fetching weather data.");
            showErrorAlert(e.getMessage());
        }
        catch (Exception e)
        {
            statusLabel.setText("Unexpected error occurred.");
            showErrorAlert("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Helper function - Just highlights the city the user picked
    public void handleCitySelection()
    {
        String selectedCity = citySelector.getValue();
        for (Map.Entry<String, CityUICard> entry : cityUICards.entrySet())
        {
            boolean cityIsSelected = entry.getKey().equals(selectedCity);
            entry.getValue().setHighlighted(cityIsSelected);
        }
    }

    // Helper function - Error displayer
    private void showErrorAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Weather Retrieval Error");
        alert.setHeaderText("Unable to Fetch Weather Data");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
