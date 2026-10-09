This is a command-line program that displays weather data for several locations using the Open-Meteo API.

--- Members ---

Krrish Karwal
UIN: 676475701
Email: kkarw3@uic.edu


--- Compiling and Running the Program ---

Compile command: mvn compile

Run command: mvn exec:java -Dexec.mainClass="weather.Main"

Note: The program will not run if any files bundled in the .zip file are deleted or improperly edited. Modify at your own risk.

--- Running the Test Suite ---

Test command: mvn test

This will compile and run the tests automatically.

--- Design ---

The design of this program is split up into multiple parts for ease of development and organization. There are 4 major components:


Model - The Model component contains the core data objects for the program (Location data, weather data, etc.). The Model then passes this data to the other major components.

Provider - The Provider component handles everything relating to the outside of the program (Network communication, external APIs). The Provider parses the raw JSON data from the Open Meteo API into WeatherData objects to be used in the program.

Service - The Service component is where the calculations happen. Data is received from the Provider, and the Service performs calculations like comparing the temperatures of two different locations or finding the warmest/coldest location from a list of locations. The results are then formatted into readable strings.

Command-Line Interface (CLI) - The CLI component handles the user interface parts of the program, namely receiving the user's inputs, passing them along to the Service component, and then displaying the results of the given commands.

--- Interfaces ---

An interface is used in Provider (WeatherDataProvider) to define retrieving weather data. This is used so that 'WeatherService' does not have to depend on only one extremely specific implementation such as OpenMeteoWeatherProvider. In the event that someone would want to use a different weather API, you would just need to swap the definitions and rewire a few other parts for the different API to work, instead of rewriting the entire program just to use a different weather API.

-- Dependency Injection ---

This program uses two different constructor-based Dependency Injections: OpenMeteoWeatherProvider, which is injected into 'WeatherService', which itself is injected into 'WeatherCLI'. These injections occur entirely in Main.java. The reason for these two different injections is because it decouples the major components from each other. Because WeatherService is dependent on WeatherDataProvider and not OpenMeteoWeatherProvider directly, it makes the code more modular and also simplifies unit testing.

--- Testing ---

To test the program without using a live API , the WeatherDataProvider interface is used to simulate an actual API . In the testing suite, static inner classes are defined for the purpose of implementing the WeatherDataProvider interface in a simulated environment. Instead of making API requests, these mock classes return hardcoded objects with distinct values, which allow for tests involving things like the calculation logic or the CLI formatting.

--- Java 25 ---

This program utilizes features included in Java 25, namely:

Records - Used for 'Location' and 'WeatherData'. Records help generate constructors and getters, which drastically reduce the amount of code otherwise required for said objects.

Text Blocks - Used for 'WeatherService' and 'WeatherCLI' to format multi-line strings, which bypasses needing to concatenate multiple strings and newlines together with redundant code.

Switch statements - Used in 'WeatherCLI' to handle user commands. Specifically, the arrow syntax (->) makes it so that break statements aren't needed and simplifies the overall structure.

--- Additional Features ---
 
Aside from the required commands, I also added two commands, which find the warmest or coldest city from a list of cities. When run, WeatherService iterates over the list of cities and finds the city with either the warmest or coldest temperature. It then returns the city and its temperature in a formatted string.


*** Design Reflection Questions ***

1. What responsibilities were combined in the original starter code that you separated during refactoring?

From the initial starter code, I ended up separating several things into more organized classes and folders. Namely:

- Main was reduced to doing nothing more than simply assembling the core parts of the program together. This freed up Main completely and allowed me to separate the other parts of the program up into separate files & folders without anything being accidentally linked together.
- The initial service component included with the starter code simply printed directly to the console. I separated these two parts up by making WeatherService return formatted strings and wrote the console parts in WeatherCLI so that these weren't in the same class file.
- I also separated the networking components from the calculation components to make sure the two didn't interfere with each other.


2. How does programming to WeatherDataProvider make the program easier to change?

Programming to WeatherDataProvider makes the program easier to change by separating the calculation logic components from the network/infrastructure components. Because the calculations only really interacts with the network/infrastructure components through generic class functions and not the API itself, it makes it easier to switch APIs without rewriting entire chunks of code.


3. How does dependency injection make WeatherService easier to test?

Dependency injection made WeatherService easier to test because it allows for someone to pass a mock implementation of WeatherDataProvider directly to the WeatherService constructor. Instead of making an online API request, the constructor receives a object that contains hardcoded values to be tested. This makes the tests free from any online requirements and also lets them run faster.

4. Why should unit tests avoid depending on the live weather API?

Unit tests should generally be separate from live APIs to prevent issues with things like network connectivity or changes in data. If an internet connection is faulty, a unit test that relies on said internet connection will either fail to run or take a long time to run. Additionally, if a unit test relies on live data like today's temperature or wind speed, the test might run successfully on one day and fail on another day due to things like the temperature or wind speed changing between days.


5. If the current weather API were replaced with a completely different provider, which parts of your
application would need to change? Which parts should not need to change?

If the API were to change, the only thing that would really need to be rewritten would be the network/infrastructure component of my project. The only thing needing to change would be things like the API URL, JSON data format (if need be), or any other hooks that need to be configured. Everything else (WeatherService, WeatherCLI, and the Location & WeatherData components) shouldn't need to be changed, since these parts don't connect to an API directly at all.