package edu.uic.cs342.poker.controller;

import edu.uic.cs342.poker.SceneNavigator;
import edu.uic.cs342.poker.service.GameService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class WelcomeController
{
    @FXML private Button startButton;
    @FXML private Button rulesButton;
    @FXML private Button exitButton;
    @FXML private Button themeToggleButton;
    @FXML private Label themeStatusLabel;
    @FXML private VBox rulesBox;

    @FXML
    public void initialize()
    {
        updateThemeLabel();
    }

    @FXML
    private void handleStartGame()
    {
        GameService.getInstance().resetGame();
        SceneNavigator.switchScreen(SceneNavigator.GAMEPLAY_FXML);
    }

    @FXML
    private void handleToggleRules()
    {
        boolean show = !rulesBox.isVisible();
        rulesBox.setVisible(show);
        rulesBox.setManaged(show);
        rulesButton.setText(show ? "Hide Rules & Payouts" : "Game Rules & Payouts");
    }

    @FXML
    private void handleToggleTheme()
    {
        SceneNavigator.toggleTheme(startButton.getScene());
        updateThemeLabel();
    }

    @FXML
    private void handleExit()
    {
        Platform.exit();
    }

    private void updateThemeLabel()
    {
        themeStatusLabel.setText("Theme: " + SceneNavigator.getThemeName());
    }
}
