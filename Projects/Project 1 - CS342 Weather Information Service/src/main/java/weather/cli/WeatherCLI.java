package weather.cli;

import weather.model.Location;
import weather.service.WeatherService;

import java.util.Map;
import java.util.Scanner;

// Handles all user input and output in the Command Line Interface
public class WeatherCLI
{
    private final WeatherService weatherService;
    private final Map<String, Location> locationDatabase;

    // Constructs the CLI with the required dependencies and a database
    public WeatherCLI(WeatherService weatherService)
    {
        this.weatherService = weatherService;

        this.locationDatabase = Map.of(
                "new york", Location.NEW_YORK,
                "los angeles", Location.LOS_ANGELES,
                "chicago", Location.CHICAGO,
                "houston", Location.HOUSTON,
                "phoenix", Location.PHOENIX,
                "philadelphia", Location.PHILADELPHIA,
                "san antonio", Location.SAN_ANTONIO,
                "san diego", Location.SAN_DIEGO,
                "dallas", Location.DALLAS,
                "fort worth", Location.FORT_WORTH

        );
    }

    // Main CLI function
    public void start()
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the Weather Information Service.");
        System.out.println("Type 'help' for a list of commands.");

        // Main CLI loop
        while (true)
        {
            System.out.print("\nweather> ");
            String userInput = scanner.nextLine().trim();

            if (userInput.isEmpty())
            {
                continue;
            }

            // Splits the user's input string into two parts: The command and the arguments
            String[] userInputParts = userInput.split(" ", 2);
            String userCommand = userInputParts[0].toLowerCase();

            String userArguments = "";
            if (userInputParts.length > 1)
            {
                userArguments = userInputParts[1].trim();
            }
            else
            {
                userArguments = "";
            }

            // Used to determine what command to run based on the user's inputted command
            switch (userCommand)
            {
                case "help" -> printHelp();
                case "locations" -> printLocations();
                case "current" -> currentLocationWeather(userArguments);
                case "compare" -> compareLocations(userArguments);
                case "summary" -> printWeatherSummary(userArguments);
                case "warmest" -> printWarmestLocation();
                case "coldest" -> printColdestLocation();
                case "quit" ->
                {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Unknown command. Type 'help' for available commands.");
            }
        }
    }

    // Prints the help menu, accessed upon typing "help"
    private void printHelp()
    {
        System.out.println("""
                Available commands:
                  help                                 - Display this menu
                  locations                            - Display all known locations
                  current <location>                   - Show the current weather
                  compare <location1> "<location2>"    - Compare the weather between two locations (use quotes for multi-word cities)
                  summary <location>                   - Show a brief weather summary
                  warmest                              - Show the warmest known location from a list of locations
                  coldest                              - Show the coldest known location from a list of locations
                  quit                                 - Exits the application
                """);
    }

    // Prints all known location names, accessed upon typing "locations"
    private void printLocations()
    {
        System.out.println("Known locations:");

        // Creates an array
        java.util.List<String> names = new java.util.ArrayList<>();

        // Extract the names from the Location objects
        for (Location location : locationDatabase.values())
        {
            names.add(location.name());
        }

        // Sort the list alphabetically
        java.util.Collections.sort(names);

        // Print each location name
        for (String name : names)
        {
            System.out.println("  - " + name);
        }
    }

    // Displays the current weather for a given location, accessed upon typing "current <location>"
    private void currentLocationWeather(String argument)
    {
        Location location = findLocation(argument);
        if (location != null)
        {
            System.out.println(weatherService.getCurrentWeatherDisplay(location));
        }
    }

    // Compares the weather of two given locations
    private void compareLocations(String arguments)
    {
        String[] locations = parseCompArgs(arguments);

        // If there are fewer than 2 locations given, output an error
        if (locations.length != 2)
        {
            System.out.println("Error: The 'compare' command requires exactly two locations.");
            System.out.println("Usage: compare <location1> <location2>");
            return;
        }

        Location location1 = findLocation(locations[0]);
        Location location2 = findLocation(locations[1]);

        if (location1 != null && location2 != null)
        {
            System.out.println(weatherService.getComparisonWeatherDisplay(location1, location2));
        }
    }

    // Prints a summary of the weather for a given location, accessed upon typing "summary <location>"
    private void printWeatherSummary(String argument)
    {
        Location location = findLocation(argument);
        if (location != null)
        {
            System.out.println(weatherService.getSummaryWeatherDisplay(location));
        }
    }

    // Prints the warmest location, accessed upon typing "warmest"
    private void printWarmestLocation()
    {
        System.out.println(weatherService.getWarmestLocationWeatherDisplay(locationDatabase.values()));
    }

    // Prints the coldest location, accessed upon typing "coldest"
    private void printColdestLocation()
    {
        System.out.println(weatherService.getColdestLocationWeatherDisplay(locationDatabase.values()));
    }

    // Helper function - Looks up a location from the database
    private Location findLocation(String locationName)
    {
        // Removes the quotes from the location's name if the user included quotes
        String cleanName = locationName.replace("\"", "").toLowerCase().trim();
        Location location = locationDatabase.get(cleanName);

        // If no location by the given name was found, output an error
        if (location == null)
        {
            System.out.println("Location '" + locationName + "' not found. Type 'locations' to see available options.");
        }

        return location;
    }

    // Helper function - Parses arguments for the compare command
    private String[] parseCompArgs(String arguments)
    {
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(arguments);
        java.util.List<String> matches = new java.util.ArrayList<>();

        while (m.find())
        {
            matches.add(m.group(1).replace("\"", ""));
        }

        return matches.toArray(new String[0]);
    }
}