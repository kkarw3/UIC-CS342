module edu.uic.cs342.poker
{
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.uic.cs342.poker to javafx.fxml;
    opens edu.uic.cs342.poker.controller to javafx.fxml;

    exports edu.uic.cs342.poker;
    exports edu.uic.cs342.poker.controller;
    exports edu.uic.cs342.poker.model;
}