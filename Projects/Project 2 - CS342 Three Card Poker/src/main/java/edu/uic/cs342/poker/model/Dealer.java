package edu.uic.cs342.poker.model;

import java.util.ArrayList;

public class Dealer
{
    private Deck deck;
    private ArrayList<Card> dealerHand;

    public Dealer()
    {
        deck = new Deck();
        dealerHand = new ArrayList<>();
    }

    public ArrayList<Card> dealHand()
    {
        if (deck.size() <= 34)
        {
            deck.newDeck();
        }

        ArrayList<Card> hand = new ArrayList<>();
        for (int i = 0; i < 3; i++)
        {
            hand.add(deck.remove(0));
        }

        return hand;
    }

    // Helper methods
    public Deck getDeck() { return deck; }
    public ArrayList<Card> getDealerHand() { return dealerHand; }
    public void setDealersHand(ArrayList<Card> dealersHand) { this.dealerHand = dealersHand; }
}
