package edu.uic.cs342;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
public class CounterController {
    @FXML private Label countLabel;
    private final Counter counter = new Counter();
    @FXML private void increment() { countLabel.setText(Integer.toString(counter.increment())); }
    @FXML private void reset() { counter.reset(); countLabel.setText("0"); }
}
