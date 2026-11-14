package edu.uic.cs342.poker.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ThreeCardLogic
{
    public static int evaluateHand(ArrayList<Card> hand)
    {
        if (hand == null || hand.size() != 3)
        {
            return 0;
        }

        ArrayList<Card> sortedHand = new ArrayList<>(hand);
        sortedHand.sort(Comparator.comparingInt(Card::getValue));

        int value1 = sortedHand.get(0).getValue();
        int value2 = sortedHand.get(1).getValue();
        int value3 = sortedHand.get(2).getValue();

        char suit1 = sortedHand.get(0).getSuit();
        char suit2 = sortedHand.get(1).getSuit();
        char suit3 = sortedHand.get(2).getSuit();

        boolean isFlush = ((suit1 == suit2) && (suit2 == suit3));

        // Note: The 2nd half of the OR statement is specifically for Ace-low straights
        boolean isStraight = (value1 + 1 == value2 && value2 + 1 == value3) || (value1 == 2 && value2 == 3 && value3 == 14);

        // Returns a value from 0-5 depending on the player's hand
        if (isStraight && isFlush)
        {
            return 1; // Straight Flush
        }
        if (value1 == value2 && value2 == value3)
        {
            return 2; // Three of a Kind
        }
        if (isStraight)
        {
            return 3; // Straight
        }
        if (isFlush)
        {
            return 4; // Flush
        }
        if (value1 == value2 || value2 == value3 || value1 == value3)
        {
            return 5; // Pair
        }

        return 0; // High Card
    }

    public static int evaluatePlayerWinnings(ArrayList<Card> hand, int bet)
    {
        if (bet <= 0) { return 0; } // Invalid bet

        int handRank = evaluateHand(hand);
        return switch (handRank)
        {
            case 1 -> bet * 40; // Straight Flush - 40:1
            case 2 -> bet * 30; // Three of a Kind - 30:1
            case 3 -> bet * 6;  // Straight - 6:1
            case 4 -> bet * 3;  // Flush - 3:1
            case 5 -> bet;      // Pair - 1:1
            default -> 0;       // High Card - Pair Plus
        };
    }

    public static int compareHands(ArrayList<Card> dealerHand, ArrayList<Card> playerHand)
    {
        int dealerRank = evaluateHand(dealerHand);
        int playerRank = evaluateHand(playerHand);

        // Convert rank to a score to make comparing easier
        int dealerScore = getHandScore(dealerRank);
        int playerScore = getHandScore(playerRank);

        if (dealerScore > playerScore)
        {
            return 1; // Dealer win
        }
        else if (playerScore > dealerScore)
        {
            return 2; // Player win
        }

        // The below code runs only if there is a tie
        ArrayList<Card> dealerSorted = new ArrayList<>(dealerHand);
        ArrayList<Card> playerSorted = new ArrayList<>(playerHand);
        dealerSorted.sort(Comparator.comparingInt(Card::getValue));
        playerSorted.sort(Comparator.comparingInt(Card::getValue));

        // Special logic for tie-breaking Pairs
        if (dealerRank == 5)
        {
            int dealerPairVal = (dealerSorted.get(0).getValue() == dealerSorted.get(1).getValue()) ? dealerSorted.get(0).getValue() : dealerSorted.get(2).getValue();
            int dealerKicker = (dealerSorted.get(0).getValue() == dealerSorted.get(1).getValue()) ? dealerSorted.get(2).getValue() : dealerSorted.get(0).getValue();
            
            int pPairVal = (playerSorted.get(0).getValue() == playerSorted.get(1).getValue()) ? playerSorted.get(0).getValue() : playerSorted.get(2).getValue();
            int playerKicker = (playerSorted.get(0).getValue() == playerSorted.get(1).getValue()) ? playerSorted.get(2).getValue() : playerSorted.get(0).getValue();

            if (dealerPairVal != pPairVal)
            {
                return (dealerPairVal > pPairVal) ? 1 : 2;
            }
            if (dealerKicker != playerKicker)
            {
                return (dealerKicker > playerKicker) ? 1 : 2;
            }

            return 0;
        }

        // Special logic for tie-breaking Straights / Straight Flushes
        if (dealerRank == 1 || dealerRank == 3)
        {
            // Gets the straight's high card value
            int dealerHigh = getStraightHighCard(dealerSorted);
            int playerHigh = getStraightHighCard(playerSorted);

            if (dealerHigh != playerHigh)
            {
                return (dealerHigh > playerHigh) ? 1 : 2;
            }
            return 0;
        }

        // General logic for other hands (Three of a Kind, Flush, High Card)
        for (int i = 2; i >= 0; i--)
        {
            int dealerValue = dealerSorted.get(i).getValue();
            int playerValue = playerSorted.get(i).getValue();

            if (dealerValue != playerValue)
            {
                return (dealerValue > playerValue) ? 1 : 2;
            }
        }

        return 0; // Somehow, there's an exact tie
    }

    private static int getHandScore(int evaluateHandRank)
    {
        return switch (evaluateHandRank)
        {
            case 1 -> 6; // Straight Flush
            case 2 -> 5; // Three of a Kind
            case 3 -> 4; // Straight
            case 4 -> 3; // Flush
            case 5 -> 2; // Pair
            default -> 1; // High Card (0)
        };
    }

    private static int getStraightHighCard(ArrayList<Card> sortedHand)
    {
        // Edge case for Ace-low Straights: 3 is the high card, not the Ace
        if (sortedHand.get(0).getValue() == 2 && sortedHand.get(2).getValue() == 14)
        {
            return 3;
        }

        return sortedHand.get(2).getValue();
    }
}