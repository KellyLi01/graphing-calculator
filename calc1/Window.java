package calc1;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
    private final int WIDTH, HEIGHT;
    private JFrame wholeScene;
    private Grapher graph;
    private SubWindow controls;
    public Window() {
        super();
        WIDTH = 500;
        HEIGHT = 700;
        getContentPane().setLayout(new GridLayout(2, 1));
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        JButton graphButton = new JButton("Graph");
//        graphButton.setBounds(475, 500, 75, 25);
//        /*getContentPane().*/add(graphButton/*, BorderLayout.SOUTH*/);
//        JTextField textEq = new JTextField(400);
//        textEq.setEditable(true);
//        textEq.setBackground(Color.WHITE);
//        textEq.setBounds(25, 500, 425, 25);        
//        /*getContentPane().*/add(textEq/*, BorderLayout.SOUTH*/);
        graph = new Grapher();
        graph.setPreferredSize(new Dimension(400, 400));
        graph.setMaximumSize(new Dimension(400, 400));
        graph.setMinimumSize(new Dimension(400, 400));
        // final Dimension preferredSize = new Dimension(200, 200);
        // graph.setPreferredSize(preferredSize);
        //graph.setPreferredSize(new Dimension(200, 200));
        //graph.setBounds(25, 25, 200, 200);
        getContentPane().add(graph, 0);
        controls = new SubWindow();
        getContentPane().add(controls, 1);
        // add(graph);
        setVisible(true);
        calculate();
    }
    
    public void calculate() {
        String equation = "";
        if (controls.graphButton.getModel().isPressed()) {
            equation = controls.textEq.getText();
            graph.enterEquation(equation);
        }
    }
    
    static class SubWindow extends JPanel {
        public JButton graphButton;
        public JTextField textEq;
        
        SubWindow() {
            super();
            initiate();            
        }
        void initiate() {
            GroupLayout gl = new GroupLayout(this);

            gl.setAutoCreateGaps(true);
            gl.setAutoCreateContainerGaps(true);
            
            JLabel typeEquationHere = new JLabel("Type Equation Here:");
            textEq = new JTextField(400);
            //setPreferredSize(new Dimension(200, 200));
            textEq.setEditable(true);
            textEq.setBackground(Color.WHITE);
            //textEq.setBounds(25, 500, 425, 25);
            textEq.setSize(425, 25);
            //add(textEq);
        
            graphButton = new JButton("Graph");
            //graphButton.setBounds(475, 500, 75, 25);
            graphButton.setSize(75, 25);
            //add(graphButton);
            JButton integralBtn = new JButton("Integrate");
            integralBtn.setSize(75, 25);
            JButton firstDerivBtn = new JButton("1st Derivative");
            integralBtn.setSize(75, 25);                      
            
            JLabel typeLimitsHere = new JLabel("Type Limits Here:");
            JTextField lowerLimit = new JTextField(10);
            //setPreferredSize(new Dimension(200, 200));
            lowerLimit.setEditable(true);
            lowerLimit.setBackground(Color.WHITE);
            lowerLimit.setSize(40, 25);
            JTextField upperLimit = new JTextField(10);
            //setPreferredSize(new Dimension(200, 200));
            upperLimit.setEditable(true);
            upperLimit.setBackground(Color.WHITE);
            upperLimit.setSize(40, 25);
            JButton secondDerivBtn = new JButton("2nd Derivative");
            integralBtn.setSize(75, 25);
            
            setLayout(gl);
            gl.setHorizontalGroup(
                gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(typeEquationHere)
                        .addComponent(textEq)
                        .addGroup(gl.createSequentialGroup()
                            //.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)) ****** add the lower and upper limit labels
                            .addComponent(firstDerivBtn)
                            .addComponent(secondDerivBtn)
                        )
                        //.addComponent(firstDerivBtn, GroupLayout.Alignment.TRAILING)
                        .addComponent(typeLimitsHere)
                        .addGroup(gl.createSequentialGroup()
                            .addComponent(lowerLimit)
                            .addComponent(upperLimit)
                        )
                    )
                    /*.addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        //.addComponent(typeEquationHere)
                        //.addComponent(textEq)
                        //.addComponent(firstDerivBtn) 
                        //.addComponent(firstDerivBtn, GroupLayout.Alignment.TRAILING)
                        //.addComponent(typeLimitsHere)
                        //.addComponent(upperLimit)
                    )*/
                    //.addComponent(textEq)
                    //.addComponent(graphButton)
                    
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(graphButton)
                        //.addComponent(secondDerivBtn)
                        .addComponent(integralBtn)                           
                    )
                    
            );

                        
            gl.setVerticalGroup(
                gl.createSequentialGroup()
                    .addComponent(typeEquationHere)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(textEq)
                        //.addComponent(graphButton)
                        .addComponent(graphButton))
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(firstDerivBtn)
                        //.addComponent(graphButton)
                        .addComponent(secondDerivBtn))
                    .addComponent(typeLimitsHere)
                    .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(lowerLimit, 25, 25, 25)
                        .addComponent(upperLimit, 25, 25, 25)
                        
                        .addComponent(integralBtn))
            );
        } 
    }
}
