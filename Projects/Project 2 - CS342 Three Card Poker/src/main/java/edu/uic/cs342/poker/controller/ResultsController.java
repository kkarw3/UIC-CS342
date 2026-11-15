package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.SceneNavigator;
import edu.uic.cs342.poker.model.Card;
import edu.uic.cs342.poker.model.Player;
import edu.uic.cs342.poker.model.ThreeCardLogic;
import edu.uic.cs342.poker.service.GameService;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.ArrayList;

public class ResultsController
{
    @FXML private Label resultsTitleLabel;
    @FXML private HBox dealerCardsBox;
    @FXML private Label dealerClassificationLabel;
    @FXML private Label dealerQualifyLabel;

    @FXML private VBox p1SummaryPanel;
    @FXML private HBox p1CardsBox;
    @FXML private Label p1ClassificationLabel;
    @FXML private Label p1WagerBreakdownLabel;
    @FXML private Label p1OutcomeLabel;
    @FXML private Label p1NetChangeLabel;
    @FXML private Label p1CumulativeLabel;

    @FXML private VBox p2SummaryPanel;
    @FXML private HBox p2CardsBox;
    @FXML private Label p2ClassificationLabel;
    @FXML private Label p2WagerBreakdownLabel;
    @FXML private Label p2OutcomeLabel;
    @FXML private Label p2NetChangeLabel;
    @FXML private Label p2CumulativeLabel;

    @FXML private Button nextRoundButton;
    @FXML private Button mainMenuButton;

    private final GameService gameService = GameService.getInstance();

    @FXML
    public void initialize()
    {
        resultsTitleLabel.setText("Round " + gameService.getRoundNumber() + " — Results Summary");

        // Info for dealer
        ArrayList<Card> dealerHand = gameService.getDealer().getDealerHand();
        populateCards(dealerCardsBox, dealerHand);
        dealerClassificationLabel.setText("Hand Classification: " + GameService.getHandName(ThreeCardLogic.evaluateHand(dealerHand)));
        dealerQualifyLabel.setText(gameService.getDealerStatusSummary());

        // Info for Player 1
        Player p1 = gameService.getPlayer1();
        populatePlayerSummary(
                p1, p1SummaryPanel, p1CardsBox, p1ClassificationLabel,
                p1WagerBreakdownLabel, p1OutcomeLabel, p1NetChangeLabel, p1CumulativeLabel,
                gameService.getP1RoundNet(), gameService.getP1ResultSummary(), gameService.isP1Folded()
        );

        // Info for Player 2
        Player p2 = gameService.getPlayer2();
        populatePlayerSummary(
                p2, p2SummaryPanel, p2CardsBox, p2ClassificationLabel,
                p2WagerBreakdownLabel, p2OutcomeLabel, p2NetChangeLabel, p2CumulativeLabel,
                gameService.getP2RoundNet(), gameService.getP2ResultSummary(), gameService.isP2Folded()
        );
    }

    private void populatePlayerSummary(
            Player player, VBox panel, HBox cardsBox, Label classLabel,
            Label wagerLabel, Label outcomeLabel, Label netLabel, Label cumulativeLabel,
            int roundNet, String summaryText, boolean folded)
    {
        populateCards(cardsBox, player.getHand());

        String handRankName = GameService.getHandName(ThreeCardLogic.evaluateHand(player.getHand()));
        classLabel.setText(folded ? "Hand Classification: " + handRankName + " (Folded)" : "Hand Classification: " + handRankName);

        wagerLabel.setText(String.format("Wagers — Ante: $%d | Pair Plus: $%d | Play: $%d", player.getAnteBet(), player.getPairPlusBet(), player.getPlayBet()));

        outcomeLabel.setText(summaryText);

        String formattedNet = (roundNet > 0) ? "+$" + roundNet : (roundNet < 0 ? "-$" + Math.abs(roundNet) : "$0");
        String statusBadge = (roundNet > 0) ? " [WIN]" : (roundNet < 0 ? " [LOSS]" : " [PUSH / CARRY]");
        netLabel.setText("Round Net Payout: " + formattedNet + statusBadge);

        cumulativeLabel.setText("Updated Cumulative Winnings: $" + player.getTotalWinnings());

        if (roundNet > 0)
        {
            panel.getStyleClass().add("win-highlight");
            ScaleTransition pulse = new ScaleTransition(Duration.millis(320), panel);
            pulse.setFromX(0.96);
            pulse.setFromY(0.96);
            pulse.setToX(1.0);
            pulse.setToY(1.0);
            pulse.setCycleCount(2);
            pulse.setAutoReverse(true);
            pulse.play();
        }
    }

    private void populateCards(HBox container, ArrayList<Card> cards)
    {
        container.getChildren().clear();

        for (Card c : cards)
        {
            Label cardLabel = new Label(GameplayController.formatCardDisplay(c));
            cardLabel.getStyleClass().add("card-face");

            if (c.getSuit() == 'H' || c.getSuit() == 'D')
            {
                cardLabel.getStyleClass().add("card-red");
            }
            else
            {
                cardLabel.getStyleClass().add("card-black");
            }

            container.getChildren().add(cardLabel);
        }
    }

    @FXML
    private void handleNextRound()
    {
        gameService.startNextRound();
        SceneNavigator.switchScreen(SceneNavigator.GAMEPLAY_FXML);
    }

    @FXML
    private void handleReturnToMenu()
    {
        SceneNavigator.switchScreen(SceneNavigator.WELCOME_FXML);
    }

    @FXML
    private void handleToggleTheme()
    {
        SceneNavigator.toggleTheme(nextRoundButton.getScene());
    }
}
