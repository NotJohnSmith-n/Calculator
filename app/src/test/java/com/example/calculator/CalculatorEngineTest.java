package com.example.calculator;

import org.junit.Test;
import static org.junit.Assert.*;

public class CalculatorEngineTest {

    @Test
    public void testComplexOrderOfOperations() {
        double result = CalculatorEngine.evaluateExpression("2+3*4-10/2");
        assertEquals(9.0, result, 0.001);
    }

    @Test
    public void testDivisionByZero() {
        double result = CalculatorEngine.evaluateExpression("5/0");
        assertTrue(Double.isInfinite(result));
    }

    @Test
    public void testNegativeNumberStart() {
        double result = CalculatorEngine.evaluateExpression("-5+3");
        assertEquals(-2.0, result, 0.001);
    }

    @Test
    public void testDecimalCalculation() {
        double result = CalculatorEngine.evaluateExpression("1.5*2");
        assertEquals(3.0, result, 0.001);
    }

    @Test
    public void testNegativeResultMultiplication() {
        double result = CalculatorEngine.evaluateExpression("5-10*2");
        assertEquals(-15.0, result, 0.001);
    }
}