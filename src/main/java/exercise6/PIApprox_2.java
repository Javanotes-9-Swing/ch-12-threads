package exercise6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This program uses a probabilistic technique to estimate the
 * value of the mathematical constant pi.  The technique is to
 * choose random numbers x and y in the range 0 to 1, and to
 * compute x*x + y*y.  The probability that x*x + y*y is less than
 * 1 is pi/4.  If many trials are performed, and the number of
 * trials in which x*x+y*y is less than 1 is divided by the total
 * number of trials, the result is an approximation for pi/4.
 * Multiplying this by 4 gives an approximation for pi.
 * <p>
 * The program shows the estimate produced by this procedure, along
 * with the number of trials that have been done and, for comparison,
 * the actual value of pi.  These values are shown in three JLabels.
 * The computation is done by a separate thread that updates the
 * contents of the labels after every millionth trial.
 * <p>
 * In this version of the program, the computation thread runs
 * continually from the time the program is started until it
 * ends.  It is run at a reduced priority so that it does not
 * interfere with the GUI thread.
 */
public class PIApprox_2 extends JPanel implements ActionListener {

    public static final int BATCH_SIZE = 1000000;

    public static void main(String[] args) {
        JFrame window = new JFrame("PI Approximator");
        PIApprox_2 content = new PIApprox_2();
        window.setContentPane(content);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocation(1000, 400);
        window.setSize(350, 200);
        window.setVisible(true);
    }

    final static Color labelBG = Color.lightGray;  // For creating labels
    final static Color labelFG = Color.black;
    final static Font labelFont = new Font("Monospaced", Font.BOLD, 20);


    private final JLabel piEstimateLabel;   // Current estimate of value of PI.
    private final JLabel countLabel;       // The number of times positions have been

    private final JButton runPauseButton;      // Run trials at choosing positions in the square.

    private volatile boolean running; // This control whether the computation thread is paused.

     final ComputationThread runner;

    public PIApprox_2() {

        // Static display of value of PI.
        JLabel actualPI = makeLabel(" Actual value of pi: 3.141592653589793");
        piEstimateLabel = makeLabel(" Current Estimate:  0");
        countLabel = makeLabel(" Number of trials:  0");

        runPauseButton = new JButton("Run");
        runPauseButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(runPauseButton);

        setBackground(Color.BLUE);
        setLayout(new GridLayout(4, 1, 2, 2));
        add(actualPI);
        add(piEstimateLabel);
        add(countLabel);
        add(buttonPanel);

        runner = new ComputationThread();
        runner.start();
    }

    /**
     * A utility routine for creating the labels that are used
     * for display.  This routine is called by the constructor.
     *
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

    @Override
    public void actionPerformed(ActionEvent event) {

        // if running when button event happens, then pause
        if (running) {
            runPauseButton.setText("Run");
            running = false;
        } else {
            runPauseButton.setText("Pause");
            synchronized (runner) {
                running = true;
                runner.notify();
            }
        }
    }

    /**
     * This class defines the thread that does the computation.
     * The thread runs in an infinite loop in which it performs
     * batches of 1000000 trials and then updates the display labels.
     */
    private class ComputationThread extends Thread {
        long trialCount;
        long inCircleCount;

        public ComputationThread() {
            setDaemon(true);
            setPriority(Thread.currentThread().getPriority() - 1);
        }

        @Override
        public void run() {
            super.run();
            while (true) {
                synchronized (this) {
                    while (!running) {
                        try {
                            runner.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                trialGenerator();
            }
        }

        private void trialGenerator() {
                for (int i = 0; i < BATCH_SIZE; i++) {
                    double x = Math.random();
                    double y = Math.random();
                    trialCount++;
                    if (x * x + y * y < 1)
                        inCircleCount++;
                }
                double estimateForPI = 4 * ((double) inCircleCount / trialCount);
                countLabel.setText(" Number of trials: " + trialCount);
                piEstimateLabel.setText(" Current Estimate:  " + estimateForPI);
            }
    }
}
