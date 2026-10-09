package weather.service;

import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.provider.WeatherProviderException;

public class WeatherService
{
    private final WeatherDataProvider weatherDataProvider;

    // Constructs the WeatherService using the given weatherDataProvider
    public WeatherService(WeatherDataProvider weatherDataProvider)
    {
        this.weatherDataProvider = weatherDataProvider;
    }

    // Formats the current weather for a given location
    public String getCurrentWeatherDisplay(Location location)
    {
        try
        {
            WeatherData weatherData = weatherDataProvider.getCurrentWeather(location);

            return """
                    Current Weather for %s:
                    Temperature: %.1f ºC
                    Wind Speed: %.1f km/h
                    Humidity: %d%%
                    """.formatted(location.name(), weatherData.currTemperature(), weatherData.windSpeed(), weatherData.relativeHumidity());
        }
        catch (WeatherProviderException e)
        {
            return "Error retrieving weather data for " + location.name() + ": " + e.getMessage();
        }
    }

    // Compares the weather of two given locations
    public String getComparisonWeatherDisplay(Location location1, Location location2)
    {
        try
        {
            WeatherData weatherData1 = weatherDataProvider.getCurrentWeather(location1);
            WeatherData weatherData2 = weatherDataProvider.getCurrentWeather(location2);

            String temperatureDiff = String.format("%.1f", Math.abs(weatherData1.currTemperature() - weatherData2.currTemperature()));
            String warmerCity = "";
            if (weatherData1.currTemperature() > weatherData2.currTemperature())
            {
                warmerCity = location1.name();
            }
            else
            {
                warmerCity = location2.name();
            }

            return """
                   Comparison: %s vs %s
                   %s: %.1f °C, %d%% Humidity, %.1f km/h Wind
                   %s: %.1f °C, %d%% Humidity, %.1f km/h Wind
                   
                   %s is warmer by %s °C.
                   """.formatted(
                    location1.name(), location2.name(),
                    location1.name(), weatherData1.currTemperature(), weatherData1.relativeHumidity(), weatherData1.windSpeed(),
                    location2.name(), weatherData2.currTemperature(), weatherData2.relativeHumidity(), weatherData2.windSpeed(),
                    warmerCity, temperatureDiff);
        }
        catch (WeatherProviderException e)
        {
            return "Error during comparison: " + e.getMessage();
        }
    }

    // Outputs a short summary of the current weather conditions
    public String getSummaryWeatherDisplay(Location location)
    {
        try
        {
            WeatherData weatherData = weatherDataProvider.getCurrentWeather(location);

            String temperatureDescription = "";
            String windDescription = "";

            if (weatherData.currTemperature() > 25)
            {
                temperatureDescription = "warm";
            }
            else if (weatherData.currTemperature() < 10)
            {
                temperatureDescription = "cold";
            }
            else
            {
                temperatureDescription = "mild";
            }

            if (weatherData.windSpeed() > 20)
            {
                windDescription = "windy";
            }
            else
            {
                windDescription = "calm";
            }

            return String.format("It is currently %s and %s in %s, with a temperature of %.1f °C.", temperatureDescription, windDescription, location.name(), weatherData.currTemperature());
        }
        catch (WeatherProviderException e)
        {
            return "Error generating summary for " + location.name() + ": " + e.getMessage();
        }
    }

    // Finds the warmest location from a list of locations
    public String getWarmestLocationWeatherDisplay(Iterable<Location> locations)
    {
        Location warmestLocation = null;
        double maxTemperature = -10000.0;

        for (Location loc : locations)
        {
            try
            {
                WeatherData weatherData = weatherDataProvider.getCurrentWeather(loc);
                if (weatherData.currTemperature() > maxTemperature)
                {
                    maxTemperature = weatherData.currTemperature();
                    warmestLocation = loc;
                }
            }
            catch (WeatherProviderException e)
            {
                System.err.println("Warning: Could not retrieve data for " + loc.name() + " (" + e.getMessage() + ")");
            }
        }

        if (warmestLocation == null)
        {
            return "Error: Could not retrieve weather data for any locations.";
        }

        return String.format("The warmest location is %s with a temperature of %.1f ºC.", warmestLocation.name(), maxTemperature);
    }

    // Finds the coldest location from a list of locations
    public String getColdestLocationWeatherDisplay(Iterable<Location> locations)
    {
        Location coldestLocation = null;
        double minTemperature = 10000.0;

        for (Location loc : locations)
        {
            try
            {
                WeatherData weatherData = weatherDataProvider.getCurrentWeather(loc);
                if (weatherData.currTemperature() < minTemperature)
                {
                    minTemperature = weatherData.currTemperature();
                    coldestLocation = loc;
                }
            }
            catch (WeatherProviderException e)
            {
                System.err.println("Warning: Could not retrieve data for " + loc.name() + " (" + e.getMessage() + ")");
            }
        }

        if (coldestLocation == null)
        {
            return "Error: Could not retrieve weather data for any locations.";
        }

        return String.format("The coldest location is %s with a temperature of %.1f ºC.", coldestLocation.name(), minTemperature);
    }
}
