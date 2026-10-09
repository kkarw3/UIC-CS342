/*
 * STUDENT IMPLEMENTATION / COMPOSITION ROOT.
 *
 * Java 25 compact source file:
 * no explicit class declaration and no "public static void main(String[] args)".
 *
 * Create the major objects here, inject dependencies, perform the five
 * calculations, and pass each CalculationResult to ConsoleReporter.
 */
void main()
{
    var rateReader = new RateFileReader("rates.txt");
    var savingsReader = new SavingsFileReader("savings.txt");

    var fixedCalculator = new FixedWealthCalculator();

    var variableCalculator = new VariableWealthCalculator(rateReader, savingsReader);

    var reporter = new ConsoleReporter();

    // TODO: create input records, call all five calculations,
    // and report their results.

    // Test for fixed future value
    var lumpSumAmount = new LumpSumInput(20000, 0.02, 5);
    var resultOne = fixedCalculator.futureValue(lumpSumAmount);
    reporter.report(resultOne);

    // Test for fixed compound saving
    var fixedCompSavings = new SavingsInput(30000, 2500, 0.04, 10);
    var resultTwo = fixedCalculator.compoundSavings(fixedCompSavings);
    reporter.report(resultTwo);

    // Test for variable future value
    var resultThree = variableCalculator.futureValue(50000);
    reporter.report(resultThree);

    // Test for present value
    var presentVal = new PresentValueInput(23000, 0.05, 20);
    var resultFour = fixedCalculator.presentValue(presentVal);
    reporter.report(resultFour);

    // Test for variable compound savings
    var resultFive = variableCalculator.compoundSavings(60000);
    reporter.report(resultFive);
}
