package com.example.lab3;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert;

@RunWith(AndroidJUnit4.class)
public class CalculatorTest {

    // Test addition method
    @Test
    public void testAddition() {
        int result = Calculator.add(5, 3);
        Assert.assertEquals(8, result);
    }

    // Test subtraction method
    @Test
    public void testSubtraction() {
        int result = Calculator.subtract(7, 4);
        Assert.assertEquals(3, result);
    }

    // Test multiplication method
    @Test
    public void testMultiplication() {
        int result = Calculator.multiply(5, 6);
        Assert.assertEquals(30, result);
    }

    // Test division method
    @Test
    public void testDivision() {
        float result = Calculator.divide(10, 2);
        Assert.assertEquals(5, result, 0.0001);
    }

    // Test floating-point division
    @Test
    public void testFloatingPointDivision() {
        float result = Calculator.divide(7, 3);
        Assert.assertEquals(2.3333f, result, 0.0001);
    }

    // Test division by zero (should throw ArithmeticException)
    @Test(expected = ArithmeticException.class)
    public void testDivisionByZero() {
        Calculator.divide(10, 0);
    }

    // Test square root calculation
    @Test
    public void testSqrt() {
        double result = Calculator.sqrt(16);
        Assert.assertEquals(4, result, 0.0001);
    }

    // Test square root of a floating-point number
    @Test
    public void testSqrtFloatingPoint() {
        double result = Calculator.sqrt(2.25);
        Assert.assertEquals(1.5, result, 0.0001);
    }

    // Test chaining multiple arithmetic operations (e.g., 5 + 3 * 2)
    @Test
    public void testChainingOperations() {
        int result = Calculator.add(5, Calculator.multiply(3, 2));  // 5 + 6 = 11
        Assert.assertEquals(11, result);
    }

    // Test memory store and recall functionality
    @Test
    public void testMemoryStore() {
        Calculator calculator = new Calculator();
        calculator.setMemory(42);
        Assert.assertEquals(42, calculator.getMemory(), 0.0001);
    }

    // Test adding to memory
    @Test
    public void testAddToMemory() {
        Calculator calculator = new Calculator();
        calculator.setMemory(50);
        calculator.addToMemory(20);
        Assert.assertEquals(70, calculator.getMemory(), 0.0001);
    }

    // Test subtracting from memory
    @Test
    public void testSubtractFromMemory() {
        Calculator calculator = new Calculator();
        calculator.setMemory(50);
        calculator.subtractFromMemory(15);
        Assert.assertEquals(35, calculator.getMemory(), 0.0001);
    }

    // Test handling large numbers
    @Test
    public void testLargeNumbers() {
        int result = Calculator.add(Integer.MAX_VALUE, 1);
        Assert.assertEquals(Integer.MIN_VALUE, result);  // Overflow test
    }

    // Test operations with negative numbers
    @Test
    public void testNegativeNumbers() {
        int result = Calculator.subtract(-5, -3);
        Assert.assertEquals(-2, result);
    }

    // Test pressing the "1" button on the calculator UI
    @Test
    public void testButtonOnePress() {
        Espresso.onView(ViewMatchers.withId(R.id.btnOne))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.tvCalculator))
                .check(ViewAssertions.matches(ViewMatchers.withText("1")));
    }

    // Test pressing the addition button and calculating 1 + 2
    @Test
    public void testAdditionUI() {
        Espresso.onView(ViewMatchers.withId(R.id.btnOne)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnPlus)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnTwo)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnEqual)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.tvCalculator))
                .check(ViewAssertions.matches(ViewMatchers.withText("3")));
    }

    // Test pressing the clear button on the UI
    @Test
    public void testClearButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btnOne)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnC)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.tvCalculator))
                .check(ViewAssertions.matches(ViewMatchers.withText("0")));
    }

    // Test chaining multiple arithmetic operations in UI (e.g., 5 + 3 * 2)
    @Test
    public void testChainingOperationsUI() {
        Espresso.onView(ViewMatchers.withId(R.id.btnFive)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnPlus)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnThree)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnMultiplication)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnTwo)).perform(ViewActions.click());
        Espresso.onView(ViewMatchers.withId(R.id.btnEqual)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.tvCalculator))
                .check(ViewAssertions.matches(ViewMatchers.withText("11")));
    }
}
class Calculator {

    private int memory;

    public static int add(int a, int b) {
        return a + b;
    }

    public static int subtract(int a, int b) {
        return a - b;
    }

    public static int multiply(int a, int b) {
        return a * b;
    }

    public static float divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return (float) a / b;
    }

    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    // Memory operations
    public void setMemory(int value) {
        memory = value;
    }

    public int getMemory() {
        return memory;
    }

    public void addToMemory(int value) {
        memory += value;
    }

    public void subtractFromMemory(int value) {
        memory -= value;
    }
}