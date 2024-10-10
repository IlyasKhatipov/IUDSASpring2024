// Ilyas

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String expression = scanner.nextLine();
        String rpn = infixToRPN(expression);
        System.out.println(rpn);
    }

    static class CustomStack {
        private static final int MAX = 1000;
        private String[] array;
        private int top;

        public CustomStack() {
            array = new String[MAX];
            top = -1;
        }

        public void push(String item) {
            array[++top] = item;
        }

        public String pop() {
            return array[top--];
        }

        public String peek() {
            return array[top];
        }

        public boolean isEmpty() {
            return top < 0;
        }
    }

    private static final int PRECEDENCE_PLUS_MINUS = 1;
    private static final int PRECEDENCE_MULTIPLY_DIVIDE = 2;
    private static final int PRECEDENCE_MIN_MAX = 3;
    private static final int PRECEDENCE_POWER = 4;

    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/") || token.equals("^");
    }

    private static boolean isFunction(String token) {
        return token.equals("min") || token.equals("max");
    }

    private static int precedence(String token) {
        switch (token) {
            case "+":
            case "-":
                return PRECEDENCE_PLUS_MINUS;
            case "*":
            case "/":
                return PRECEDENCE_MULTIPLY_DIVIDE;
            case "min":
            case "max":
                return PRECEDENCE_MIN_MAX;
            case "^":
                return PRECEDENCE_POWER;
            default:
                return -1;
        }
    }

    public static String infixToRPN(String expression) {
        StringBuilder result = new StringBuilder();
        CustomStack stack = new CustomStack();

        String[] tokens = expression.split("\\s+");
        for (String token : tokens) {
            if (Character.isDigit(token.charAt(0))) {
                result.append(token).append(" ");
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.append(stack.pop()).append(" ");
                }
                stack.pop();
            } else if (isOperator(token) || isFunction(token)) {
                while (!stack.isEmpty() && precedence(token) <= precedence(stack.peek())) {
                    result.append(stack.pop()).append(" ");
                }
                stack.push(token);
            } else if (token.equals(",")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.append(stack.pop()).append(" ");
                }
            } else {
                return "Error: Invalid token encountered.";
            }
        }

        while (!stack.isEmpty()) {
            result.append(stack.pop()).append(" ");
        }

        return result.toString().trim();
    }
}
