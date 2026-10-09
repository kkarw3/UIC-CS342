package weather;

import org.junit.jupiter.api.Test;
import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.provider.WeatherProviderException;
import weather.service.WeatherService;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherServiceTest
{
    // A mock provider used for testing the weather service
    private static class MockWeatherProvider implements WeatherDataProvider
    {
        @Override
        public WeatherData getCurrentWeather(Location location) throws WeatherProviderException
        {
            if (Location.CHICAGO.equals(location))
            {
                return new WeatherData(15.0, 50.0, 10);
            }
            else if (Location.NEW_YORK.equals(location))
            {
                return new WeatherData(35.0, 80.0, 40);
            }
            else if (Location.LOS_ANGELES.equals(location))
            {
                return new WeatherData(-5.0, 20.0, 0);
            }
            throw new WeatherProviderException("Simulated API Error");
        }
    }

    private final WeatherService service = new WeatherService(new MockWeatherProvider());

    @Test
    public void testCurrentWeatherDisplaySuccess()
    {
        String result = service.getCurrentWeatherDisplay(Location.CHICAGO);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Verifies if it returns valid text
        assertFalse(result.toLowerCase().contains("error"));
    }

    @Test
    public void testCurrentWeatherDisplayFailure()
    {
        WeatherService errorService = new WeatherService(loc ->
        {
            throw new WeatherProviderException("Connection timeout");
        });

        String result = errorService.getCurrentWeatherDisplay(Location.CHICAGO);

        // Verify it handles exceptions and outputs an error message
        assertTrue(result.toLowerCase().contains("error"));
    }

    @Test
    public void testComparisonWeatherDisplayLoc1Warmer()
    {
        String result = service.getComparisonWeatherDisplay(Location.NEW_YORK, Location.CHICAGO);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.toLowerCase().contains("error"));
    }

    @Test
    public void testComparisonWeatherDisplayLoc2Warmer()
    {
        String result = service.getComparisonWeatherDisplay(Location.CHICAGO, Location.NEW_YORK);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.toLowerCase().contains("error"));
    }

    @Test
    public void testComparisonWeatherDisplayFailure()
    {
        WeatherService errorService = new WeatherService(loc ->
        {
            throw new WeatherProviderException("API Down");
        });

        String result = errorService.getComparisonWeatherDisplay(Location.CHICAGO, Location.NEW_YORK);
        assertTrue(result.toLowerCase().contains("error"));
    }

    @Test
    public void testSummaryWeatherDisplayWarmAndBreezy()
    {
        String result = service.getSummaryWeatherDisplay(Location.NEW_YORK);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertFalse(result.toLowerCase().contains("error"));
    }

    @Test
    public void testSummaryWeatherDisplayColdAndCalm()
    {
        String laResult = service.getSummaryWeatherDisplay(Location.LOS_ANGELES);

        assertNotNull(laResult);
        assertFalse(laResult.toLowerCase().contains("error"));

        // Ensures the logic actually branches: extreme cold should look different from extreme heat
        String nyResult = service.getSummaryWeatherDisplay(Location.NEW_YORK);
        assertNotEquals(nyResult, laResult, "Different weather should produce different summaries.");
    }

    @Test
    public void testSummaryWeatherDisplayFailure() {
        WeatherService errorService = new WeatherService(loc ->
        {
            throw new WeatherProviderException("Missing Data");
        });

        String result = errorService.getSummaryWeatherDisplay(Location.CHICAGO);
        assertTrue(result.toLowerCase().contains("error"));
    }
}