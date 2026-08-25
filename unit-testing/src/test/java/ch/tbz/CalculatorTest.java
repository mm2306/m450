package ch.tbz;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorTest {

    private Calculator calculator;
    private static final double DELTA = 0.0001;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Addition zweier positiver Zahlen")
    void testAdd() {
        assertEquals(15.0, calculator.add(10.0, 5.0), DELTA);
    }

    @Test
    @DisplayName("Subtraktion zweier Zahlen")
    void testSubtract() {
        assertEquals(5.0, calculator.subtract(10.0, 5.0), DELTA);
    }

    @Test
    @DisplayName("Multiplikation zweier Zahlen")
    void testMultiply() {
        assertEquals(50.0, calculator.multiply(10.0, 5.0), DELTA);
    }

    @Test
    @DisplayName("Division durch reguläre Zahl")
    void testDivide() {
        assertEquals(2.0, calculator.divide(10.0, 5.0), DELTA);
    }

    @Test
    @DisplayName("Division durch Null wirft IllegalArgumentException")
    void testDivideByZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        });
        assertEquals("Division durch Null ist nicht erlaubt!", exception.getMessage());
    }
}
