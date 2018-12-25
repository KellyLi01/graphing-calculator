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
public class EvaluatorNode<String> {
    private List<EvaluatorNode<String>> children;
    private EvaluatorNode<String> parent;
    private String value;
    private double valueint;
    public EvaluatorNode() { // to add children together, use recursion
        children = new ArrayList<>();
        parent = null;
        value = null;
        valueint = 0;
    }
    
    public EvaluatorNode(String equation) { // to add children together, use recursion
        children = new ArrayList<>();
        parent = null;
        value = equation;
        valueint = 0;
    }
    
    public EvaluatorNode(String equation, EvaluatorNode<String> p) { // to add children together, use recursion
        children = new ArrayList<>();
        parent = p;
        value = equation;
        valueint = 0;
    }
    
    public void addChild(EvaluatorNode<String> c) {
        children.add(c);
        c.setParent(this);
    }
    
    public void addChildren(List<EvaluatorNode<String>> c) {
        for(int i = 0; i < c.size(); i++) {
            addChild(c.get(i));
        }
    }

    public void addChild(EvaluatorNode<String> c, int index) {
        children.add(index, c);
        c.setParent(this);
    }
    public List<EvaluatorNode<String>> getChildren() {
        return children;
    }
    
    public void setParent(EvaluatorNode<String> p) {
        parent = p;
    }
    
    public String getValue() {
        return value;
    }
    
    public double getNumVal() {
        return valueint;
    }
    
    public void changeValue(String s) {
        value = s;
    }
    
    public void changeNumVal(double d) {
        valueint = d;
    }
    
    public boolean hasChildren() {
        return children != null;
    }
    
    public void clearChildren() {
        children = new ArrayList<>();
    }
}
