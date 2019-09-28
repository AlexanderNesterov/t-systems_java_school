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
        if (statement == null || statement.equals("")) {
            return null;
        }

        if (!checkSymbols(statement)) {
            return null;
        }

        if (!checkBrackets(statement)) {
            return null;
        }

        if (checkDoubleSigns(statement)) {
            return null;
        }

        String result = getNumber(statement);

        if (result == null) {
            return  null;
        }

        if (result.endsWith(".0")) {
            return result.substring(0, result.indexOf('.'));
        }

        return result;
    }

    private String getNumber(String statement) {
        String str = "";

        if (statement.contains("(")) {
            str = statement.substring(0, statement.indexOf('('));
            String str1 = getNumber(statement.substring(statement.indexOf('(') + 1));
            if (str1 == null) {
                return null;
            }
            str = str.concat(str1);
        } else {
            str = statement;
        }

        String calculated = "";

        if (str.contains(")")) {
            calculated = calculate(str.substring(0, str.indexOf(')')));
            str = str.replaceFirst(".+\\)", calculated);
        } else {
            str = calculate(str);
        }

        return str;
    }

    private String calculate(String statement) {
        boolean isMultiplyOrDivide = false;
        char sign = ' ';
        int twoSignsCounter = 0;
        LinkedList<String> list = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        if (statement.startsWith("-")) {
            sb.append("-");
            statement = statement.substring(1);
        }

        for (int i = 0; i < statement.length(); i++) {
            if (isMathSign(statement.charAt(i))) {
                twoSignsCounter++;
                if (statement.charAt(i) == '+' || statement.charAt(i) == '-') {
                    if (twoSignsCounter == 1) {
                        list.push(String.valueOf(statement.charAt(i)));
                    }
                } else {
                    sign = statement.charAt(i);
                    isMultiplyOrDivide = true;
                }
                continue;
            }

            if (twoSignsCounter == 2) {
                sb.append('-');
            }

            twoSignsCounter = 0;

            while (i < statement.length() && (statement.charAt(i) >= '0' && statement.charAt(i) <= '9' || statement.charAt(i) == '.')) {
                sb.append(statement.charAt(i));
                i++;
            }

            if (isMultiplyOrDivide) {
                String result = calculateTwoNumbers(list.pop(), String.valueOf(sign), sb.toString());

                if (result == null) {
                    return null;
                }

                list.push(result);
                isMultiplyOrDivide = false;
            } else {
                list.push(sb.toString());
            }

            i--;
            sb.setLength(0);
        }

        return calculateList(list);
    }

    private String calculateList(LinkedList<String> list) {
        String res = "";
        while (list.size() > 1) {
            String num1 = list.pollLast();
            String sign = list.pollLast();
            String num2 = list.pollLast();
            res = calculateTwoNumbers(num1, sign, num2);
            list.addLast(res);
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

/*    public static void main(String[] args) {
        String str = new Calculator().evaluate("10/3");
        System.out.println(str);
    }*/
}
