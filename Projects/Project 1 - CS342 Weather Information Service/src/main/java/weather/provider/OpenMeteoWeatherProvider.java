package weather.provider;

import weather.model.Location;
import weather.model.WeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenMeteoWeatherProvider implements WeatherDataProvider
{
    private static final String API_BASE_URL = "https://api.open-meteo.com/v1/forecast";
    private final HttpClient httpClient;

    public OpenMeteoWeatherProvider()
    {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
    }

    // Gets the current weather for a given location
    @Override
    public WeatherData getCurrentWeather(Location location)
    {
        String url = buildUrl(location);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();

        // Gets the data for the requested location
        // If the location was not found, throw an error
        try
        {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());


            // Validates the HTTP status code before parsing
            if (response.statusCode() != 200)
            {
                throw new WeatherProviderException("Failed to fetch weather data. HTTP Status: " + response.statusCode());
            }

            return parseResponse(response.body());
        }
        catch (IOException | InterruptedException e)
        {
            throw new WeatherProviderException("Network error occurred while fetching weather data", e);
        }
    }

    // Constructs a URL based on a given location for use with Open-Meteo
    private String buildUrl(Location location)
    {
        return String.format("%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,wind_speed_10m", API_BASE_URL, location.latitude(), location.longitude());
    }

    // Extracts numerical values from a given JSON string
    // Throws an error if an expected field is missing
    private double extractMeasurement(String text, String regex)
    {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find())
        {
            return Double.parseDouble(matcher.group(1));
        }

        throw new IllegalStateException("Field not found for pattern: " + regex);
    }

    // Parses the JSON response string into a WeatherData record
    private WeatherData parseResponse(String json)
    {
        try
        {
            double temperature = extractMeasurement(json, "\"temperature_2m\":\\s*([\\d.-]+)");
            int humidity = (int) extractMeasurement(json, "\"relative_humidity_2m\":\\s*([\\d.-]+)");
            double windSpeed = extractMeasurement(json, "\"wind_speed_10m\":\\s*([\\d.-]+)");

            return new WeatherData(temperature, windSpeed, humidity);
        }
        catch (IllegalStateException e)
        {
            // Catches any parsing failures to prevent using fake values
            throw new WeatherProviderException("Malformed API response: Missing required weather fields.", e);
        }
    }
}