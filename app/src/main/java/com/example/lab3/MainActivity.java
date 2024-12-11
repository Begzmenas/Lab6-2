package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.MathContext;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private String currentInput = "";
    private BigDecimal lastResult = BigDecimal.ZERO;
    private boolean isNewOperation = true;
    private boolean isAfterEqual = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.tvCalculator);

        Button btnOne = findViewById(R.id.btnOne);
        Button btnTwo = findViewById(R.id.btnTwo);
        Button btnThree = findViewById(R.id.btnThree);
        Button btnFour = findViewById(R.id.btnFour);
        Button btnFive = findViewById(R.id.btnFive);
        Button btnSix = findViewById(R.id.btnSix);
        Button btnSeven = findViewById(R.id.btnSeven);
        Button btnEight = findViewById(R.id.btnEight);
        Button btnNine = findViewById(R.id.btnNine);
        Button btnZero = findViewById(R.id.btnZero);
        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnMinus = findViewById(R.id.btnMinus);
        Button btnMultiplication = findViewById(R.id.btnMultiplication);
        Button btnDivision = findViewById(R.id.btnDivision);
        Button btnSquareRoot = findViewById(R.id.btnSqrt);
        Button btnCE = findViewById(R.id.btnCE);
        Button btnDel = findViewById(R.id.btnDel);
        Button btnEqual = findViewById(R.id.btnEqual);
        Button btnDot = findViewById(R.id.btnDot);
        Button btnPlusMinus = findViewById(R.id.btnPlusMinus);
        Button btnClear = findViewById(R.id.btnC);

        btnOne.setOnClickListener(v -> appendToInput("1"));
        btnTwo.setOnClickListener(v -> appendToInput("2"));
        btnThree.setOnClickListener(v -> appendToInput("3"));
        btnFour.setOnClickListener(v -> appendToInput("4"));
        btnFive.setOnClickListener(v -> appendToInput("5"));
        btnSix.setOnClickListener(v -> appendToInput("6"));
        btnSeven.setOnClickListener(v -> appendToInput("7"));
        btnEight.setOnClickListener(v -> appendToInput("8"));
        btnNine.setOnClickListener(v -> appendToInput("9"));
        btnZero.setOnClickListener(v -> appendToInput("0"));
        btnDot.setOnClickListener(v -> appendToInput("."));

        btnPlus.setOnClickListener(v -> setOperation("+"));
        btnMinus.setOnClickListener(v -> setOperation("-"));
        btnMultiplication.setOnClickListener(v -> setOperation("*"));
        btnDivision.setOnClickListener(v -> setOperation("/"));
        btnSquareRoot.setOnClickListener(v -> calculateSquareRoot());

        btnEqual.setOnClickListener(v -> calculateResult());
        btnDel.setOnClickListener(v -> deleteLastCharacter());
        btnCE.setOnClickListener(v -> clearInput());
        btnClear.setOnClickListener(v -> clearAll());
        btnPlusMinus.setOnClickListener(v -> toggleSign());
    }

    private void appendToInput(String value) {
        if (isAfterEqual) {
            currentInput = "";
            isAfterEqual = false;
        }

        if (value.equals(".")) {
            int lastOperatorIndex = -1;
            for (int i = currentInput.length() - 1; i >= 0; i--) {
                char c = currentInput.charAt(i);
                if (isOperator(String.valueOf(c))) {
                    lastOperatorIndex = i;
                    break;
                }
            }

            String lastNumber = lastOperatorIndex == -1 ? currentInput : currentInput.substring(lastOperatorIndex + 1);

            if (!lastNumber.contains(".")) {
                currentInput += ".";
            }
        } else {
            currentInput += value;
        }

        display.setText(currentInput.isEmpty() ? "0" : currentInput);
        isNewOperation = false;
    }

    private void setOperation(String operation) {
        if (isAfterEqual) {
            currentInput = lastResult.stripTrailingZeros().toPlainString();
            isAfterEqual = false;
        }

        if (currentInput.isEmpty() && lastResult.compareTo(BigDecimal.ZERO) != 0) {
            currentInput = lastResult.stripTrailingZeros().toPlainString();
        } else if (currentInput.isEmpty()) {
            currentInput = "0";
        }

        if (isLastCharOperator()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1) + operation;
        } else {
            currentInput += operation;
        }

        display.setText(currentInput);
        isNewOperation = true;
    }

    private boolean isLastCharOperator() {
        return currentInput.endsWith("+") || currentInput.endsWith("-") ||
                currentInput.endsWith("*") || currentInput.endsWith("/") ||
                currentInput.endsWith("√");
    }

    private void calculateResult() {
        if (isLastCharOperator()) {
            display.setText("Error: Incomplete Expression");
            return;
        }

        if (!currentInput.isEmpty()) {
            lastResult = evaluateExpression(currentInput);
            String resultDisplay = lastResult.stripTrailingZeros().toPlainString();
            display.setText(resultDisplay);
            currentInput = resultDisplay;
            isNewOperation = true;
            isAfterEqual = true;
        }
    }

    private void calculateSquareRoot() {
        if (!currentInput.isEmpty() && !isLastCharOperator()) {
            BigDecimal number = new BigDecimal(currentInput);
            if (number.compareTo(BigDecimal.ZERO) >= 0) {
                lastResult = BigDecimal.valueOf(Math.sqrt(number.doubleValue()));
                String resultDisplay = lastResult.stripTrailingZeros().toPlainString();
                display.setText(resultDisplay);
                currentInput = resultDisplay;
                isAfterEqual = false;
                isNewOperation = false;
            } else {
                display.setText("Error: Negative");
                clearInput();
            }
        }
    }

    private BigDecimal evaluateExpression(String expression) {
        String[] parts = expression.split("(?<=[-+*/√])|(?=[-+*/√])");
        BigDecimal result = new BigDecimal(parts[0].trim());
        String operation = "";

        for (int i = 1; i < parts.length; i++) {
            String part = parts[i].trim();
            if (part.isEmpty()) continue;
            if (isOperator(part)) {
                operation = part;
            } else {
                BigDecimal number = new BigDecimal(part);
                switch (operation) {
                    case "+":
                        result = result.add(number);
                        break;
                    case "-":
                        result = result.subtract(number);
                        break;
                    case "*":
                        result = result.multiply(number);
                        break;
                    case "/":
                        if (number.compareTo(BigDecimal.ZERO) != 0) {
                            result = result.divide(number, MathContext.DECIMAL128);
                        } else {
                            display.setText("Error: Division by Zero");
                            clearAll();
                        }
                        break;
                }
            }
        }
        return result;
    }

    private boolean isOperator(String part) {
        return part.equals("+") || part.equals("-") || part.equals("*") || part.equals("/") || part.equals("√");
    }

    private void deleteLastCharacter() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            display.setText(currentInput.isEmpty() ? "0" : currentInput);
        }
    }

    private void clearInput() {
        currentInput = "";
        display.setText("0");
        isNewOperation = true;
        isAfterEqual = false;
    }

    private void clearAll() {
        currentInput = "";
        lastResult = BigDecimal.ZERO;
        display.setText("0");
        isNewOperation = true;
        isAfterEqual = false;
    }

    private void toggleSign() {
        if (!currentInput.isEmpty()) {
            BigDecimal number = new BigDecimal(currentInput);
            currentInput = number.negate().toString();
            display.setText(currentInput);
        }
    }
}
