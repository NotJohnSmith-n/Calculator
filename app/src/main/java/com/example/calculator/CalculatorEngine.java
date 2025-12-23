package com.example.calculator;

import java.util.ArrayList;
import java.util.List;

public class CalculatorEngine {

    public static double evaluateExpression(String expression) {
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
}