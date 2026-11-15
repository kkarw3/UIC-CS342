package edu.uic.cs342.poker;

import edu.uic.cs342.poker.model.Card;
import edu.uic.cs342.poker.model.ThreeCardLogic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ThreeCardLogicTest
{
    private ArrayList<Card> makeHand(Card c1, Card c2, Card c3)
    {
        return new ArrayList<>(Arrays.asList(c1, c2, c3));
    }

    // ----- evaluateHand Tests (1 - 8) -----

    @Test
    void testEvalStraightFlush()
    {
        ArrayList<Card> hand = makeHand(new Card('H', 10), new Card('H', 11), new Card('H', 12));
        assertEquals(1, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalAceLowStraightFlush()
    {
        ArrayList<Card> hand = makeHand(new Card('S', 14), new Card('S', 2), new Card('S', 3));
        assertEquals(1, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalThreeOfAKind()
    {
        ArrayList<Card> hand = makeHand(new Card('C', 7), new Card('D', 7), new Card('H', 7));
        assertEquals(2, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalStraightStandard()
    {
        ArrayList<Card> hand = makeHand(new Card('C', 5), new Card('D', 7), new Card('S', 6));
        assertEquals(3, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalStraightAceLow()
    {
        ArrayList<Card> hand = makeHand(new Card('H', 3), new Card('D', 14), new Card('C', 2));
        assertEquals(3, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalFlush()
    {
        ArrayList<Card> hand = makeHand(new Card('D', 2), new Card('D', 9), new Card('D', 13));
        assertEquals(4, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalPair()
    {
        ArrayList<Card> hand = makeHand(new Card('S', 11), new Card('H', 4), new Card('C', 11));
        assertEquals(5, ThreeCardLogic.evaluateHand(hand));
    }

    @Test
    void testEvalHighCard()
    {
        ArrayList<Card> hand = makeHand(new Card('C', 2), new Card('D', 8), new Card('H', 14));
        assertEquals(0, ThreeCardLogic.evaluateHand(hand));
    }

    // ----- evaluatePairPlusWinnings Tests (9 - 14) -----

    @Test
    void testEvalWinningsStraightFlush()
    {
        ArrayList<Card> hand = makeHand(new Card('C', 9), new Card('C', 10), new Card('C', 11));
        assertEquals(400, ThreeCardLogic.evaluatePairPlusWinnings(hand, 10));
    }

    @Test
    void testEvalWinningsThreeOfAKind()
    {
        ArrayList<Card> hand = makeHand(new Card('H', 14), new Card('S', 14), new Card('D', 14));
        assertEquals(300, ThreeCardLogic.evaluatePairPlusWinnings(hand, 10));
    }

    @Test
    void testEvalWinningsStraight()
    {
        ArrayList<Card> hand = makeHand(new Card('H', 8), new Card('S', 9), new Card('D', 10));
        assertEquals(60, ThreeCardLogic.evaluatePairPlusWinnings(hand, 10));
    }

    @Test
    void testEvalWinningsFlush()
    {
        ArrayList<Card> hand = makeHand(new Card('S', 4), new Card('S', 7), new Card('S', 12));
        assertEquals(30, ThreeCardLogic.evaluatePairPlusWinnings(hand, 10));
    }

    @Test
    void testEvalWinningsPair()
    {
        ArrayList<Card> hand = makeHand(new Card('C', 5), new Card('D', 5), new Card('H', 9));
        assertEquals(10, ThreeCardLogic.evaluatePairPlusWinnings(hand, 10));
    }

    @Test
    void testEvalWinningsHighCardAndZeroBet()
    {
        ArrayList<Card> highCard = makeHand(new Card('C', 3), new Card('D', 7), new Card('H', 13));
        assertEquals(0, ThreeCardLogic.evaluatePairPlusWinnings(highCard, 15));

        ArrayList<Card> pair = makeHand(new Card('C', 5), new Card('D', 5), new Card('H', 9));
        assertEquals(0, ThreeCardLogic.evaluatePairPlusWinnings(pair, 0));
    }

    // ----- compareHands Tests (15 - 22) -----

    @Test
    void testCompareDifferentRanksDealerWins()
    {
        ArrayList<Card> dealer = makeHand(new Card('H', 4), new Card('H', 8), new Card('H', 12)); // Flush
        ArrayList<Card> player = makeHand(new Card('C', 10), new Card('D', 10), new Card('S', 14)); // Pair
        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testCompareDifferentRanksPlayerWins()
    {
        ArrayList<Card> dealer = makeHand(new Card('C', 4), new Card('D', 5), new Card('S', 6)); // Straight
        ArrayList<Card> player = makeHand(new Card('H', 2), new Card('D', 2), new Card('S', 2)); // Three of a Kind
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testCompareHighCardTieBreaker()
    {
        ArrayList<Card> dealer = makeHand(new Card('C', 14), new Card('D', 10), new Card('S', 4)); // A-10-4
        ArrayList<Card> player = makeHand(new Card('H', 14), new Card('S', 10), new Card('C', 5)); // A-10-5
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testComparePairDifferentPairValues()
    {
        ArrayList<Card> dealer = makeHand(new Card('C', 9), new Card('D', 9), new Card('S', 2)); // Pair of 9s
        ArrayList<Card> player = makeHand(new Card('H', 8), new Card('S', 8), new Card('C', 14)); // Pair of 8s
        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testComparePairSamePairValueDifferentKicker()
    {
        ArrayList<Card> dealer = makeHand(new Card('C', 10), new Card('D', 10), new Card('S', 6)); // Pair of 10s, 6 kicker
        ArrayList<Card> player = makeHand(new Card('H', 10), new Card('S', 10), new Card('C', 12)); // Pair of 10s, Q kicker
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testCompareFlushTieBreaker()
    {
        ArrayList<Card> dealer = makeHand(new Card('S', 13), new Card('S', 9), new Card('S', 4)); // K-9-4 Flush
        ArrayList<Card> player = makeHand(new Card('H', 13), new Card('H', 8), new Card('H', 7)); // K-8-7 Flush
        assertEquals(1, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testCompareStraightAceLowVsFourHigh()
    {
        ArrayList<Card> dealer = makeHand(new Card('C', 14), new Card('D', 2), new Card('S', 3)); // A-2-3 (3-high)
        ArrayList<Card> player = makeHand(new Card('H', 2), new Card('S', 3), new Card('C', 4)); // 2-3-4 (4-high)
        assertEquals(2, ThreeCardLogic.compareHands(dealer, player));
    }

    @Test
    void testCompareExactTie()
    {
        ArrayList<Card> dealer = makeHand(new Card('C', 12), new Card('D', 9), new Card('S', 3));
        ArrayList<Card> player = makeHand(new Card('H', 12), new Card('S', 9), new Card('C', 3));
        assertEquals(0, ThreeCardLogic.compareHands(dealer, player));
    }
}