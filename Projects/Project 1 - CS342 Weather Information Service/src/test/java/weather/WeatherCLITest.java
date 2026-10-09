package weather;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import weather.cli.WeatherCLI;
import weather.model.Location;
import weather.model.WeatherData;
import weather.provider.WeatherDataProvider;
import weather.provider.WeatherProviderException;
import weather.service.WeatherService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class WeatherCLITest
{

    // Used for simulating input and output within the tests
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream testOut;

    // Allows the tests to run without requiring an internet connection
    private static class StubWeatherProvider implements WeatherDataProvider
    {
        @Override
        public WeatherData getCurrentWeather(Location location) throws WeatherProviderException
        {
            return new WeatherData(20.0, 50.0, 50);
        }
    }

    @BeforeEach
    public void setUp()
    {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void tearDown()
    {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    // Helper function - Simulates user input in the CLI
    // Note: The added "quit" part ensures that the while(true) loop always ends at some point
    private void runCLIWithInput(String inputCommands)
    {
        String fullInput = inputCommands + System.lineSeparator() + "quit" + System.lineSeparator();
        System.setIn(new ByteArrayInputStream(fullInput.getBytes()));

        WeatherService service = new WeatherService(new StubWeatherProvider());
        WeatherCLI cli = new WeatherCLI(service);
        cli.start();
    }

    @Test
    public void testHelpCommand()
    {
        runCLIWithInput("help");
        String output = testOut.toString();
        assertTrue(output.contains("Available commands:"));
        assertTrue(output.contains("Show a brief weather summary"));
    }

    @Test
    public void testLocationsCommand()
    {
        runCLIWithInput("locations");
        String output = testOut.toString();
        assertTrue(output.contains("Known locations:"));
        assertTrue(output.contains(Location.CHICAGO.name()));
        assertTrue(output.contains(Location.NEW_YORK.name()));
    }

    @Test
    public void testCurrentCommandKnownLocation()
    {
        runCLIWithInput("current chicago");
        String output = testOut.toString();
        assertTrue(output.contains(Location.CHICAGO.name()));
        assertTrue(output.contains("20.0") || output.contains("20,0"));
    }

    @Test
    public void testCommandUnknownLocation()
    {
        runCLIWithInput("current atlantis");
        String output = testOut.toString();
        assertTrue(output.contains("Location 'atlantis' not found"));
    }

    @Test
    public void testUnknownCommand()
    {
        runCLIWithInput("dance");
        String output = testOut.toString();
        assertTrue(output.contains("Unknown command. Type 'help' for available commands."));
    }
}