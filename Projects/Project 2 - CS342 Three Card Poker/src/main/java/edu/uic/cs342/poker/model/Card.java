package edu.uic.cs342.poker.model;

public class Card
{
    private char suit;
    private int value;

    // Card creator
    public Card(char suit, int value)
    {
        this.suit = suit;
        this.value = value;
    }

    // Helper methods
    public void setSuit(char suit) { this.suit = suit; }
    public void setValue(int value) { this.value = value; }
    public char getSuit() { return suit; }
    public int getValue() { return value; }

    // Handler for Face cards & Aces
    @Override
    public String toString()
    {
        String valueString = switch (value)
        {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(value);
        };
        return valueString + suit;
    }
}