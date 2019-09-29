package com.tsystems.javaschool.tasks.calculator;

import java.util.LinkedList;

public class Calculator {

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        // TODO: Implement the logic here

        if (!checkStatement(statement)) {
            return null;
        }

        String result = getCalculatedString(statement);

        if (result == null) {
            return  null;
        }

        if (result.endsWith(".0")) {
            return result.substring(0, result.indexOf('.'));
        }

        return result;
    }

    private String getCalculatedString(String statement) {
        String result = "";

        if (statement.contains("(")) {
            result = statement.substring(0, statement.indexOf('('));
            String str1 = getCalculatedString(statement.substring(statement.indexOf('(') + 1));

            if (str1 == null) {
                return null;
            }

            result = result.concat(str1);
        } else {
            result = statement;
        }

        String calculated = "";

        if (result.contains(")")) {
            calculated = calculate(result.substring(0, result.indexOf(')')));

            if (calculated == null) {
                return null;
            }

            result = result.replaceFirst(".+\\)", calculated);
        } else {
            result = calculate(result);
        }

        return result;
    }

    private String calculate(String statement) {
        char multiplyOrDivideSign = ' ';
        int twoSignsInRowCounter = 0;

        LinkedList<String> list = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        if (statement.startsWith("-")) {
            sb.append("-");
            statement = statement.substring(1);
        }

        for (int i = 0; i < statement.length(); i++) {
            if (isMathSign(statement.charAt(i))) {
                twoSignsInRowCounter++;
                if (statement.charAt(i) == '+' || statement.charAt(i) == '-') {
                    if (twoSignsInRowCounter == 1) {
                        list.push(String.valueOf(statement.charAt(i)));
                    }
                } else {
                    multiplyOrDivideSign = statement.charAt(i);
                }
                continue;
            }

            if (twoSignsInRowCounter == 2) {
                sb.append('-');
            }

            twoSignsInRowCounter = 0;

            while (i < statement.length() && (statement.charAt(i) >= '0' && statement.charAt(i) <= '9'
                    || statement.charAt(i) == '.')) {
                sb.append(statement.charAt(i));
                i++;
            }

            if (multiplyOrDivideSign != ' ') {
                String result = calculateTwoNumbers(list.pop(), String.valueOf(multiplyOrDivideSign), sb.toString());

                if (result == null) {
                    return null;
                }

                list.push(result);
                multiplyOrDivideSign = ' ';
            } else {
                list.push(sb.toString());
            }

            i--;
            sb.setLength(0);
        }

        return calculateList(list);
    }

    private String calculateList(LinkedList<String> list) {
        String result = "";
        while (list.size() > 1) {
            result = calculateTwoNumbers(list.pollLast(), list.pollLast(), list.pollLast());
            list.addLast(result);
        }

        return list.get(0);
    }

    private String calculateTwoNumbers(String firstNumber, String sign, String secondNumber) {
        double num1 = Double.parseDouble(firstNumber);
        double num2 = Double.parseDouble(secondNumber);

        switch (sign) {
            case "*":
                return String.valueOf(num1 * num2);
            case "/":
                if (num2 == 0) {
                    return null;
                }
                return String.valueOf(num1 / num2);
            case "+":
                return String.valueOf(num1 + num2);
            case "-":
                return String.valueOf(num1 - num2);
            default:
                return null;
        }
    }

    private boolean isMathSign(char symbol) {
        switch (symbol) {
            case '+':
            case '-':
            case '/':
            case '*':
                return true;

            default:
                return false;
        }
    }

    private boolean checkSymbols(String statement) {
        for (int i = 0; i < statement.length(); i++) {
            char symbol = statement.charAt(i);

            if (symbol >= '0' && symbol <= '9') {
                continue;
            }

            switch (symbol) {
                case '*':
                case '+':
                case '/':
                case '-':
                case '.':
                case '(':
                case ')':
                    break;

                default:
                    return false;
            }
        }

        return true;
    }

    private boolean checkBrackets(String statement) {
        int openBracketsNumber = 0;
        int closeBracketsNumber = 0;

        for (int i = 0; i < statement.length(); i++) {
            switch (statement.charAt(i)) {
                case '(':
                    openBracketsNumber++;
                    break;
                case ')':
                    closeBracketsNumber++;
                    break;
            }
        }

        return openBracketsNumber == closeBracketsNumber;
    }

    private boolean checkDoubleSigns(String statement) {
        if (statement.matches(".*\\*\\*+.*|.*\\+\\++.*|.*--+.*|.*//+.*|.*\\.\\.+.*")) {
            return true;
        }

        return false;
    }

    private boolean checkStatement(String statement) {
        if (statement == null || statement.equals("")) {
            return false;
        }

        if (!checkSymbols(statement)) {
            return false;
        }

        if (statement.matches(".*\\+$|.*-$|.*\\*$|.*/$|.*\\.$|.*\\($")) {
            return false;
        }

        if (!checkBrackets(statement)) {
            return false;
        }

        if (checkDoubleSigns(statement)) {
            return false;
        }

        return true;
    }
}
