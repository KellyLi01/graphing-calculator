package calculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Each node could be
 * 1. an expression like: term1 + term2
 * 2. a term like: x^2, 2x, 5 * x (where * is optional, i.e. 5x and 5*x are treated the same), a / b
 * 3. a factor like: 123, x, sin(x), ln(x)
 *
 * @author kelly.li
 */
public class EvaluatorNode {
    private List<EvaluatorNode> children;
    private String value;
    private double aDouble;

    public EvaluatorNode(String equation) {
        children = new ArrayList<>();
        value = equation;
        aDouble = 0.0;
    }

    public EvaluatorNode(double dd) {
        aDouble = dd;
    }

    public EvaluatorNode(String func, EvaluatorNode left, EvaluatorNode right) {
        this(func);
        addChild(left);
        addChild(right);
    }

    public EvaluatorNode(String func, EvaluatorNode unary) {
        this(func);
        addChild(unary);
    }

    public double evaluate(final double xVal) {
        if (value == null) {
            return aDouble;
        }

        if (value.isEmpty()) {
            return 0.0;
        }

        if (value.equalsIgnoreCase("x")) {
            return xVal;
        }
        
        if (value.equals("+")) {
            return add(xVal);
        }

        if (value.equals("-")) {
            return subtract(xVal);
        }

        if (value.equals("*")) {
            return multiply(xVal);
        }

        if (value.equals("/")) {
            try {
                return divide(xVal);
            }
            catch (final ArithmeticException e) {
                if (e.getMessage().startsWith("Zero dividend")) {
                    return Double.NaN;
                }
            }
        }

        if (value.equals("^")) {
            return exponent(xVal);
        }

        if (value.equals("sin")) {
            return sin(xVal);
        }

        if (value.equals("cos")) {
            return cos(xVal);
        }

        if (value.equals("tan")) {
            return tan(xVal);
        }

        if (value.equals("abs")) {
            return abs(xVal);
        }

        if (value.equals("log")) {
            return log(xVal);
        }

        if (value.equals("ln")) {
            return ln(xVal);
        }

        if (value.equals("sqrt")) {
            return sqrt(xVal);
        }

        if (value.equals("cqrt")) {
            return cqrt(xVal);
        }

        throw new IllegalStateException("Unknown operator: " + value);
    }

    //add child to parent
    private void addChild(EvaluatorNode c) {
        children.add(c);
    }

    private double add(final double xVal) {
        validateArguments(2);
        final double firstArgument = children.get(0).evaluate(xVal);
        final double secondArgument = children.get(1).evaluate(xVal);
        return firstArgument + secondArgument;
    }

    private double subtract(final double xVal) {
        validateArguments(1);
        final double firstArgument = children.get(0).evaluate(xVal);
        if (children.size() > 1) {
            final double secondArgument = children.get(1).evaluate(xVal);
            return firstArgument - secondArgument;
        }

        return -firstArgument;
    }

    private double multiply(final double xVal) {
        validateArguments(2);
        final double leftValue = children.get(0).evaluate(xVal);
        final double rightValue = children.get(1).evaluate(xVal);
        return leftValue * rightValue;
    }

    private double divide(final double xVal) {
        validateArguments(2);
        final double leftValue = children.get(0).evaluate(xVal);
        final double rightValue = children.get(1).evaluate(xVal);
        if (Math.abs(rightValue) < (Window.INCREMENT / 100.0)) {
            throw new ArithmeticException("Zero dividend");
        }
        return leftValue / rightValue;
    }

    private double exponent(final double xVal) {
        validateArguments(2);
        final double base = children.get(0).evaluate(xVal);
        final double originalExp = children.get(1).evaluate(xVal);
        final long exp = Math.round(originalExp);
        if (Math.abs(originalExp - exp) > 0.01) {
            throw new ArithmeticException("Please use function sqrt()/cqrt() for roots of power. For power calculation, exponent must be integer.");
        }
        return Math.pow(base, exp);
    }

    private double sin(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.sin(base);
    }

    private double cos(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.cos(base);
    }

    private double tan(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.tan(base);
    }

    private double abs(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.abs(base);
    }

    private double log(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.log10(base);
    }

    private double ln(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.log(base);
    }

    private double sqrt(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.sqrt(base);
    }

    private double cqrt(final double xVal) {
        validateArguments(1);
        final double base = children.get(0).evaluate(xVal);
        return Math.cbrt(base);
    }


    private void validateArguments(final int expectedCount) {
        if (children.size() < expectedCount) {
            throw new RuntimeException("Incorrect expression.");
        }
    }
}
