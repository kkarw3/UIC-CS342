package weather.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import weather.service.WeatherService;
import weather.ui.ProgrammaticWeatherView;

public class WeatherApp extends Application
{
    @Override
    public void start(Stage mainStage)
    {
        WeatherService weatherService = new WeatherService();
        ProgrammaticWeatherView programmaticView = new ProgrammaticWeatherView(weatherService);

        Scene scene = new Scene(programmaticView, 500, 350);

        mainStage.setTitle("Weather Dashboard");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
