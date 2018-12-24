package calculator;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * For a given equation, and a range of x-axis, class use {@link Parser} to parse the equation.
 * It then evaluates the equation against the given x-range and increment to generate the list
 * of coordinates, first derivative points, second derivative points, removable discontinuities
 * and points of inflection
 *
 * @author kelly.li
 */
public class EquationEvaluator {

    private final double[][] coordinates;
    private final double[][] firstDerivPts;
    private final double[][] secondDerivPts;
    private final double[][] turningPoints;
    private final double[][] pointsOfInflection;
    private final double[][] removableDiscontinuities;
    private List<Point2D> asymtotes = new ArrayList<>();
    private final EvaluatorNode equationTree;
    private final double step;
    private final double sX, eX;

    EquationEvaluator(final String equation, final double startX, final double endX, final double step) {
        if (equation == null || equation.isEmpty()) {
            equationTree = null;
        }
        else {
            equationTree = new Parser(equation).parse();
        }

        coordinates = evaluate(startX, endX, step);
        removableDiscontinuities = calcDiscontinuities(coordinates);
        firstDerivPts = calcDeriv(coordinates);
        secondDerivPts = calcDeriv(firstDerivPts);

        turningPoints = calcTurningPoints(firstDerivPts, false);
        pointsOfInflection = calcTurningPoints(secondDerivPts, true);
        this.step = step;
        sX = startX;
        eX = endX;
    }

    public double[][] getCoordinates() {
        return coordinates;
    }

    public double[][] getFirstDerivPts() {
        return firstDerivPts;
    }

    public double[][] getSecondDerivPts() {
        return secondDerivPts;
    }

    public double[][] getTurningPoints() {
        return turningPoints;
    }

    public double[][] getPointsOfInflection() {
        return pointsOfInflection;
    }

    public double[][] getRemovableDiscontinuities() {
        return removableDiscontinuities;
    }

    public List<Point2D> getAsymtotes() {
        return this.asymtotes;
    }

    public double calculateIntegration(double startX, double endX) {

        if (asymtotes.size() > 0 || removableDiscontinuities != null) {
            if (asymtotes.size() > 0) {
                for (final Point2D asymtote: asymtotes) {
                    if (Double.isNaN(asymtote.getY()) && asymtote.getX() >= startX && asymtote.getX() <= endX) {
                        return Double.NaN;
                    }
                }
            }
            if (removableDiscontinuities != null) {
                for (int i = 0; i < removableDiscontinuities.length; i++) {
                    if (removableDiscontinuities[i][0] >= startX && removableDiscontinuities[i][1] <= endX) {
                        return Double.NaN;
                    }
                }
            }
        }
        int index = 0;
        while (firstDerivPts[index][0] < startX && index < firstDerivPts.length) {
            index++;
        }

        double prevY = firstDerivPts[index][1];
        double sum = 0.0;
        while (firstDerivPts[index][0] <= endX && index < firstDerivPts.length) {
            double area = Window.INCREMENT * (firstDerivPts[index][1] + prevY) / 2.0;
            sum += area ;
            prevY = firstDerivPts[index][1];
            index++;
        }
        System.out.println("f(" + endX + ") - f(" + startX + ") = " + Math.round((coordinates[(int)((endX-sX)/step)][1] - coordinates[(int)((startX-sX)/step)][1])*100.0)/100.0);
        return Math.round(sum * 100.0) / 100.0;
    }

    private double[][] calcTurningPoints(final double[][] derivatives, boolean isSecondDeriv) {
        if (derivatives == null) {
            return null;
        }

        final List<Double> turnX = new ArrayList<>();
        double previousX = derivatives[0][0];
        double previousD = derivatives[0][1];
        for (final double[] derivate: derivatives) {
            final double currentX = derivate[0];
            final double currentD = derivate[1];
            if (Math.abs(currentD) < Window.INCREMENT / 10) {
                turnX.add(derivate[0]);
            }
            else {
                // When derivative changes direction, we can assume, there is a turning point
                // between the previous discrete drawing point and the current one.
                if (Math.abs(previousD + currentD) < Math.abs(previousD) + Math.abs(currentD)) {
                    turnX.add((previousX + currentX) / 2.0);
                }
            }

            previousX = currentX;
            previousD = currentD;
        }

        if (turnX.size() > 0) {
            final double[][] turns = new double[turnX.size()][2];
            int index = 0;
            for (final double point : turnX) {
                turns[index][0] = point;
                turns[index][1] = equationTree.evaluate(point);
                if (!isSecondDeriv)
                    System.out.print("Relative Extrema at: ");
                else
                    System.out.print("Point of Inflection at: ");
                System.out.println("( " + Math.round(turns[index][0]*100.0)/100.0 + ", " + Math.round(turns[index][1]*100.0)/100.0 + ")");
                ++index;
            }
            return turns;
        }
        return null;
    }

    // Evaluate an expression represented by a string and return coordinates as a two-dimensional array

    private double[][] evaluate(final double startX, final double endX, final double step) {

        if (equationTree == null) {
            return null;
        }

        final int count = (int) ((endX-startX)/step);
        final double[][] answers = new double[count][2]; //start here we need to do a for loop
        double x = startX;
        for (int i = 0; i < count; i++) {
            answers[i][0] = x;
            answers[i][1] = equationTree.evaluate(x);
            x += step;
        }
        return answers;
    }

    private double[][] calcDiscontinuities(final double[][] coordinates) {
        final List<double[]> discontinuePoints = new ArrayList<>();
        for (int i = 1; i < coordinates.length; ++i) {
            if (Double.isNaN(coordinates[i][1])) {
                double[] prev = coordinates[i-1];
                double[] next = coordinates[i+1];
                double midPoint = (prev[1] + next[1]) / 2.0;
                if (Math.abs(next[1] - prev[1]) < 399.0) {
                    final double[] point = new double[2];
                    point[0] = coordinates[i][0];
                    point[1] = midPoint;
                    System.out.println("Discontinuity at: ( " + Math.round(point[0] * 100.0)/100.0 + ", " + Math.round(point[1] * 100.0)/100.0 + " )");

                    discontinuePoints.add(point);
                }
                else if (!Double.isNaN(prev[1]) && !Double.isNaN(next[1])){
                    final Point2D aPoint = new Point2D.Double(coordinates[i][0], Double.NaN);
                    this.asymtotes.add(aPoint);
                }
            }
        }
        final double leftLimit = equationTree.evaluate(-Double.MAX_VALUE);
        final double rightLimit = equationTree.evaluate(Double.MAX_VALUE);
        if (Math.abs(rightLimit - leftLimit) < 0.001) {
            final Point2D aPoint = new Point2D.Double(Double.NaN, (leftLimit + rightLimit) / 2.0);
            this.asymtotes.add(aPoint);
        }

        if (discontinuePoints.size() == 0) {
            return null;
        }

        final double[][] result = new double[discontinuePoints.size()][2];
        int index = 0;
        for (final double[] point: discontinuePoints) {
            result[index++] = point;
        }

        return result;
    }

    private double[][] calcDeriv(final double[][] originalPoints) {
        if (originalPoints == null) {
            return null;
        }

        final int pointsCount = originalPoints.length;
        final double[][] deriv = new double[pointsCount - 1][2];
        double prevX = originalPoints[0][0];
        double prevY = originalPoints[0][1];
        for (int i = 1; i < pointsCount; i++) {
            final double slope = (originalPoints[i][1] - prevY)/(originalPoints[i][0] - prevX);
            deriv[i - 1][0] = (originalPoints[i][0] + prevX)/2;
            deriv[i - 1][1] = slope;
            prevX = originalPoints[i][0];
            prevY = originalPoints[i][1];
        }
        return deriv;
    }
}
