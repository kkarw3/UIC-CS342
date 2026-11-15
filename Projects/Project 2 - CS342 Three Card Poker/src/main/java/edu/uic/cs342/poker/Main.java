package edu.uic.cs342.poker;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        SceneNavigator.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Three Card Poker");
        primaryStage.setMinWidth(980);
        primaryStage.setMinHeight(680);
        SceneNavigator.switchScreen(SceneNavigator.WELCOME_FXML);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}