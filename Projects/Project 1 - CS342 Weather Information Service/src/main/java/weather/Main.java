package weather;

import weather.cli.WeatherCLI;
import weather.provider.OpenMeteoWeatherProvider;
import weather.provider.WeatherDataProvider;
import weather.service.WeatherService;

public class Main
{
    public static void main(String[] args)
    {
        // Initiates the infrastructure layer (handles external APIs)
        WeatherDataProvider weatherDataProvider = new OpenMeteoWeatherProvider();

        // Initiates the logic layer and injects the provider
        WeatherService weatherService = new WeatherService(weatherDataProvider);

        // Initiates the presentation layer and injects the service
        WeatherCLI weatherCLI = new WeatherCLI(weatherService);

        // Starts the CLI
        weatherCLI.start();
    }
}