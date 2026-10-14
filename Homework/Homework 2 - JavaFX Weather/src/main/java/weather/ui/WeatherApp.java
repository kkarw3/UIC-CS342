package weather.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import weather.controller.WeatherController;
import weather.service.WeatherService;

import java.net.URL;

public class WeatherApp extends Application
{
    private BorderPane mainLayout;
    private ProgrammaticWeatherView programmaticWeatherView;
    private Parent fxmlWeatherView;

    @Override
    public void start(Stage mainStage) throws Exception
    {
        WeatherService weatherService = new WeatherService();
        programmaticWeatherView = new ProgrammaticWeatherView(weatherService);

        URL fxmlLocation = getClass().getResource("/weather/weather-view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        fxmlWeatherView = loader.load();

        WeatherController controller = loader.getController();
        controller.setWeatherService(weatherService);

        Label navigationLabel = new Label("Switch Interface:");
        navigationLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white;");

        Button buttonProgrammatic = new Button("Programmatic Dashboard");
        Button buttonFXML = new Button("FXML Dashboard");
        
        buttonProgrammatic.setOnAction(e -> mainLayout.setCenter(programmaticWeatherView));
        buttonFXML.setOnAction(e -> mainLayout.setCenter(fxmlWeatherView));

        HBox navigationBar = new HBox(15, navigationLabel, buttonProgrammatic, buttonFXML);
        navigationBar.setAlignment(Pos.CENTER);
        navigationBar.setPadding(new Insets(10));
        navigationBar.setStyle("-fx-background-color: #2c3e50;");

        mainLayout = new BorderPane();
        mainLayout.setTop(navigationBar);
        mainLayout.setCenter(programmaticWeatherView);

        Scene scene = new Scene(mainLayout, 850, 550);

        mainStage.setTitle("Weather Dashboard");
        mainStage.setScene(scene);
        mainStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}