package calc1;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yihua
 */
import javax.swing.JFrame;

public class CalculatorPart {
    public static void main(String[] args){
//        BufferedReader br = new BufferedReader(new FileReader("file.in"));
//        String equation = br.readLine();
//        StringTokenizer st = new StringTokenizer (equation);
        Window win = new Window();
        
    }
    
    public boolean isRationalExpression(String equation) {
        return (equation.contains("/"));
    }
    
    public boolean isLogFunction(String equation) {
        return (equation.contains("log")) || (equation.contains("ln"));
    }
    
    
}
