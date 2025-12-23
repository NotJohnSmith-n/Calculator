package com.example.calculator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

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
            double result = CalculatorEngine.evaluateExpression(expression);
            txtDisplay.setText(formatResult(result));
            isResultDisplayed = true;
        } catch (Exception e) {
            txtDisplay.setText("Error");
            isResultDisplayed = true;
        }
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