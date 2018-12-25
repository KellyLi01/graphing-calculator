package calc1;


import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yihua
 */
public class OperationEvaluator { // traverse the tree through recursion
    private String equation;
    private EvaluatorNode<String> equationTree;
    private Parser p;
    public OperationEvaluator(String eq) {
        equation = eq;
        equationTree = new EvaluatorNode(equation);
        p = new Parser(equationTree);
        orderOfOperations();
    }
    public void orderOfOperations() {
        List<int[]> parentheses = findTheParentheses(equationTree.getValue(), 0);
        nodeThePlusSigns(equationTree, parentheses);
        /*nodeTheMinusSigns(equationTree);
        nodeTheMultiplySigns(equationTree);
        nodeTheDivideSigns(equationTree);  
        nodeTheExponentSigns(equationTree);
        nodeTheParentheses(equationTree, parentheses);*/
        p.parse();
        
    }
    
    public EvaluatorNode<String> getEquationTree() {
        return equationTree;
    }
    
    /*public void nodeTheParentheses(EvaluatorNode<String> en) { // this will be a while loop
        String equation = en.getValue();
        int leftParIndex = -1;
        int rightParIndex = -1;
        int parCount = -1;
        for (int i = 0; i < equation.length(); i++) {
            String temp = equation.substring(i, i+1);
            if (temp.equals('(')) {
                if (leftParIndex == -1) {
                    leftParIndex = i;
                    parCount = 1;
                } else {
                    parCount++;
                }
            } else if (temp.equals(')')) {
                if (rightParIndex < i) {
                    rightParIndex = i;
                }
                parCount--;
            }
            if (parCount == 0) {
                break;
            }
        }
        if (leftParIndex == 0 && rightParIndex == equation.length()-1) {
            en.changeValue(equation.substring(1, rightParIndex-1));
            nodeTheParentheses(en);
        } else if (leftParIndex != -1) {
            EvaluatorNode<String> parenth;
            if (leftParIndex <= equation.length() - rightParIndex) {
                parenth = new EvaluatorNode(equation.substring(0, rightParIndex+1));
                en.changeValue(equation.substring(rightParIndex+1));
            } else {
                parenth = new EvaluatorNode(equation.substring(leftParIndex, equation.length()));
                en.changeValue(equation.substring(0, leftParIndex));
            }
            parenth.setParent(en);
            en.addChild(parenth);
            nodeTheParentheses(parenth);
            nodeTheParentheses(en);
        } else {
            return;
        }
    }*/
    
    public void nodeTheParentheses(EvaluatorNode<String> en) { // this will be a while loop
        String equation = en.getValue();
        if (equation.indexOf('(') > -1) {
            en.changeValue(equation.substring(1, equation.length()-1));
            nodeThePlusSigns(en, findTheParentheses(en.getValue(), 0));
        }
    }
    
    public List<int[]> findTheParentheses(String en, int startIndex) { // this will be a while loop
        String equation = en;
        List<int[]> parenthPairs = new ArrayList<>();
        int[] pair = new int[2];
        int leftParIndex = equation.indexOf('(', startIndex);
        if (leftParIndex == -1) {
            return parenthPairs;
        } else {
            pair[0] = leftParIndex;
        }
        int rightParIndex = -1;
        int parCount = 1;
        for (int i = leftParIndex+1; i < equation.length(); i++) {
            String temp = equation.substring(i, i+1);
            if (temp.equals('(')) {
                parCount++;
            } else if (temp.equals(')')) {
                parCount--;
            }
            if (parCount == 0) {
                rightParIndex = i;
                pair[1] = i;
                break;
            }
        }
        parenthPairs = findTheParentheses(en, rightParIndex+1);
        parenthPairs.add(pair);
        return parenthPairs;
    }
    
    public void nodeThePlusSigns(EvaluatorNode<String> en, List<int[]> parentheses) {// shouldn't only take in en.getValue but all of its kids too
        // should skip over the pts w the parentheses
        String eq = en.getValue();
        int plusIndex = eq.indexOf("+");// if the val is bw some nums, change it
        if (plusIndex == -1) {
            return;
        }
        String left = eq.substring(0, plusIndex);
        String right = eq.substring(plusIndex, eq.length());        
        en.changeValue("+");
        EvaluatorNode<String> leftSide = new EvaluatorNode(left);
        EvaluatorNode<String> rightSide = new EvaluatorNode(right);
        if (plusIndex > 0) {
            leftSide.setParent(en);
            en.addChild(leftSide, 0);
            nodeThePlusSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            nodeTheMinusSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            rightSide.setParent(en);
            en.addChild(rightSide);
            nodeThePlusSigns(rightSide, findTheParentheses(rightSide.getValue(), 0));
        } else {
            nodeTheMinusSigns(en, findTheParentheses(en.getValue(), 0));
        }
    }
    
    public void nodeTheMinusSigns(EvaluatorNode<String> en, List<int[]> parentheses) {
        String eq = en.getValue();
        int minusIndex = eq.indexOf("-");
        if (minusIndex == -1) {
            return;
        }
        String left = eq.substring(0, minusIndex);
        String right = eq.substring(minusIndex, eq.length());
        en.changeValue("-");
        EvaluatorNode<String> leftSide = new EvaluatorNode(left);
        EvaluatorNode<String> rightSide = new EvaluatorNode(right);
        if (minusIndex > 0) {
            leftSide.setParent(en);
            en.addChild(leftSide, 0);
            nodeTheMinusSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            nodeTheMultiplySigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            rightSide.setParent(en);
            en.addChild(rightSide);
            nodeTheMinusSigns(rightSide, findTheParentheses(rightSide.getValue(), 0));
        } else {
            nodeTheMultiplySigns(en, findTheParentheses(en.getValue(), 0));
        }
    }
    
    public void nodeTheMultiplySigns(EvaluatorNode<String> en, List<int[]> parentheses) {
        String eq = en.getValue();
        int multiplyIndex = eq.indexOf("*");
        if (multiplyIndex == -1) {
            return;
        }
        String left = eq.substring(0, multiplyIndex);
        String right = eq.substring(multiplyIndex, eq.length());
        en.changeValue("*");
        EvaluatorNode<String> leftSide = new EvaluatorNode(left);
        EvaluatorNode<String> rightSide = new EvaluatorNode(right);
        if (multiplyIndex > 0) {
            leftSide.setParent(en);
            en.addChild(leftSide, 0);
            nodeTheMultiplySigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            nodeTheDivideSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            rightSide.setParent(en);
            en.addChild(rightSide);
            nodeTheMultiplySigns(rightSide, findTheParentheses(rightSide.getValue(), 0));
        } else {
            nodeTheDivideSigns(en, findTheParentheses(en.getValue(), 0));
        }
    }
    
    public void nodeTheDivideSigns(EvaluatorNode<String> en, List<int[]> parentheses) {
        String eq = en.getValue();
        int divideIndex = eq.indexOf("/");
        if (divideIndex == -1) {
            return;
        }
        String left = eq.substring(0, divideIndex);
        String right = eq.substring(divideIndex, eq.length());
        en.changeValue("/");
        EvaluatorNode<String> leftSide = new EvaluatorNode(left);
        EvaluatorNode<String> rightSide = new EvaluatorNode(right);
        if (divideIndex > 0) {
            leftSide.setParent(en);
            en.addChild(leftSide, 0);
            nodeTheDivideSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            nodeTheExponentSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            rightSide.setParent(en);
            en.addChild(rightSide);
            nodeTheDivideSigns(rightSide, findTheParentheses(rightSide.getValue(), 0));
        } else {
            nodeTheExponentSigns(en, findTheParentheses(en.getValue(), 0));
        }
    }    
    public void nodeTheExponentSigns(EvaluatorNode<String> en, List<int[]> parentheses) {
        String eq = en.getValue();
        int exponentIndex = eq.indexOf("^");
        if (exponentIndex == -1) {
            return;
        }
        String left = eq.substring(0, exponentIndex);
        String right = eq.substring(exponentIndex, eq.length());
        en.changeValue("^");
        EvaluatorNode<String> leftSide = new EvaluatorNode(left);
        EvaluatorNode<String> rightSide = new EvaluatorNode(right);
        if (exponentIndex > 0) {
            leftSide.setParent(en);
            en.addChild(leftSide, 0);
            nodeTheExponentSigns(leftSide, findTheParentheses(leftSide.getValue(), 0));
            nodeTheParentheses(leftSide);
            rightSide.setParent(en);
            en.addChild(rightSide);
            nodeTheExponentSigns(rightSide, findTheParentheses(rightSide.getValue(), 0));
        } else {
            nodeTheParentheses(en);
        }
    }    
}
