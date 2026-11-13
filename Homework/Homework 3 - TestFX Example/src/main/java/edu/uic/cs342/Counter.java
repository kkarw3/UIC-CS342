package edu.uic.cs342;
/** UI-independent model, suitable for fast JUnit tests. */
public class Counter
{
    private int value;

    public int getValue()
    {
        return value;
    }
    public int increment()
    {
        return ++value;
    }
    public int decrement()
    {
        if (value <= 0)
        {
            throw new IllegalStateException("Count cannot be negative.");
        }
        return --value;
    }
    public void reset()
    {
        value = 0;
    }
}