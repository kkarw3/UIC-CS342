package edu.uic.cs342.poker;

import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;

public class SceneNavigator
{
    private static Stage primaryStage;
    private static boolean useDarkTheme = false;

    public static final String WELCOME_FXML = "/edu/uic/cs342/poker/fxml/welcome.fxml";
    public static final String GAMEPLAY_FXML = "/edu/uic/cs342/poker/fxml/gameplay.fxml";
    public static final String RESULTS_FXML = "/edu/uic/cs342/poker/fxml/results.fxml";

    private static final String LIGHT_CSS = "/edu/uic/cs342/poker/css/light.css";
    private static final String DARK_CSS = "/edu/uic/cs342/poker/css/dark.css";

    public static void setPrimaryStage(Stage stage)
    {
        primaryStage = stage;
    }

    public static void switchScreen(String fxmlPath)
    {
        try
        {
            URL resource = SceneNavigator.class.getResource(fxmlPath);
            if (resource == null)
            {
                throw new IllegalStateException("Cannot find FXML resource: " + fxmlPath);
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent root = loader.load();

            Scene scene = primaryStage.getScene();
            if (scene == null)
            {
                scene = new Scene(root, 1080, 740);
                primaryStage.setScene(scene);
            }
            else
            {
                scene.setFill(Color.BLACK);
                scene.setRoot(root);
            }

            applyTheme(scene);

            // Fade transition between screens
            // NOTE: subject to change
            root.setOpacity(0.0);
            FadeTransition fade = new FadeTransition(Duration.millis(500), root);
            fade.setFromValue(0.0);
            fade.setToValue(1.0);
            fade.play();
        }
        catch (IOException e)
        {
            throw new RuntimeException("Failed to load screen: " + fxmlPath, e);
        }
    }

    public static void toggleTheme(Scene scene)
    {
        useDarkTheme = !useDarkTheme;

        if (scene != null)
        {
            applyTheme(scene);
        }
    }

    public static void applyTheme(Scene scene)
    {
        if (scene == null)
        {
            return;
        }

        scene.getStylesheets().clear();
        String cssPath = useDarkTheme ? DARK_CSS : LIGHT_CSS;
        URL cssUrl = SceneNavigator.class.getResource(cssPath);

        if (cssUrl != null)
        {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }
    }

    public static boolean isDarkTheme()
    {
        return useDarkTheme;
    }

    public static String getThemeName()
    {
        return useDarkTheme ? "Dark" : "Light";
    }
}
