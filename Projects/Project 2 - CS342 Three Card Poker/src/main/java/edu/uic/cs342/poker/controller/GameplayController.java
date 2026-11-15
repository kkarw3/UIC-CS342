package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.SceneNavigator;
import edu.uic.cs342.poker.model.Card;
import edu.uic.cs342.poker.model.Player;
import edu.uic.cs342.poker.model.ThreeCardLogic;
import edu.uic.cs342.poker.service.GamePhase;
import edu.uic.cs342.poker.service.GameService;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Optional;

public class GameplayController
{
    @FXML private Label roundLabel;
    @FXML private Label phaseLabel;
    @FXML private Label guidanceLabel;
    @FXML private Label errorLabel;

    @FXML private HBox dealerCardsBox;
    @FXML private Label dealerHandStatusLabel;

    @FXML private Label p1WinningsLabel;
    @FXML private HBox p1CardsBox;
    @FXML private Label p1HandRankLabel;
    @FXML private TextField p1AnteField;
    @FXML private TextField p1PairPlusField;
    @FXML private Label p1ActiveWagersLabel;
    @FXML private Button p1PlayButton;
    @FXML private Button p1FoldButton;
    @FXML private Label p1DecisionStatusLabel;

    @FXML private Label p2WinningsLabel;
    @FXML private HBox p2CardsBox;
    @FXML private Label p2HandRankLabel;
    @FXML private TextField p2AnteField;
    @FXML private TextField p2PairPlusField;
    @FXML private Label p2ActiveWagersLabel;
    @FXML private Button p2PlayButton;
    @FXML private Button p2FoldButton;
    @FXML private Label p2DecisionStatusLabel;

    @FXML private Button dealButton;
    @FXML private Button viewResultsButton;

    private final GameService gameService = GameService.getInstance();

    @FXML
    public void initialize()
    {
        hideError();
        refreshUI(false, false);
    }

    @FXML
    private void handleDealCards()
    {
        hideError();
        try
        {
            int p1Ante = gameService.isP1AnteCarried() ? gameService.getPlayer1().getAnteBet() : parseBetInput(p1AnteField.getText(), "Player 1 Ante");
            int p1PairPlus = parseOptionalBetInput(p1PairPlusField.getText(), "Player 1 Pair Plus");

            int p2Ante = gameService.isP2AnteCarried() ? gameService.getPlayer2().getAnteBet() : parseBetInput(p2AnteField.getText(), "Player 2 Ante");
            int p2PairPlus = parseOptionalBetInput(p2PairPlusField.getText(), "Player 2 Pair Plus");

            gameService.placeBets(1, p1Ante, p1PairPlus);
            gameService.placeBets(2, p2Ante, p2PairPlus);
            gameService.dealCards();

            refreshUI(true, false);
        }
        catch (IllegalArgumentException | IllegalStateException ex)
        {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleP1Play()
    {
        makeDecision(1, true);
    }

    @FXML
    private void handleP1Fold()
    {
        makeDecision(1, false);
    }

    @FXML
    private void handleP2Play()
    {
        makeDecision(2, true);
    }

    @FXML
    private void handleP2Fold()
    {
        makeDecision(2, false);
    }

    private void makeDecision(int playerNum, boolean play)
    {
        hideError();
        try
        {
            gameService.makePlayerDecision(playerNum, play);
            boolean justResolved = (gameService.getCurrentPhase() == GamePhase.ROUND_OVER);
            refreshUI(false, justResolved);
        }
        catch (IllegalStateException ex)
        {
            showError(ex.getMessage());
        }
    }

    @FXML
    private void handleViewResults()
    {
        if (gameService.getCurrentPhase() == GamePhase.ROUND_OVER)
        {
            SceneNavigator.switchScreen(SceneNavigator.RESULTS_FXML);
        }
    }

    @FXML
    private void handleToggleTheme()
    {
        SceneNavigator.toggleTheme(dealButton.getScene());
    }

    @FXML
    private void handleBackToMenu()
    {
        if (gameService.getCurrentPhase() == GamePhase.PLAYER_DECISION)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Abandon Active Round?");
            alert.setHeaderText("A round is currently in progress.");
            alert.setContentText("Returning to the Main Menu will reset the current game. Continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK)
            {
                return;
            }
        }

        SceneNavigator.switchScreen(SceneNavigator.WELCOME_FXML);
    }

    private void refreshUI(boolean animateDeal, boolean animateDealerReveal)
    {
        GamePhase phase = gameService.getCurrentPhase();
        Player player1 = gameService.getPlayer1();
        Player player2 = gameService.getPlayer2();

        roundLabel.setText("Round " + gameService.getRoundNumber());
        phaseLabel.setText("Phase: " + phase.name());

        p1WinningsLabel.setText("Total Winnings: $" + player1.getTotalWinnings());
        p2WinningsLabel.setText("Total Winnings: $" + player2.getTotalWinnings());

        p1ActiveWagersLabel.setText(String.format("Active Wagers — Ante: $%d | Pair Plus: $%d | Play: $%d", player1.getAnteBet(), player1.getPairPlusBet(), player1.getPlayBet()));
        p2ActiveWagersLabel.setText(String.format("Active Wagers — Ante: $%d | Pair Plus: $%d | Play: $%d", player2.getAnteBet(), player2.getPairPlusBet(), player2.getPlayBet()));

        if (phase == GamePhase.BETTING)
        {
            if (gameService.isP1AnteCarried())
            {
                p1AnteField.setText(String.valueOf(player1.getAnteBet()));
                p1AnteField.setDisable(true);
            }
            else
            {
                p1AnteField.setDisable(false);
            }
            if (gameService.isP2AnteCarried())
            {
                p2AnteField.setText(String.valueOf(player2.getAnteBet()));
                p2AnteField.setDisable(true);
            }
            else
            {
                p2AnteField.setDisable(false);
            }
            p1PairPlusField.setDisable(false);
            p2PairPlusField.setDisable(false);
        }
        else
        {
            p1AnteField.setDisable(true);
            p1PairPlusField.setDisable(true);
            p2AnteField.setDisable(true);
            p2PairPlusField.setDisable(true);
        }

        if (phase == GamePhase.BETTING)
        {
            renderHiddenCards(dealerCardsBox);
            renderHiddenCards(p1CardsBox);
            renderHiddenCards(p2CardsBox);
            dealerHandStatusLabel.setText("Cards Hidden Until Dealt");
            p1HandRankLabel.setText("Hand: Place Ante to Deal");
            p2HandRankLabel.setText("Hand: Place Ante to Deal");
        }
        else
        {
            renderFaceUpCards(p1CardsBox, player1.getHand());
            renderFaceUpCards(p2CardsBox, player2.getHand());
            p1HandRankLabel.setText("Hand: " + GameService.getHandName(ThreeCardLogic.evaluateHand(player1.getHand())));
            p2HandRankLabel.setText("Hand: " + GameService.getHandName(ThreeCardLogic.evaluateHand(player2.getHand())));

            if (phase == GamePhase.ROUND_OVER)
            {
                renderFaceUpCards(dealerCardsBox, gameService.getDealer().getDealerHand());
                dealerHandStatusLabel.setText(gameService.getDealerStatusSummary());
            }
            else
            {
                renderHiddenCards(dealerCardsBox);
                dealerHandStatusLabel.setText("Dealer Cards Hidden Until Both Players Decide");
            }
        }

        dealButton.setDisable(phase != GamePhase.BETTING);
        viewResultsButton.setDisable(phase != GamePhase.ROUND_OVER);

        boolean p1CanDecide = (phase == GamePhase.PLAYER_DECISION && !gameService.isP1Decided());
        p1PlayButton.setDisable(!p1CanDecide);
        p1FoldButton.setDisable(!p1CanDecide);

        boolean p2CanDecide = (phase == GamePhase.PLAYER_DECISION && !gameService.isP2Decided());
        p2PlayButton.setDisable(!p2CanDecide);
        p2FoldButton.setDisable(!p2CanDecide);

        if (phase == GamePhase.BETTING)
        {
            guidanceLabel.setText("Step 1: Enter Ante ($5–$25) and optional Pair Plus ($5–$25) for both players, then click 'Place Bets & Deal Cards'.");
            p1DecisionStatusLabel.setText(gameService.isP1AnteCarried() ? "Status: Carried Ante Locked ($" + player1.getAnteBet() + ")" : "Status: Ready for Bets");
            p2DecisionStatusLabel.setText(gameService.isP2AnteCarried() ? "Status: Carried Ante Locked ($" + player2.getAnteBet() + ")" : "Status: Ready for Bets");
        }
        else if (phase == GamePhase.PLAYER_DECISION)
        {
            guidanceLabel.setText("Step 2: Inspect your 3 cards! Each player must independently choose Play (match Ante) or Fold.");
            p1DecisionStatusLabel.setText(gameService.isP1Decided() ? (gameService.isP1Folded() ? "Decision: FOLDED" : "Decision: PLAY ($" + player1.getPlayBet() + ")") : "Action Needed: Choose Play or Fold");
            p2DecisionStatusLabel.setText(gameService.isP2Decided() ? (gameService.isP2Folded() ? "Decision: FOLDED" : "Decision: PLAY ($" + player2.getPlayBet() + ")") : "Action Needed: Choose Play or Fold");
        }
        else
        {
            guidanceLabel.setText("Round Complete! Dealer cards revealed. Click 'View Round Results' to see full payouts and start the next round.");
            p1DecisionStatusLabel.setText(gameService.isP1Folded() ? "Outcome: Folded" : "Outcome: Round Evaluated");
            p2DecisionStatusLabel.setText(gameService.isP2Folded() ? "Outcome: Folded" : "Outcome: Round Evaluated");
        }

        if (animateDeal)
        {
            playDealAnimation();
        }
        if (animateDealerReveal)
        {
            playDealerFlipAnimation();
        }
    }

    private void renderHiddenCards(HBox container)
    {
        container.getChildren().clear();

        for (int i = 0; i < 3; i++)
        {
            Label back = new Label("♠ ♥\n♦ ♣");
            back.getStyleClass().add("card-back");
            container.getChildren().add(back);
        }
    }

    private void renderFaceUpCards(HBox container, ArrayList<Card> cards)
    {
        container.getChildren().clear();
        for (Card c : cards)
        {
            Label cardLabel = new Label(formatCardDisplay(c));
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

    public static String formatCardDisplay(Card c)
    {
        String rank = switch (c.getValue())
        {
            case 11 -> "J";
            case 12 -> "Q";
            case 13 -> "K";
            case 14 -> "A";
            default -> String.valueOf(c.getValue());
        };

        String suitSymbol = switch (c.getSuit())
        {
            case 'H' -> "♥";
            case 'D' -> "♦";
            case 'S' -> "♠";
            case 'C' -> "♣";
            default -> "?";
        };

        return rank + "\n" + suitSymbol;
    }

    private void playDealAnimation()
    {
        ParallelTransition parallel = new ParallelTransition();
        int delayIndex = 0;

        for (HBox box : new HBox[]{p1CardsBox, p2CardsBox, dealerCardsBox})
        {
            for (Node cardNode : box.getChildren())
            {
                cardNode.setTranslateY(-90);
                cardNode.setOpacity(0.2);
                TranslateTransition tt = new TranslateTransition(Duration.millis(280), cardNode);
                tt.setFromY(-90);
                tt.setToY(0);
                tt.setDelay(Duration.millis(delayIndex * 45L));
                cardNode.setOpacity(1.0);
                parallel.getChildren().add(tt);
                delayIndex++;
            }
        }

        parallel.play();
    }

    private void playDealerFlipAnimation()
    {
        SequentialTransition seq = new SequentialTransition();

        for (Node cardNode : dealerCardsBox.getChildren())
        {
            RotateTransition rt = new RotateTransition(Duration.millis(220), cardNode);
            rt.setAxis(Rotate.Y_AXIS);
            rt.setFromAngle(90);
            rt.setToAngle(0);
            seq.getChildren().add(rt);
        }

        seq.play();
    }

    private int parseBetInput(String text, String fieldName)
    {
        if (text == null || text.trim().isEmpty())
        {
            throw new IllegalArgumentException(fieldName + " is required ($5–$25).");
        }

        try
        {
            return Integer.parseInt(text.trim());
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException(fieldName + " must be a whole dollar number.");
        }
    }

    private int parseOptionalBetInput(String text, String fieldName)
    {
        if (text == null || text.trim().isEmpty())
        {
            return 0;
        }

        try
        {
            return Integer.parseInt(text.trim());
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException(fieldName + " must be a whole dollar number.");
        }
    }
    
    private void showError(String message)
    {
        errorLabel.setText("Invalid Input: " + message);
        errorLabel.setVisible(true);
        errorLabel.setManaged(true);
    }

    private void hideError()
    {
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
    }
}
