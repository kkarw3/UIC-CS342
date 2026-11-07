package edu.uic.cs342;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
class CounterAppTest extends ApplicationTest {
    @Override public void start(Stage stage) { new CounterApp().start(stage); }
    @Test void clickingIncrementUpdatesLabel() {
        clickOn("#incrementButton");
        Label label = lookup("#countLabel").queryAs(Label.class);
        assertEquals("1", label.getText());
    }
    @Test void counterAccumulatesClicks() {
        clickOn("#incrementButton"); clickOn("#incrementButton"); clickOn("#incrementButton");
        assertEquals("3", lookup("#countLabel").queryAs(Label.class).getText());
    }
}
