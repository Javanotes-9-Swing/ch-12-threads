package exercise6;

import javax.swing.*;
import java.awt.*;
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

    final static Color labelBG = new Color(240,225,200);  // For creating labels
    final static Color labelFG = new Color(180,0,0);
    final static Font labelFont = new Font("Monospaced", Font.PLAIN, 12);


    private JLabel actualPI;        // Static display of value of PI.
    private JLabel piEstimateLabel;   // Current estimate of value of PI.
    private JLabel countLabel;       // The number of times positions have been chosen.

    private JButton runTrials;      // Run trials at choosing positions in the square.

    private PIApproximator piApproximator;

    public PIApproxGUI() {

        PIApproximator approximator = new PIApproximator();

        piEstimateLabel =   makeLabel(" Current Estimate:  0");

         /* Use a GridLayout with 4 rows and 1 column, and add all the
          components that have been created to the panel. */

        setBackground(Color.BLUE);
        setLayout(new GridLayout(4,1,2,2));
        add(piEstimateLabel);


    } // end constructor

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    /**
     * A utility routine for creating the labels that are used
     * for display.  This routine is called by the constructor.
     * @param text The text to show on the label.
     */
    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setBackground(labelBG);
        label.setForeground(labelFG);
        label.setFont(labelFont);
        label.setOpaque(true);
        return label;
    }
}
