package edu.uic.cs342;
public class AmountValidator {
    public boolean isValid(String text) {
        if (text == null || text.isBlank()) return false;
        try { return Double.isFinite(Double.parseDouble(text)); }
        catch (NumberFormatException ex) { return false; }
    }
}
