package edu.uic.cs342.poker.model;

import java.util.ArrayList;
import java.util.Collections;

public class Deck extends ArrayList<Card>
{
    public Deck() { newDeck(); }

    // Deck maker
    public void newDeck()
    {
        this.clear();
        char[] suits = {'C', 'D', 'H', 'S'};

        for (char suit : suits)
        {
            for (int value = 2; value <= 14; value++)
            {
                this.add(new Card(suit, value));
            }
        }
        shuffleDeck();
    }

    public void shuffleDeck() { Collections.shuffle(this); }
}
