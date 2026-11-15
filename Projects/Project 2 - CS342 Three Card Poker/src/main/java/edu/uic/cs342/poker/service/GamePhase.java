package edu.uic.cs342.poker.service;

// Used by controller to know which buttons to enable/disable at what points
public enum GamePhase
{
    BETTING,            // Placing Antes and optional bets
    PLAYER_DECISION,    // Cards are dealt, Player 1 & Player 2 choose Play or Fold
    ROUND_OVER          // Dealer reveals cards, payouts calculated, view results or click 'Play Again'
}
