package calculator;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.Line2D;
import javax.swing.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yihua
 */
public class Window extends JFrame {

    static final double INCREMENT = 0.005;

    private final int WIDTH = 800;
    private final int HEIGHT = 400;
    private Grapher graph;
    private ControlPanel controls;
    private final int startX = -5;
    private final int startY = -5;
    private final int endX = 5;
    private final int endY = 5;
    private String equation;
    private EquationEvaluator evaluator;

    private boolean showGraph;
    private boolean showFirstDeriv;
    private boolean showSecondDeriv;
    private boolean showMinMax;
    private boolean showInflection;
    private boolean showIntegration;

    public Window() {
        super();
        getContentPane().setLayout(new GridLayout(1, 2));
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        graph = new Grapher();
        getContentPane().add(graph, 0);
        controls = new ControlPanel(new AbstractAction() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                // When the Graph button gets clicked in the control window,
                // We calculate the equation and repaint the Grapher panel
                calculate(e.getActionCommand());
                System.out.println("Action " + e.getActionCommand());
            }
        });
        getContentPane().add(controls, 1);
        setVisible(true);
    }


    public class Grapher extends JPanel{
        private int xGap = 0;
        private int xSize = 400;
        private final int zoom = xSize / (endX - startX);
        private int yGap = 0;
        private int ySize = 400;
        private Graphics g;
    
        public Grapher() {
            super(new FlowLayout());
            setPreferredSize(new Dimension(200, 200));
            setBackground(Color.WHITE);
        
            setVisible(true);
        }
    
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            drawGrid(g2);

            if (showGraph) {
                drawLine(g2, Color.red, evaluator.getCoordinates());

                final double[][] discontintuities = evaluator.getRemovableDiscontinuities();
                if (discontintuities != null) {
                    drawPoints(g2, Color.black, discontintuities, false);
                }

                final double[] asymptote = evaluator.getAsymptote();
                if (asymptote != null) {
                    drawDashedLine(g2, Color.pink, asymptote[0], /*-20.0*/ startX, asymptote[0], /*20.0*/ endX);
                    drawDashedLine(g2, Color.pink, /*-20.0*/ startY, asymptote[1], /*20.0*/ endY, asymptote[1]);
                    System.out.println("Asymptote at x=" + Math.round(asymptote[0]*100.0)/100.0);
                }
            }
            if (showMinMax) {
                final double[][] turningPoints = evaluator.getTurningPoints();
                if (turningPoints != null) {
                    drawPoints(g2, Color.darkGray, turningPoints, true);
                }
            }

            if (showInflection) {
                final double[][] flexions = evaluator.getFlexions();
                if (flexions != null) {
                    drawPoints(g2, Color.magenta, flexions, true);
                }
            }

            if (showFirstDeriv) {
                drawLine(g2, Color.green, evaluator.getFirstDerivPts());
            }
            if (showSecondDeriv) {
                drawLine(g2, Color.blue, evaluator.getSecondDerivPts());
            }

            if (showIntegration) {
                drawArea(g2, Color.orange);
                controls.showIntegrationResult(evaluator.calculateIntegration(controls.getLowerLimit(), controls.getUpperLimit()));
            }
        }

        public void drawDashedLine(Graphics g, Color color, double x0, double y0, double x1, double y1){

            //creates a copy of the Graphics instance
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(color);

            //set the stroke of the copy, not the original
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            g2d.setStroke(dashed);
            g2d.draw(new Line2D.Double(x0 * zoom + 200, 200 - y0 * zoom, x1 * zoom + 200, 200 - y1 * zoom));

            //gets rid of the copy
            g2d.dispose();
        }


        private void drawArea(final Graphics2D g2, final Color orange) {
            final double lowerLimit = controls.getLowerLimit();
            final double upperLimit = controls.getUpperLimit();
            g2.setColor(orange);
            for (final double[] point: evaluator.getCoordinates()) {
                double x = point[0];
                double y = point[1];
                if (Double.isNaN(y)) {
                    continue;
                }
                if (x >= lowerLimit && x <= upperLimit) {
                    x *= zoom;
                    y *= zoom;
                    g2.draw(new Line2D.Double(x + 200, 200 - 0, x + 200, 200 - y));
                }
            }
        }

        private void drawGrid(final Graphics2D g2) {
            final int distance = xSize / (endX - startX) * 2;

            g2.setColor(Color.black);
            g2.drawLine(xGap, yGap + ySize/2, xGap + xSize, yGap + ySize/2); // x-exile
            g2.drawLine(xGap +xSize/2, yGap, xGap + xSize/2, yGap + ySize);  // y-exile

            g2.setColor(Color.lightGray);
            int gridX = ySize / 2 - distance;
            while (gridX >= 0) {
                g2.drawLine(xGap, yGap + gridX, xGap + xSize, yGap + gridX); // x-grid line
                gridX -= distance;
            }

            gridX = ySize / 2 + distance;
            while (gridX <= ySize) {
                g2.drawLine(xGap, yGap + gridX, xGap + xSize, yGap + gridX); // x-grid line
                gridX += distance;
            }

            int gridY = xSize / 2 - distance;
            while (gridY >= 0) {
                g2.drawLine(xGap + gridY, yGap, xGap + gridY, yGap + ySize);  // y-exile
                gridY -= distance;
            }

            gridY = xSize / 2 + distance;
            while (gridY <= xSize) {
                g2.drawLine(xGap + gridY, yGap, xGap + gridY, yGap + ySize);  // y-exile
                gridY += distance;
            }
        }

        private void drawPoints(final Graphics2D g2, final Color color, final double[][] turningPoints, boolean fill) {
            g2.setColor(color);

            for (final double[] coordinate: turningPoints) {
                if (fill) {
                    g2.fillArc((int) Math.round(coordinate[0] * zoom) + 200 - 5, 200 - (int) Math.round(coordinate[1] * zoom) - 5,
                            10, 10, 0, 360);
                }
                else {
                    g2.drawArc((int) Math.round(coordinate[0] * zoom) + 200 - 5, 200 - (int) Math.round(coordinate[1] * zoom) - 5,
                            10, 10, 0, 360);
                }
            }
        }

        private void drawLine(Graphics2D g2, final Color color, final double[][] coordinates) {
            g2.setColor(color);

            if (coordinates != null) {
                double x0 = coordinates[0][0] * zoom;
                double y0 = coordinates[0][1] * zoom;

                for (int i = 1; i < coordinates.length; i++) {
                    double x1 = coordinates[i][0] * zoom;
                    double y1 = coordinates[i][1] * zoom;

                    if (!Double.isNaN(y1) && Math.abs(y1 - y0) < 200) {
                        g2.draw(new Line2D.Double(x0 + 200, 200 - y0, x1 + 200, 200 - y1));
                    }
                    x0 = x1;
                    y0 = y1;
                }
            }
        }
    }

    private void calculate(final String actionCommand) {
        String eq = controls.getEquation().trim();

        if (!eq.equalsIgnoreCase(equation)) {
            equation = eq;
            evaluator = new EquationEvaluator(eq, startX, endX, INCREMENT);

            showGraph = false;
            showFirstDeriv = false;
            showSecondDeriv = false;
            showMinMax = false;
            showInflection = false;
            showIntegration = false;
        }

        if (actionCommand.equalsIgnoreCase(ControlPanel.CMD_GRAPH)) {
            showGraph = !showGraph;
        }
        else if (actionCommand.equalsIgnoreCase(ControlPanel.CMD_DERIVATIVE_1)) {
            showFirstDeriv = !showFirstDeriv;
        }
        else if (actionCommand.equalsIgnoreCase(ControlPanel.CMD_DERIVATIVE_2)) {
            showSecondDeriv = !showSecondDeriv;
        }
        else if (actionCommand.equalsIgnoreCase(ControlPanel.CMD_MIN_MAX)) {
            showMinMax = !showMinMax;
        }
        else if (actionCommand.equalsIgnoreCase(ControlPanel.CMD_INFLEXIONS)) {
            showInflection = !showInflection;
        }
        else if (actionCommand.equalsIgnoreCase(ControlPanel.CMD_INTEGRATE)) {
            showIntegration = !showIntegration;
        }

        graph.repaint();
    }
}
