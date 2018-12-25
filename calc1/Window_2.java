package calc1;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
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
public class Window_2 extends JFrame {
    private final int WIDTH, HEIGHT;
    private JFrame wholeScene;
    public Window_2() {
        super();
        WIDTH = 600;
        HEIGHT = 700;
//        getContentPane().setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JButton graphButton = new JButton("Graph");
        graphButton.setBounds(475, 500, 75, 25);
        /*getContentPane().*/add(graphButton/*, BorderLayout.SOUTH*/);
        JTextField textEq = new JTextField(400);
        textEq.setEditable(true);
        textEq.setBackground(Color.WHITE);
        textEq.setBounds(25, 500, 425, 25);        
        /*getContentPane().*/add(textEq/*, BorderLayout.SOUTH*/);
        Grapher graph = new Grapher();
        // final Dimension preferredSize = new Dimension(200, 200);
        // graph.setPreferredSize(preferredSize);
        //graph.setPreferredSize(new Dimension(200, 200));
        //graph.setBounds(25, 25, 200, 200);
        /*getContentPane().*/add(graph/*, BorderLayout.CENTER*/);
        // add(graph);
        setVisible(true);
    }
}
