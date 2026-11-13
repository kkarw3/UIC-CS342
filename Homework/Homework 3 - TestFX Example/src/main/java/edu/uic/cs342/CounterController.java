package edu.uic.cs342;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class CounterController
{
    @FXML private Label countLabel;
    @FXML private Button decrementButton;
    @FXML private Button resetButton;

    private final Counter counter = new Counter();

    private void updateUI()
    {
        countLabel.setText(Integer.toString(counter.getValue()));
        boolean isZero = (counter.getValue() == 0);
        resetButton.setDisable(isZero);
        decrementButton.setDisable(isZero);
    }

    @FXML
    public void initialize()
    {
        updateUI();
    }

    @FXML
    private void increment()
    {
        counter.increment();
        updateUI();
    }

    @FXML
    private void decrement()
    {
        if (counter.getValue() > 0)
        {
            counter.decrement();
        }
        updateUI();
    }

    @FXML
    private void reset()
    {
        counter.reset();
        updateUI();
    }
}