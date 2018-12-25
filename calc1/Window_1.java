package calc1;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JFrame;
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
public class Window_1 extends JFrame {
    private final int WIDTH, HEIGHT;
    private JFrame wholeScene;
    public Window_1() {
        super();
        WIDTH = 600;
        HEIGHT = 700;
        getContentPane().setLayout(new BorderLayout());
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        
        Grapher graph = new Grapher();
        // final Dimension preferredSize = new Dimension(200, 200);
        // graph.setPreferredSize(preferredSize);
        //graph.setPreferredSize(new Dimension(200, 200));
        //graph.setBounds(25, 25, 200, 200);
        getContentPane().add(BorderLayout.CENTER, graph);
        // add(graph);
//        JTextField textEq = new JTextField(400);
//        textEq.setEditable(true);
//        textEq.setBackground(Color.WHITE);
//        textEq.setBounds(25, 500, 425, 25);        
//        getContentPane().add(BorderLayout.NORTH, textEq);
//        
//         JButton graphButton = new JButton("Graph");
//        graphButton.setBounds(475, 500, 75, 25);
//        getContentPane().add(BorderLayout.SOUTH, graphButton);

        SubWindow command = new SubWindow();
        getContentPane().add(BorderLayout.SOUTH, command);
        
        setVisible(true);
    }
   
    static class SubWindow extends JPanel {
        SubWindow() {
            super();
            
            JTextField textEq = new JTextField(400);
            setPreferredSize(new Dimension(200, 200));
            textEq.setEditable(true);
            textEq.setBackground(Color.WHITE);
            textEq.setBounds(25, 500, 425, 25);        
            add(textEq);
        
            JButton graphButton = new JButton("Graph");
            graphButton.setBounds(475, 500, 75, 25);
            add(graphButton);
        }
    }
    
}
