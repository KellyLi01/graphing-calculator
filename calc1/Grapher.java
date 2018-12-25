package calc1;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yihua
 */
public class Grapher extends JPanel{
    public int zoom, startX, startY, endX, endY;
    private final double increment;
    private OperationEvaluator e1, e2, e3, e4, e5, e6;
    private double[][] points;
    private Graphics g;
    
    public Grapher() {
        super(new GridLayout(20, 20));
        zoom = 20;
        startX = -20;
        startY = -20;
        endX = 20;
        endY = 20;
        increment = 0.05;
        points = new double[(int)((endX-startX)/increment)][2];
        //g = new Graphics(10, 0, 300, 300);
        //setBounds(100, 100, 40, 40);
        setPreferredSize(new Dimension(200, 200));
//        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(Color.WHITE);
        
        setVisible(true);
        //paintComponent(g);
    }
    
    public void enterEquation(String equation) {
        e1 = new OperationEvaluator(equation);
        points = evaluate(e1);
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        
        g2.drawLine(40, 170, 440, 170);
        g2.drawLine(240, 20, 240, 320);
        
        for (int i = 0; i < points.length; i++) {
            double x = points[i][0];
            double y = points[i][1];
            //g2.drawLine((double)startX + x + 40, (double)endY - y + 20, i, i);
            
        }
    }
    
    public void draw(Graphics g) {
        
        double xVal;
        for (xVal = startX; xVal <= endX; xVal += increment) {
            //double yVal = evaluate(e1, xVal);
        }
        g.drawLine(1, 1, 2, 2);
        
    }
    
    public double[][] evaluate(OperationEvaluator e) {
        EvaluatorNode<String> equationTree = e.getEquationTree();
        double[][] answers = new double[(int)((endX-startX)/increment)][2]; //start here we need to do a for loop
        for (int i = startX; i <= endX; i+= increment) {
            double answer = evaluate(equationTree, i);
            answers[Math.abs(Math.abs(i)-Math.abs(startX))][0] = i;
            answers[Math.abs(Math.abs(i)-Math.abs(startX))][1] = answer;
        }
        return answers;
    }
    
    public double evaluate(EvaluatorNode<String> equation, double xVal) {
        double returnValue = 0;
        if (equation.hasChildren()) {
            int index = equation.getChildren().size();
            for (int i = 0; i < index; i++) {
                EvaluatorNode<String> child = equation.getChildren().get(i);
                if (child.hasChildren()) {
                    evaluate(child, xVal);
                }
            }
        }
        
        String val = equation.getValue();
        if (val.equals("+")) {
            add(equation);
        } else if (val.equals("-")) {
            subtract(equation);
        } else if (val.equals("*")) {
            // 2x will be one variable, make sure change parser so it doesn't parse it and you eval here
            multiply(equation);
        } else if (val.equals("/")) {
            divide(equation);
        } else if (val.equals("^")) {
            exponent(equation);
        } else if (val.contains("x")) {
            subX(equation, xVal);
        }
        
        returnValue = equation.getNumVal();
        return returnValue;
    }
    
    public void subX(EvaluatorNode<String> e, double xVal) {
        String val = e.getValue();
        e.changeNumVal(xVal);
        if (val.length() != 1) {
            double num = Double.parseDouble(val.substring(0, val.indexOf("x")));
            e.changeNumVal(xVal * num);
        }
    }
    
    public void add(EvaluatorNode<String> e) {
        int index = e.getChildren().size();
        double val = 0;
        for (int i = 0; i < index; i++) {
            EvaluatorNode<String> child = e.getChildren().get(i);
            val += child.getNumVal();
        }
        e.changeValue(String.valueOf(val));
        e.changeNumVal(val);
        e.clearChildren();
    }
    
    public void subtract(EvaluatorNode<String> e) {
        int index = e.getChildren().size();
        double val = e.getChildren().get(0).getNumVal();
        for (int i = 1; i < index; i++) {
            EvaluatorNode<String> child = e.getChildren().get(i);
            val -= child.getNumVal();
        }
        e.changeValue(String.valueOf(val));
        e.changeNumVal(val);
        e.clearChildren();    
    }
    
    public void multiply(EvaluatorNode<String> e) {
        int index = e.getChildren().size();
        double val = e.getChildren().get(0).getNumVal();
        for (int i = 1; i < index; i++) {
            EvaluatorNode<String> child = e.getChildren().get(i);
            val *= child.getNumVal();
        }
        e.changeValue(String.valueOf(val));
        e.changeNumVal(val);
        e.clearChildren(); 
    }
    
    public void divide(EvaluatorNode<String> e) {
        int index = e.getChildren().size();
        double val = e.getChildren().get(0).getNumVal();
        for (int i = 1; i < index; i++) {
            EvaluatorNode<String> child = e.getChildren().get(i);
            val /= child.getNumVal();
        }
        e.changeValue(String.valueOf(val));
        e.changeNumVal(val);
        e.clearChildren(); 
    }
    
    public void exponent(EvaluatorNode<String> e) {
        int index = e.getChildren().size();
        double val = e.getChildren().get(0).getNumVal();
        double base = val;
        for (int i = 1; i < index; i++) {
            EvaluatorNode<String> child = e.getChildren().get(i);
            val = Math.pow(base, child.getNumVal());
        }
        e.changeValue(String.valueOf(val));
        e.changeNumVal(val);
        e.clearChildren(); 
    }
    public double[][] calcDeriv(double[][] points) {
        double[][] deriv = new double[2][points[0].length - 1];
        double prevX = points[0][0];
        double prevY = points[1][0];
        for (int i = 1; i < points[0].length; i++) {
            double slope = (points[1][i] - prevY)/(points[0][i] - prevX);
            deriv[0][i-1] = (points[0][i] + prevX)/2;
            deriv[1][i-1] = slope;
            prevX = points[0][i];
            prevY = points[1][i];
        }
        return deriv;
    }
}
