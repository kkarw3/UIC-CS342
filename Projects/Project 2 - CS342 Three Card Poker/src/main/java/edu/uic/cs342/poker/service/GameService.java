package edu.uic.cs342.poker.service;

import edu.uic.cs342.poker.model.Card;
import edu.uic.cs342.poker.model.Dealer;
import edu.uic.cs342.poker.model.Player;
import edu.uic.cs342.poker.model.ThreeCardLogic;

import java.util.ArrayList;

public class GameService
{
    private static GameService instance;

    private final Dealer dealer;
    private final Player player1;
    private final Player player2;

    private GamePhase currentPhase;
    private int roundNumber;

    // Track player round states
    private boolean p1Folded;
    private boolean p2Folded;
    private boolean p1Decided;
    private boolean p2Decided;
    private boolean p1AnteCarried;
    private boolean p2AnteCarried;

    // Track individual round summaries for the results screen
    private int p1RoundNet;
    private int p2RoundNet;
    private String p1ResultSummary;
    private String p2ResultSummary;
    private String dealerStatusSummary;

    private GameService()
    {
        dealer = new Dealer();
        player1 = new Player();
        player2 = new Player();
        resetGame();
    }

    public static GameService getInstance()
    {
        if (instance == null)
        {
            instance = new GameService();
        }

        return instance;
    }

    // Resets the game completely
    public void resetGame()
    {
        dealer.getDeck().newDeck();
        dealer.setDealersHand(new ArrayList<>());

        resetPlayer(player1);
        resetPlayer(player2);

        p1AnteCarried = false;
        p2AnteCarried = false;
        roundNumber = 1;
        prepareNextRoundState();
    }

    // Resets a player completely
    private void resetPlayer(Player p)
    {
        p.setHand(new ArrayList<>());
        p.setAnteBet(0);
        p.setPairPlusBet(0);
        p.setPlayBet(0);
        p.setTotalWinnings(0);
    }

    public void startNextRound()
    {
        if (currentPhase != GamePhase.ROUND_OVER)
        {
            throw new IllegalStateException("Cannot start a new round before the current round is resolved.");
        }
        roundNumber++;
        prepareNextRoundState();
    }

    public void prepareNextRoundState()
    {
        dealer.setDealersHand(new ArrayList<>());
        player1.setHand(new ArrayList<>());
        player2.setHand(new ArrayList<>());

        // Preserves carried-over Ante if the dealer failed to qualify in the previous round
        if (!p1AnteCarried)
        {
            player1.setAnteBet(0);
        }
        if (!p2AnteCarried)
        {
            player2.setAnteBet(0);
        }

        player1.setPairPlusBet(0);
        player1.setPlayBet(0);
        player2.setPairPlusBet(0);
        player2.setPlayBet(0);

        p1Folded = false;
        p2Folded = false;
        p1Decided = false;
        p2Decided = false;

        p1RoundNet = 0;
        p2RoundNet = 0;
        p1ResultSummary = "";
        p2ResultSummary = "";
        dealerStatusSummary = "";

        currentPhase = GamePhase.BETTING;
    }

    /**
     * Validates and places wagers for a given player
     * Ante must be between $5 - $25
     * Pair Plus must be either $0 or between $5 - $25.
     */
    public void placeBets(int playerNum, int ante, int pairPlus)
    {
        if (currentPhase != GamePhase.BETTING)
        {
            throw new IllegalStateException("Bets can only be placed during the BETTING phase.");
        }

        Player player = getPlayer(playerNum);
        boolean isCarried = (playerNum == 1) ? p1AnteCarried : p2AnteCarried;

        if (!isCarried && (ante < 5 || ante > 25))
        {
            throw new IllegalArgumentException("Ante wager must be between $5 and $25.");
        }

        if (pairPlus != 0 && (pairPlus < 5 || pairPlus > 25))
        {
            throw new IllegalArgumentException("Pair Plus wager must be $0 or between $5 and $25.");
        }

        if (!isCarried)
        {
            player.setAnteBet(ante);
        }

        player.setPairPlusBet(pairPlus);
    }

    // Deals 3 cards to both players and the dealer once bets are placed
    public void dealCards()
    {
        if (currentPhase != GamePhase.BETTING)
        {
            throw new IllegalStateException("Cards can only be dealt from the BETTING phase.");
        }
        if (player1.getAnteBet() < 5 || player2.getAnteBet() < 5)
        {
            throw new IllegalStateException("Both players must have a valid Ante wager ($5-$25) before dealing.");
        }

        player1.setHand(dealer.dealHand());
        player2.setHand(dealer.dealHand());
        dealer.setDealersHand(dealer.dealHand());

        currentPhase = GamePhase.PLAYER_DECISION;
    }

    // Records a player's decision to play or fold
    // The round is resolved once both players have made a decision
    public void makePlayerDecision(int playerNum, boolean play)
    {
        if (currentPhase != GamePhase.PLAYER_DECISION)
        {
            throw new IllegalStateException("Player decisions are only allowed during the PLAYER_DECISION phase.");
        }

        Player player = getPlayer(playerNum);
        if (playerNum == 1)
        {
            if (p1Decided)
            {
                throw new IllegalStateException("Player 1 has already made a decision.");
            }

            p1Decided = true;
            p1Folded = !play;
        }
        else
        {
            if (p2Decided)
            {
                throw new IllegalStateException("Player 2 has already made a decision.");
            }

            p2Decided = true;
            p2Folded = !play;
        }

        if (play)
        {
            player.setPlayBet(player.getAnteBet());
        }
        else
        {
            player.setPlayBet(0);
        }

        if (p1Decided && p2Decided)
        {
            resolveRound();
        }
    }

    // Resolves the current round
    private void resolveRound()
    {
        boolean dealerQualifies = checkDealerQualifies();

        if (dealerQualifies)
        {
            dealerStatusSummary = "Dealer Qualifies (" + getHandName(ThreeCardLogic.evaluateHand(dealer.getDealerHand())) + ")";
        }
        else
        {
            dealerStatusSummary = "Dealer Does Not Qualify (Less than Queen-High)";
        }

        // Change Player 1's total money
        p1RoundNet = evaluatePlayerOutcome(player1, p1Folded, dealerQualifies, 1);
        player1.setTotalWinnings(player1.getTotalWinnings() + p1RoundNet);

       // Change Player 2's total money
        p2RoundNet = evaluatePlayerOutcome(player2, p2Folded, dealerQualifies, 2);
        player2.setTotalWinnings(player2.getTotalWinnings() + p2RoundNet);

        currentPhase = GamePhase.ROUND_OVER;
    }

    // Evaluates the player's outcome for the round
    private int evaluatePlayerOutcome(Player player, boolean folded, boolean dealerQualifies, int playerNum)
    {
        int ante = player.getAnteBet();
        int play = player.getPlayBet();
        int pp = player.getPairPlusBet();
        int net = 0;

        // A folded hand forfeits Ante and Pair Plus
        if (folded)
        {
            net -= (ante + pp);
            setPlayerCarriedFlag(playerNum, false);
            setPlayerSummary(playerNum, "Folded — Forfeited $" + (ante + pp) + " in wagers.");
            return net;
        }

        // Evaluate Pair Plus independently of the dealer's hand
        String ppNote = "";
        if (pp > 0)
        {
            int ppWin = ThreeCardLogic.evaluatePairPlusWinnings(player.getHand(), pp);
            if (ppWin > 0)
            {
                net += ppWin;
                ppNote = " | Pair Plus Won: +$" + ppWin;
            }
            else
            {
                net -= pp;
                ppNote = " | Pair Plus Lost: -$" + pp;
            }
        }

        if (!dealerQualifies)
        {
            setPlayerCarriedFlag(playerNum, true);
            setPlayerSummary(playerNum, "Dealer Not Qualified — Play ($" + play + ") returned, Ante ($" + ante + ") carries over" + ppNote);
            return net;
        }

        setPlayerCarriedFlag(playerNum, false);
        int outcome = ThreeCardLogic.compareHands(dealer.getDealerHand(), player.getHand());

        // Player beats dealer: Player is paid 1:1 on both Ante and Play
        if (outcome == 2)
        {
            net += (ante + play);
            setPlayerSummary(playerNum, "Beat Dealer! Won +$" + (ante + play) + " on Ante/Play" + ppNote);
        }
        // Dealer beats player: Player loses both Ante and Play
        else if (outcome == 1)
        {
            net -= (ante + play);
            setPlayerSummary(playerNum, "Lost to Dealer (-$" + (ante + play) + ")" + ppNote);
        }
        // Tie: Push Ante and Play
        else
        {
            setPlayerSummary(playerNum, "Tie with Dealer (Ante & Play Pushed)" + ppNote);
        }

        return net;
    }

    // Checks if the dealer qualifies for the current round
    public boolean checkDealerQualifies()
    {
        ArrayList<Card> dealerHand = dealer.getDealerHand();
        if (dealerHand == null || dealerHand.size() != 3)
        {
            return false;
        }

        int rank = ThreeCardLogic.evaluateHand(dealerHand);

        // Any Pair, Flush, Straight, Three of a Kind, or Straight Flush automatically qualifies
        if (rank != 0)
        {
            return true;
        }

        // Dealer only qualifies for High Card if highest card is Queen or better
        for (Card c : dealerHand)
        {
            if (c.getValue() >= 12)
            {
                return true;
            }
        }

        return false;
    }

    public static String getHandName(int evalRank)
    {
        return switch (evalRank)
        {
            case 1 -> "Straight Flush";
            case 2 -> "Three of a Kind";
            case 3 -> "Straight";
            case 4 -> "Flush";
            case 5 -> "Pair";
            default -> "High Card";
        };
    }

    private void setPlayerCarriedFlag(int playerNum, boolean carried)
    {
        if (playerNum == 1)
        {
            p1AnteCarried = carried;
        }
        else
        {
            p2AnteCarried = carried;
        }
    }

    private void setPlayerSummary(int playerNum, String summary)
    {
        if (playerNum == 1)
        {
            p1ResultSummary = summary;
        }
        else
        {
            p2ResultSummary = summary;
        }
    }

    public Player getPlayer(int playerNum)
    {
        if (playerNum == 1)
        {
            return player1;
        }
        if (playerNum == 2)
        {
            return player2;
        }
        throw new IllegalArgumentException("Player number must be 1 or 2.");
    }

    // Helper functions
    public Dealer getDealer() { return dealer; }
    public Player getPlayer1() { return player1; }
    public Player getPlayer2() { return player2; }
    public GamePhase getCurrentPhase() { return currentPhase; }
    public int getRoundNumber() { return roundNumber; }
    public boolean isP1Folded() { return p1Folded; }
    public boolean isP2Folded() { return p2Folded; }
    public boolean isP1Decided() { return p1Decided; }
    public boolean isP2Decided() { return p2Decided; }
    public boolean isP1AnteCarried() { return p1AnteCarried; }
    public boolean isP2AnteCarried() { return p2AnteCarried; }
    public int getP1RoundNet() { return p1RoundNet; }
    public int getP2RoundNet() { return p2RoundNet; }
    public String getP1ResultSummary() { return p1ResultSummary; }
    public String getP2ResultSummary() { return p2ResultSummary; }
    public String getDealerStatusSummary() { return dealerStatusSummary; }
}