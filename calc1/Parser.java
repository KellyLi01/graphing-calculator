/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calc1;

/**
 *
 * @author yihua
 */
public class Parser {
    private EvaluatorNode<String> e;
    private double xValue;
    public Parser(EvaluatorNode e) {
        this.e = e;
    }
    
    public void parse() {
        stringToDouble(e);
    }
    
    public void stringToDouble(EvaluatorNode<String> en) {
        String value = en.getValue();
        if (value.contains("0") || value.contains("1") || value.contains("2") ||
                value.contains("3") || value.contains("4") || value.contains("5") ||
                value.contains("6") || value.contains("7") || value.contains("8") || value.contains("9")) {
            double numVal = Double.parseDouble(value);
            en.changeNumVal(numVal);
        }
        if (en.hasChildren()) {
            for (int i = 0; i < en.getChildren().size(); i++) {
                stringToDouble(en.getChildren().get(i));
            }
        }
    }
}
