package edu.uic.cs342;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
class CounterFxmlAppTest extends ApplicationTest {
    @Override public void start(Stage stage) throws Exception { new CounterFxmlApp().start(stage); }
    @Test void fxmlButtonIsWired() {
        clickOn("#incrementButton");
        assertEquals("1", lookup("#countLabel").queryAs(Label.class).getText());
    }
    @Test void resetButtonRestoresZero() {
        clickOn("#incrementButton"); clickOn("#resetButton");
        assertEquals("0", lookup("#countLabel").queryAs(Label.class).getText());
    }
}
