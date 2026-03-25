package exercise6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIApproxGUI extends JPanel implements Runnable, ActionListener {

    public static final double BATCH_SIZE = 1000000;
    double estimateForPI = 0;
    private Timer timer;

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

    final static Color labelBG = Color.lightGray;  // For creating labels
    final static Color labelFG = Color.black;
    final static Font labelFont = new Font("Monospaced", Font.BOLD, 20);


    private JLabel actualPI;        // Static display of value of PI.
    private JLabel piEstimateLabel;   // Current estimate of value of PI.
    private JLabel countLabel;       // The number of times positions have been chosen.

    private JButton trialsButton;      // Run trials at choosing positions in the square.

    private PIApproximator piApproximator;

    public PIApproxGUI() {

        PIApproximator approximator = new PIApproximator();

        actualPI = makeLabel(" Actual value of pi: 3.141592653589793");
        piEstimateLabel =   makeLabel(" Current Estimate:  0");
        countLabel = makeLabel(" Number of trials:  0");

        trialsButton = new JButton("Run");
        trialsButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(trialsButton);

        // --- Timer Setup ---
        int delay = 1000; // 1000 milliseconds = 1 second
        // 'this' refers to the current class instance which implements ActionListener
        timer = new Timer(delay, this);
        timer.start();

         /* Use a GridLayout with 4 rows and 1 column, and add all the
          components that have been created to the panel. */

        setBackground(Color.BLUE);
        setLayout(new GridLayout(4,1,2,2));
        add(actualPI);
        add(piEstimateLabel);
        add(countLabel);
        add(buttonPanel);


        /* Add a blue border around the panel. */

        setBorder( BorderFactory.createLineBorder(Color.BLUE, 2) );

        new Thread(this).start();

    } // end constructor

    @Override
    public void actionPerformed(ActionEvent e) {

//        if (secondsElapsed >= 10) {
//            timer.stop();
//            timeLabel.setText("Time: " + secondsElapsed + " seconds. Stopped.");
//        }
        piEstimateLabel.setText(" Current Estimate:  " + estimateForPI);
    }

//x`

    @Override
    public void run() {
        while (true) {
//            Continuously produce million round pi area trials
            System.out.println(trialGenerator());
            trialsButton.repaint();
        }
    }

    double trialGenerator() {
        long trialCount = 0;
        long inCircleCount = 0;
        for (int i = 0; i < BATCH_SIZE; i++) {
            double x = Math.random();
            double y = Math.random();
            trialCount++;
            if (x * x + y * y < 1)
                inCircleCount++;
        }
        estimateForPI = 4 * ((double) inCircleCount / trialCount);
        return estimateForPI;
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
