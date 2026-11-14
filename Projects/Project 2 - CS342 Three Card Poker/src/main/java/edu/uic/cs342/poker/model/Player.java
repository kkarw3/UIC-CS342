package edu.uic.cs342.poker.model;

import java.util.ArrayList;

public class Player
{
    private ArrayList<Card> hand;
    private int anteBet;
    private int playBet;
    private int pairPlusBet;
    private int totalWinnings;

    public Player()
    {
        this.hand = new ArrayList<>();
        this.anteBet = 0;
        this.playBet = 0;
        this.pairPlusBet = 0;
        this.totalWinnings = 0;
    }

    // Helper methods
    public void setHand(ArrayList<Card> hand) { this.hand = hand; }
    public void setAnteBet(int anteBet) { this.anteBet = anteBet; }
    public void setPlayBet(int playBet) { this.playBet = playBet; }
    public void setPairPlusBet(int pairPlusBet) { this.pairPlusBet = pairPlusBet; }
    public void setTotalWinnings(int totalWinnings) { this.totalWinnings = totalWinnings; }
    public ArrayList<Card> getHand() { return hand; }
    public int getAnteBet() { return anteBet; }
    public int getPlayBet() { return playBet; }
    public int getPairPlusBet() { return pairPlusBet; }
    public int getTotalWinnings() { return totalWinnings; }
}
