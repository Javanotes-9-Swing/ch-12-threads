package exercise6;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIApproxGUI extends JPanel implements ActionListener {

    public static void main(String[] args) {
        JFrame window = new JFrame("PI Approximator");
        PIApproxGUI content = new PIApproxGUI();
        window.setContentPane(content);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocation(1000,400);
        window.setSize(350,200);
        window.setVisible(true);
    }

    //---------------------------------------------------------------------

    private JLabel actualPI;        // Static display of value of PI.
    private JLabel currentApprox;   // Current estimate of value of PI.
    private JLabel numTrials;       // The number of times positions have been chosen.

    private JButton runTrials;      // Run trials at choosing positions in the square.

    private PIApproximator piApproximator;

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
