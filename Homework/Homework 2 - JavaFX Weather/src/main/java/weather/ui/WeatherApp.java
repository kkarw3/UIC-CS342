package weather.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import weather.controller.WeatherController;
import weather.service.WeatherService;
import java.net.URL;

import java.net.URL;

public class WeatherApp extends Application
{
    @Override
    public void start(Stage mainStage) throws Exception
    {
        WeatherService weatherService = new WeatherService();

        URL fxmlLocation = getClass().getResource("/weather/weather-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        WeatherController controller = loader.getController();
        controller.setWeatherService(weatherService);

        Scene scene = new Scene(root, 550, 400);

        mainStage.setTitle("Weather Dashboard");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}