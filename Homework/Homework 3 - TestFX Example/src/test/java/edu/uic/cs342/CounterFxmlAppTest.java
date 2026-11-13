package edu.uic.cs342;

import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

class CounterFxmlAppTest extends ApplicationTest
{
    @Override
    public void start(Stage stage) throws Exception
    {
        new CounterFxmlApp().start(stage);
    }
    @Test
    void fxmlButtonIsWired()
    {
        clickOn("#incrementButton");
        assertEquals("1", lookup("#countLabel").queryAs(Label.class).getText());
    }
    @Test
    void resetButtonRestoresZero()
    {
        clickOn("#incrementButton");
        clickOn("#resetButton");
        assertEquals("0", lookup("#countLabel").queryAs(Label.class).getText());
    }
    @Test
    void decrementButtonLowersCount()
    {
        clickOn("#incrementButton");
        clickOn("#incrementButton");
        clickOn("#decrementButton");
        assertEquals("1", lookup("#countLabel").queryAs(Label.class).getText());
    }
    @Test
    void resetDisabledWhenZero()
    {
        Button resetButton = lookup("#resetButton").queryAs(Button.class);
        assertTrue(resetButton.isDisabled(), "Reset should be disabled on startup (0)");

        clickOn("#incrementButton");
        assertFalse(resetButton.isDisabled(), "Reset should be enabled after increment");

        clickOn("#resetButton");
        assertTrue(resetButton.isDisabled(), "Reset should be disabled after resetting to 0");
    }
    @Test
    void negativeAmountCriteria()
    {
        Button decrementButton = lookup("#decrementButton").queryAs(Button.class);
        assertTrue(decrementButton.isDisabled(), "Decrement should be disabled at zero");

        clickOn("#incrementButton");
        assertFalse(decrementButton.isDisabled());
        clickOn("#decrementButton");

        assertEquals("0", lookup("#countLabel").queryAs(Label.class).getText());
        assertTrue(decrementButton.isDisabled(), "Decrement should be disabled after returning to zero");
    }
}
