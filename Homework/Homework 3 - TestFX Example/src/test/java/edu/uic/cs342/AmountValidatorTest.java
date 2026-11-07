package edu.uic.cs342;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;
class AmountValidatorTest {
    private final AmountValidator validator = new AmountValidator();
    @ParameterizedTest @CsvSource({"12.5", "0", "-2", "3e2"})
    void acceptsNumbers(String value) { assertTrue(validator.isValid(value)); }
    @ParameterizedTest @CsvSource({"abc", "NaN", "Infinity"})
    void rejectsNonFiniteOrInvalidNumbers(String value) { assertFalse(validator.isValid(value)); }
    @Test void rejectsBlankAndNull() { assertFalse(validator.isValid(" ")); assertFalse(validator.isValid(null)); }
}
