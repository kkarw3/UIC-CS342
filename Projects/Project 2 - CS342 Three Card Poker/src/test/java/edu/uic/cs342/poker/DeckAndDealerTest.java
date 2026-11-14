package edu.uic.cs342.poker;

import edu.uic.cs342.poker.model.Card;
import edu.uic.cs342.poker.model.Dealer;
import edu.uic.cs342.poker.model.Deck;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DeckAndDealerTest
{
    // ----- Deck Tests (1 - 6) -----

    @Test
    void testDeckInitialSize()
    {
        Deck deck = new Deck();
        assertEquals(52, deck.size());
    }

    @Test
    void testDeckContains52UniqueCards()
    {
        Deck deck = new Deck();
        Set<String> uniqueCards = new HashSet<>();

        for (Card c : deck)
        {
            uniqueCards.add(c.toString());
        }

        assertEquals(52, uniqueCards.size());
    }

    @Test
    void testDeckSuitAndValueBounds()
    {
        Deck deck = new Deck();

        for (Card c : deck)
        {
            assertTrue(c.getSuit() == 'C' || c.getSuit() == 'D' || c.getSuit() == 'S' || c.getSuit() == 'H');
            assertTrue(c.getValue() >= 2 && c.getValue() <= 14);
        }
    }

    @Test
    void testDeckCardCountsPerSuit()
    {
        Deck deck = new Deck();
        long clubs = deck.stream().filter(c -> c.getSuit() == 'C').count();
        long diamonds = deck.stream().filter(c -> c.getSuit() == 'D').count();
        long spades = deck.stream().filter(c -> c.getSuit() == 'S').count();
        long hearts = deck.stream().filter(c -> c.getSuit() == 'H').count();

        assertEquals(13, clubs);
        assertEquals(13, diamonds);
        assertEquals(13, spades);
        assertEquals(13, hearts);
    }

    @Test
    void testNewDeckResetsSizeAfterRemoval()
    {
        Deck deck = new Deck();
        deck.remove(0);
        deck.remove(0);
        assertEquals(50, deck.size());

        deck.newDeck();
        assertEquals(52, deck.size());
    }

    @Test
    void testShuffleDeckChangesOrder()
    {
        Deck deck = new Deck();
        ArrayList<String> before = new ArrayList<>();

        for (Card c : deck)
        {
            before.add(c.toString());
        }

        deck.shuffleDeck();
        ArrayList<String> after = new ArrayList<>();

        for (Card c : deck)
        {
            after.add(c.toString());
        }

        assertEquals(52, after.size());
        assertNotEquals(before, after);
    }
    
    // ----- Dealer Tests (7 - 12) -----

    @Test
    void testDealerInitializesWithFullDeck()
    {
        Dealer dealer = new Dealer();
        assertNotNull(dealer.getDeck());
        assertEquals(52, dealer.getDeck().size());
    }

    @Test
    void testDealHandReturnsThreeCards()
    {
        Dealer dealer = new Dealer();
        ArrayList<Card> hand = dealer.dealHand();
        assertNotNull(hand);
        assertEquals(3, hand.size());
    }

    @Test
    void testDealHandReducesDeckByThree()
    {
        Dealer dealer = new Dealer();
        dealer.dealHand();
        assertEquals(49, dealer.getDeck().size());
    }

    @Test
    void testMultipleHandsHaveDistinctCards()
    {
        Dealer dealer = new Dealer();
        ArrayList<Card> p1Hand = dealer.dealHand();
        ArrayList<Card> p2Hand = dealer.dealHand();
        ArrayList<Card> dealerHand = dealer.dealHand();

        Set<String> allDealt = new HashSet<>();
        p1Hand.forEach(c -> allDealt.add(c.toString()));
        p2Hand.forEach(c -> allDealt.add(c.toString()));
        dealerHand.forEach(c -> allDealt.add(c.toString()));

        assertEquals(9, allDealt.size());
        assertEquals(43, dealer.getDeck().size());
    }

    @Test
    void testDealerReshufflesAt34CardsOrLess()
    {
        Dealer dealer = new Dealer();

        // Deal 6 hands (18 cards removed, should be 34 cards remaining)
        for (int i = 0; i < 6; i++)
        {
            dealer.dealHand();
        }
        assertEquals(34, dealer.getDeck().size());

        // Next deal should trigger newDeck() (52 cards) and remove 3 -> 49 remaining
        ArrayList<Card> nextHand = dealer.dealHand();
        assertEquals(3, nextHand.size());
        assertEquals(49, dealer.getDeck().size());
    }

    @Test
    void testSetAndgetDealerHand()
    {
        Dealer dealer = new Dealer();
        ArrayList<Card> hand = dealer.dealHand();
        dealer.setDealersHand(hand);
        assertEquals(hand, dealer.getDealerHand());
        assertEquals(3, dealer.getDealerHand().size());
    }
}
