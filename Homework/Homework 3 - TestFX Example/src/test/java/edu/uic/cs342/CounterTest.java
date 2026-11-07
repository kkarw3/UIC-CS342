package edu.uic.cs342;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class CounterTest {
    @Test void startsAtZero() { assertEquals(0, new Counter().getValue()); }
    @Test void increments() { Counter c = new Counter(); assertEquals(1, c.increment()); assertEquals(2, c.increment()); }
    @Test void resets() { Counter c = new Counter(); c.increment(); c.reset(); assertEquals(0, c.getValue()); }
}
