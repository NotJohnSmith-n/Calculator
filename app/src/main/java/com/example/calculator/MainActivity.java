package com.example.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView txtDisplay;
    private boolean isResultDisplayed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtDisplay = findViewById(R.id.txtDisplay);
        txtDisplay.setText("0");

        setupButtons();
    }

    private void setupButtons() {
        int[] numberIds = {R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9};
        for (int id : numberIds) {
            Button btn = findViewById(id);
            btn.setOnClickListener(view -> appendText(btn.getText().toString()));
        }
        findViewById(R.id.btnDot).setOnClickListener(view -> appendText("."));

        int[] opIds = {R.id.btnPlus, R.id.btnMinus, R.id.btnMultiply, R.id.btnDivide};
        for (int id : opIds) {
            Button btn = findViewById(id);
            btn.setOnClickListener(view -> {
                isResultDisplayed = false;
                appendText(btn.getText().toString());
            });
        }

        findViewById(R.id.btnEquals).setOnClickListener(view -> calculateFinalResult());

        findViewById(R.id.btnClear).setOnClickListener(view -> {
            txtDisplay.setText("0");
            isResultDisplayed = false;
        });

        findViewById(R.id.btnBackspace).setOnClickListener(view -> {
            String text = txtDisplay.getText().toString();
            if (isResultDisplayed || text.equals("Error")) {
                txtDisplay.setText("0");
                isResultDisplayed = false;
                return;
            }
            if (text.length() > 1) {
                txtDisplay.setText(text.substring(0, text.length() - 1));
            } else {
                txtDisplay.setText("0");
            }
        });

        findViewById(R.id.btnSqrt).setOnClickListener(view -> applyFunction(val -> Math.sqrt(val)));
        findViewById(R.id.btnSign).setOnClickListener(view -> applyFunction(val -> val * -1));
    }

    private void appendText(String str) {
        String currentText = txtDisplay.getText().toString();

        if (isResultDisplayed && isNumber(str)) {
            currentText = "0";
            isResultDisplayed = false;
        }

        if (currentText.equals("0") && isNumber(str)) {
            txtDisplay.setText(str);
        } else if (currentText.equals("Error")) {
            txtDisplay.setText(str);
        } else {
            txtDisplay.setText(currentText + str);
        }
    }

    private boolean isNumber(String str) {
        return "0123456789.".contains(str);
    }

    private void calculateFinalResult() {
        String expression = txtDisplay.getText().toString();
        try {
            double result = evaluateExpression(expression);
            txtDisplay.setText(formatResult(result));
            isResultDisplayed = true;
        } catch (Exception e) {
            txtDisplay.setText("Error");
            isResultDisplayed = true;
        }
    }

    private double evaluateExpression(String expression) {
        List<Double> numbers = new ArrayList<>();
        List<Character> operators = new ArrayList<>();

        StringBuilder tempNum = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                tempNum.append(c);
            } else if ("+-*/".indexOf(c) >= 0) {
                if (c == '-' && tempNum.length() == 0) {
                    tempNum.append(c);
                } else {
                    if (tempNum.length() > 0) {
                        numbers.add(Double.parseDouble(tempNum.toString()));
                        tempNum.setLength(0);
                    }
                    operators.add(c);
                }
            }
        }
        if (tempNum.length() > 0) {
            numbers.add(Double.parseDouble(tempNum.toString()));
        }

        if (numbers.isEmpty()) return 0;
        if (operators.isEmpty()) return numbers.get(0);

        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            if (op == '*' || op == '/') {
                double n1 = numbers.get(i);
                double n2 = numbers.get(i + 1);
                double res = (op == '*') ? (n1 * n2) : (n1 / n2);

                numbers.set(i, res);
                numbers.remove(i + 1);
                operators.remove(i);
                i--;
            }
        }

        double result = numbers.get(0);
        for (int i = 0; i < operators.size(); i++) {
            char op = operators.get(i);
            double nextNum = numbers.get(i + 1);
            if (op == '+') result += nextNum;
            if (op == '-') result -= nextNum;
        }

        return result;
    }

    private void applyFunction(Function function) {
        calculateFinalResult();

        String text = txtDisplay.getText().toString();
        if (!text.equals("Error")) {
            try {
                double value = Double.parseDouble(text);
                double res = function.apply(value);
                txtDisplay.setText(formatResult(res));
                isResultDisplayed = true;
            } catch (Exception e) {
                txtDisplay.setText("Error");
            }
        }
    }

    private String formatResult(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "Error";
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            String formatted = String.format("%.8f", value);
            return formatted.replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }

    interface Function {
        double apply(double val);
    }
}