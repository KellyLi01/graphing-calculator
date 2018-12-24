package calculator;

/**
 * @author kelly.li
 */
public class Parser {

    private final String str; // original input expression
    private int pos = -1;
    private int ch;

    public Parser(final String expr) {
        str = expr;
    }

    // This is the public method. It returns a tree of EvaluatorNode when complete parsing
    public EvaluatorNode parse() {
        nextChar();
        EvaluatorNode x = parseExpression();
        if (pos < str.length()) {
            throw new RuntimeException("Unexpected: " + (char) ch);
        }
        return x;
    }

    private void nextChar() {
        ch = (++pos < str.length()) ? str.charAt(pos) : -1;
    }

    private boolean consume(int expectedChar) {
        while (ch == ' ') nextChar();
        if (ch == expectedChar) {
            nextChar();
            return true;
        }
        return false;
    }

    // Grammar:
    // expression: term | expression `+` term | expression `-` term
    // term: factor | term `*` factor | term `/` factor
    // factor: e | number | function `(` expression `)` | `+` factor | `-` factor | `(` expression `)` | factor `^` factor
    //

    private EvaluatorNode parseExpression() {
        EvaluatorNode x = parseTerm();
        for (;;) {
            if (consume('+')) {
                final EvaluatorNode secondArg = parseTerm();
                x = new EvaluatorNode("+", x, secondArg);
            }
            else if (consume('-')) {
                final EvaluatorNode secondArg = parseTerm();
                x = new EvaluatorNode("-", x, secondArg);
            }
            else {
                return x;
            }
        }
    }

    private EvaluatorNode parseTerm() {
        EvaluatorNode x = parseTerm0();
        while (true) {
            if (consume('*')) {
                final EvaluatorNode secondArg = parseTerm0();
                x = new EvaluatorNode("*", x, secondArg);
            }

            else if (consume('/')) {
                final EvaluatorNode secondArg = parseTerm0();
                x = new EvaluatorNode("/", x, secondArg);
            }
            else if (pos < str.length() && ((char)ch) != '+' && ((char)ch) != '-' && ((char)ch) != ')') {
                final EvaluatorNode secondArg = parseTerm0();
                x = new EvaluatorNode("*", x, secondArg);
            }
            else {
                return x;
            }
        }
    }

    private EvaluatorNode parseTerm0() {
        final EvaluatorNode x = parseFactor();
        if (consume('^')) {
            final EvaluatorNode secondArg = parseTerm0();
            return new EvaluatorNode("^", x, secondArg);
        }

        return x;
    }

    private EvaluatorNode parseFactor() {
        if (consume('+')) {
            return parseTerm(); // unary plus
        }
        if (consume('-')) {
            return new EvaluatorNode("-", parseTerm());
        }

        if (consume('x')) {
            return new EvaluatorNode("x");
        }

        final int startPos = pos;

        if (consume('(')) {
            EvaluatorNode x = parseExpression();
            if (!consume(')')) {
                throw new IllegalStateException("Missing ). ");
            }
            return x;
        }

        if ((ch >= '0' && ch <= '9') || ch == '.') {
            while ((ch >= '0' && ch <= '9') || ch == '.') {
                nextChar();
            }
            double aDouble = Double.parseDouble(str.substring(startPos, pos));

            final EvaluatorNode numeric = new EvaluatorNode(aDouble);
            return numeric;
        }

        if (ch >= 'a' && ch <= 'z') { // e, pi, trig, logarithmic
            while (ch >= 'a' && ch <= 'z') {
                nextChar();
            }
            String func = str.substring(startPos, pos);

            if (func.equals("e")) {
                return new EvaluatorNode(Math.E);
            }

            if (func.equals("pi")) {
                return new EvaluatorNode(Math.PI);
            }

            if (!consume('(')) {
                throw new IllegalStateException("Expecting ( but seeing " + (char) ch);
            }

            final EvaluatorNode unarg = parseExpression();

            if (!consume(')')) {
                throw new IllegalStateException("Expecting ) but seeing " + (char) ch);
            }

            return new EvaluatorNode(func, unarg);
        }

        throw new RuntimeException("Unexpected: " + (char)ch);
    }
}
