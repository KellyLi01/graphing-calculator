package calculator;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author kelly.li
 */

public class ControlPanel extends JPanel {
    private JTextField textEq;
    private JTextField lowerLimitField;
    private JTextField upperLimitField;
    private JLabel integrationResult;

    public static final String CMD_GRAPH = "Graph";
    public static final String CMD_DERIVATIVE_1 = "1st Derivative";
    public static final String CMD_DERIVATIVE_2 = "2nd Derivative";
    public static final String CMD_MIN_MAX = "Relative Extrema";
    public static final String CMD_INFLEXIONS = "Points of Inflection";
    public static final String CMD_INTEGRATE = "Integrate";

    ControlPanel(ActionListener action) {
        super();
        initiate(action);
    }

    void initiate(ActionListener action) {
        GroupLayout gl = new GroupLayout(this);

        gl.setAutoCreateGaps(true);
        gl.setAutoCreateContainerGaps(true);

        JLabel typeEquationHere = new JLabel("Type Equation Here:");
        textEq = new JTextField(400);
        textEq.setEditable(true);
        textEq.setBackground(Color.WHITE);
        textEq.setSize(425, 25);

        JButton graphButton = new JButton(CMD_GRAPH);
        graphButton.setSize(50, 25);
        graphButton.addActionListener(action);

        JButton firstDerivBtn = new JButton(CMD_DERIVATIVE_1);
        firstDerivBtn.addActionListener(action);
        firstDerivBtn.setSize(60, 25);

        JButton secondDerivBtn = new JButton(CMD_DERIVATIVE_2);
        secondDerivBtn.addActionListener(action);
        secondDerivBtn.setSize(60, 25);

        JButton integralBtn = new JButton(CMD_INTEGRATE);
        integralBtn.addActionListener(action);
        integralBtn.setSize(40, 25);

        JButton minMaxBtn = new JButton(CMD_MIN_MAX);
        minMaxBtn.setSize(60, 25);
        minMaxBtn.addActionListener(action);

        JButton inflexionBtn = new JButton(CMD_INFLEXIONS);
        inflexionBtn.setSize(60, 25);
        inflexionBtn.addActionListener(action);

        JLabel typeLimitsHere = new JLabel("Type Limits Here:");
        lowerLimitField = new JTextField(10);
        lowerLimitField.setEditable(true);
        lowerLimitField.setBackground(Color.WHITE);
        lowerLimitField.setSize(40, 25);
        upperLimitField = new JTextField(10);
        upperLimitField.setEditable(true);
        upperLimitField.setBackground(Color.WHITE);
        upperLimitField.setSize(40, 25);

        integrationResult = new JLabel("Integration Result");

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
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(minMaxBtn)
                                        .addComponent(inflexionBtn)
                                )
                                //.addComponent(firstDerivBtn, GroupLayout.Alignment.TRAILING)
                                .addComponent(typeLimitsHere)
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(lowerLimitField)
                                        .addComponent(upperLimitField)
                                )
                                .addGroup(gl.createSequentialGroup()
                                        .addComponent(integrationResult)
                                )
                        )

                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(graphButton)
                                .addComponent(integralBtn)
                        )


        );

        gl.setVerticalGroup(
                gl.createSequentialGroup()
                        .addComponent(typeEquationHere)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(textEq)
                                .addComponent(graphButton))
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(firstDerivBtn)
                                .addComponent(secondDerivBtn)
                        )
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(minMaxBtn)
                                .addComponent(inflexionBtn)
                        )
                        .addComponent(typeLimitsHere)
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(lowerLimitField, 25, 25, 25)
                                .addComponent(upperLimitField, 25, 25, 25)

                                .addComponent(integralBtn))
                        .addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(integrationResult))
        );
    }

    public String getEquation() {
        return textEq.getText();
    }

    public double getLowerLimit() {
        final String str = lowerLimitField.getText();
        if (str == null || str.isEmpty()) {
            return 0.0;
        }

        return Double.parseDouble(str);
    }

    public double getUpperLimit() {
        final String str = upperLimitField.getText();
        if (str == null || str.isEmpty()) {
            return 0.0;
        }

        return Double.parseDouble(str);
    }

    public void showIntegrationResult(final double integration) {
        integrationResult.setText("Integration Result: " + String.format("%.2f", integration));
    }
}
