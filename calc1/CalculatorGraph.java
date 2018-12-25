package calc1;


import javax.swing.JFrame;
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
public class CalculatorGraph extends JPanel{ // DO NOT USE
    private Grapher myGrapher = null;        //add this line
    private JPanel contentPane;
    public CalculatorGraph() {
        initialize();
    }
    public void initialize(){
        contentPane = new JPanel();
    }
}
