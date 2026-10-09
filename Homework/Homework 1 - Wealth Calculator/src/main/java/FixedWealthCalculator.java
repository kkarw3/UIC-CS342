/**
 * STUDENT IMPLEMENTATION.
 *
 * Performs calculations that use a fixed interest rate and,
 * when applicable, a fixed annual savings contribution.
 *
 * Do not print from this class.
 */
public class FixedWealthCalculator
{
    public CalculationResult futureValue(LumpSumInput input)
    {
        // TODO: implement
        double presentVal = input.amount();
        double rate = input.rate();
        int years = input.years();

        double result = presentVal * Math.pow(1 + rate, years);

        return new CalculationResult("Future Value", result);
    }

    public CalculationResult compoundSavings(SavingsInput input)
    {
        // Creates the required variables for the
        double balance = input.startingAmount();
        double rate = input.rate();
        double contribution = input.contribution();
        int years = input.years();

        for (int i = 0; i < years; i++)
        {
            balance = balance * (1 + rate);
            balance += contribution;
        }

        return new CalculationResult("Compound Savings", balance);
    }

    public CalculationResult presentValue(PresentValueInput input)
    {
        // TODO: implement
        double futureVal = input.futureValue();
        double rate = input.rate();
        int years = input.years();

        double presentVal = futureVal / Math.pow(1 + rate, years);

        return new CalculationResult("Present Value", presentVal);
    }
}
