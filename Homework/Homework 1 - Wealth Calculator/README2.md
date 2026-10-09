// Krrish Karwal
// NetID: kkarw3
// UIN: 676475701
// CS 342 - Software Design

*** 1. Class responsibilities: What is the responsibility of FixedWealthCalculator, VariableWealthCalculator, and ConsoleReporter? Why are these responsibilities separated into different classes? ***

FixedWealthCalculator takes an amount of money as input and calculates its future value using a fixed interest rate.
VariableWealthCalculator, on the other hand, calculates its future value based on different interest rates and
savings, producing a much more random value compared to FixedWealthCalculator.
ConsoleReporter is only used to format and print the results from both calculators to the console.
These are separated into different classes and files to organize the overall file structure of the program,
as well as making it easier to test and debug each class without conflicting errors.

*** 2. Dependency injection: What does dependency injection mean in this program? Identify the dependencies that are injected into VariableWealthCalculator. ***

For this program, dependency injection means that each class is given its required objects from other files,
instead of requiring each class to implement their required objects themselves. For VariableWealthCalculator,
the given dependencies in this case are RateFileReader and SavingsFileReader, which allow VariableWealthCalculator
to work as intended without needing to construct its own objects.

*** 3. Creating dependencies: Why are RateFileReader and SavingsFileReader created in Main.java and passed to VariableWealthCalculator instead of being created inside VariableWealthCalculator? ***

RateFileReader and SavingsFileReader are created in Main instead of VariableWealthCalculator to allow for
flexibility within the program. Instead of creating static hardcoded objects VariableWealthCalculator,
Main instead handles the workload of creating objects to pass to VariableWealthCalculator, and it also opens
up the possibility of reusing these objects for other classes.

*** 4. Records versus classes: Why are LumpSumInput, SavingsInput, PresentValueInput, and CalculationResult records, while FixedWealthCalculator and VariableWealthCalculator are regular classes? ***

LumpSumInput, SavingsInput, PresentValueInput, and CalculationResult are created as records because of their
simplicity, their only purpose being to store values to pass onto other classes. FixedWealthCalculator and
VariableWealthCalculator, on the other hand, are created as classes because they contain functions and objects
that carry the bulk of the calculations done by the overall program.

*** 5. Separating calculation from output: Why do the calculator methods return CalculationResult objects instead of printing the answers directly? What design benefit does this provide? ***

Returning objects instead of printing answers directly to the console allows for greater flexibility in terms
of how info is displayed. For instance, if you wanted a GUI instead of a command line and you have the results
hardcoded to print to a console, you would need to rewrite large parts of the code to display the results in
a GUI instead of a console. Thus, if you store the results in objects, you can either print the results or
display them in a GUI without needing to rewrite code for either one to work.