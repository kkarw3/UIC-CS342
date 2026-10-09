import java.util.List;

/**
 * STUDENT IMPLEMENTATION.
 *
 * Performs calculations using values supplied by the provided
 * RateFileReader and SavingsFileReader dependencies.
 *
 * The dependencies must be received through the constructor.
 * Do not create file readers inside this class.
 * Do not print from this class.
 */
public class VariableWealthCalculator
{
    private final RateFileReader rateReader;
    private final SavingsFileReader savingsReader;

    public VariableWealthCalculator(RateFileReader rateReader, SavingsFileReader savingsReader)
    {
        this.rateReader = rateReader;
        this.savingsReader = savingsReader;
    }

    public CalculationResult futureValue(double startingAmount)
    {
        // TODO: use rateReader.readRates() and implement the calculation

        List<Double> rates = rateReader.readRates();
        double currAmount = startingAmount;

        for (double rate : rates)
        {
            currAmount = currAmount * (1.0 + rate);
        }

        return new CalculationResult("Variable Future Value", currAmount);
    }

    public CalculationResult compoundSavings(double startingAmount)
    {
        // TODO: use both provided readers and implement the calculation
        // Savings contributions occur at the end of each year.
        List<Double> rates = rateReader.readRates();
        List<Double> savings = savingsReader.readSavings();

        double currAmount = startingAmount;
        int years = Math.min(rates.size(), savings.size());

        for (int i = 0; i < years; i++)
        {
            currAmount = currAmount * (1.0 + rates.get(i)) + savings.get(i);
        }

        return new CalculationResult("Variable Compound Savings", currAmount);
    }
}
