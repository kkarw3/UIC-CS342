package edu.uic.cs342;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
class AmountFormAppTest extends ApplicationTest {
    @Override public void start(Stage stage) { new AmountFormApp().start(stage); }
    @Test void invalidInputShowsFeedback() {
        clickOn("#amountField").write("not a number"); clickOn("#calculateButton");
        assertEquals("Enter a valid amount", lookup("#errorLabel").queryAs(Label.class).getText());
    }
    @Test void validInputShowsConfirmation() {
        clickOn("#amountField").write("42.5"); clickOn("#calculateButton");
        assertEquals("Amount accepted", lookup("#errorLabel").queryAs(Label.class).getText());
    }
}
