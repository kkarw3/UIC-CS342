package weather.controller;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import weather.model.WeatherReading;
import weather.service.WeatherService;


/** Controller for weather-view.fxml. */
public class WeatherController
{
    @FXML private VBox newYorkCityUICard;
    @FXML private Label newYorkCityTemperatureLabel;
    @FXML private ProgressBar newYorkCityProgressBar;

    @FXML private VBox losAngelesUICard;
    @FXML private Label losAngelesTemperatureLabel;
    @FXML private ProgressBar losAngelesProgressBar;

    @FXML private VBox chicagoUICard;
    @FXML private Label chicagoTemperatureLabel;
    @FXML private ProgressBar chicagoProgressBar;

    @FXML private VBox houstonUICard;
    @FXML private Label houstonTemperatureLabel;
    @FXML private ProgressBar houstonProgressBar;

    @FXML private VBox phoenixUICard;
    @FXML private Label phoenixTemperatureLabel;
    @FXML private ProgressBar phoenixProgressBar;

    @FXML private BarChart<String, Number> temperatureChart;
    private XYChart.Series<String, Number> chartDataSeries;

    @FXML private ComboBox<String> citySelector;
    @FXML private Label statusLabel;

    private WeatherService weatherService;

    @FXML
    private void initialize()
    {
        chartDataSeries = new XYChart.Series<>();
        chartDataSeries.getData().add(new XYChart.Data<>("New York", 0));
        chartDataSeries.getData().add(new XYChart.Data<>("Los Angeles", 0));
        chartDataSeries.getData().add(new XYChart.Data<>("Chicago", 0));
        chartDataSeries.getData().add(new XYChart.Data<>("Houston", 0));
        chartDataSeries.getData().add(new XYChart.Data<>("Phoenix", 0));
        temperatureChart.getData().add(chartDataSeries);
    }

    // Injects the shared app layer into the controller
    public void setWeatherService(WeatherService weatherService)
    {
        this.weatherService = weatherService;
        handleRefresh();
    }

    @FXML
    private void handleRefresh()
    {
        if (weatherService == null) { return; }
        statusLabel.setText("Retrieving Weather Data...");

        try
        {
            List<WeatherReading> weatherReadings = weatherService.getCurrentWeather();

            for (WeatherReading reading : weatherReadings)
            {
                String city = reading.location().city();
                double temperature = reading.temperatureF();
                String formattedTemperature = String.format("%.1f °F", reading.temperatureF());

                double progress = Math.max(0.0, Math.min(1.0, temperature / 100.0));

                switch (city)
                {
                    case "Chicago" ->
                    {
                        chicagoTemperatureLabel.setText(formattedTemperature);
                        chicagoProgressBar.setProgress(progress);
                    }
                    case "Los Angeles" ->
                    {
                        losAngelesTemperatureLabel.setText(formattedTemperature);
                        losAngelesProgressBar.setProgress(progress);
                    }
                    case "New York" ->
                    {
                        newYorkCityTemperatureLabel.setText(formattedTemperature);
                        newYorkCityProgressBar.setProgress(progress);
                    }
                    case "Houston" ->
                    {
                        houstonTemperatureLabel.setText(formattedTemperature);
                        houstonProgressBar.setProgress(progress);
                    }
                    case "Phoenix" ->
                    {
                        phoenixTemperatureLabel.setText(formattedTemperature);
                        phoenixProgressBar.setProgress(progress);
                    }
                }

                for (XYChart.Data<String, Number> data : chartDataSeries.getData())
                {
                    if (data.getXValue().equals(city))
                    {
                        data.setYValue(temperature);
                        break;
                    }
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

    // Handles displaying a selected city
    @FXML
    public void handleCitySelection()
    {
        String selectedCity = citySelector.getValue();

        chicagoUICard.getStyleClass().remove("highlighted-card");
        losAngelesUICard.getStyleClass().remove("highlighted-card");
        newYorkCityUICard.getStyleClass().remove("highlighted-card");
        houstonUICard.getStyleClass().remove("highlighted-card");
        phoenixUICard.getStyleClass().remove("highlighted-card");

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
        else if ("Houston".equals(selectedCity))
        {
            houstonUICard.getStyleClass().add("highlighted-card");
        }
        else if ("Phoenix".equals(selectedCity))
        {
            phoenixUICard.getStyleClass().add("highlighted-card");
        }

    }

    // Handles errors
    private void  showErrorAlert(String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Weather Retrieval Error");
        alert.setHeaderText("Unable to Fetch Weather Data");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
