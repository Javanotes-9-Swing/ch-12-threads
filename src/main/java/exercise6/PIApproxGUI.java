package exercise6;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PIApproxGUI extends JPanel implements Runnable, ActionListener {

    public static final double BATCH_SIZE = 1000000;
    double estimateForPI = 0;
    long trialCount = 0;
    long inCircleCount = 0;
    private Timer timer;

    private int status;   // Controls the execution of the thread; value is one of the following constants.

    private static final int GO = 0;       // a value for status, meaning thread is to run continuously
    private static final int PAUSE = 1;    // a value for status, meaning thread should not run
    private static final int STEP = 2;     // a value for status, meaning thread should run one step then pause
    private static final int RESTART = 3;  // a value for status, meaning thread should start again from the beginning


    public static void main(String[] args) {
        JFrame window = new JFrame("PI Approximator");
        PIApproxGUI content = new PIApproxGUI();
        window.setContentPane(content);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocation(1000, 400);
        window.setSize(350, 200);
        window.setVisible(true);
    }

    //---------------------------------------------------------------------

    final static Color labelBG = Color.lightGray;  // For creating labels
    final static Color labelFG = Color.black;
    final static Font labelFont = new Font("Monospaced", Font.BOLD, 20);


    private JLabel actualPI;        // Static display of value of PI.
    private JLabel piEstimateLabel;   // Current estimate of value of PI.
    private JLabel countLabel;       // The number of times positions have been chosen.

    private JButton runPauseButton;      // Run trials at choosing positions in the square.

    private PIApproximator piApproximator;

    private final Object monitor = new Object(); // Shared monitor object for wait/notify

    public PIApproxGUI() {

        PIApproximator approximator = new PIApproximator();

        actualPI = makeLabel(" Actual value of pi: 3.141592653589793");
        piEstimateLabel = makeLabel(" Current Estimate:  0");
        countLabel = makeLabel(" Number of trials:  0");

        runPauseButton = new JButton("Run");
        runPauseButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(runPauseButton);

//        // --- Timer Setup ---
        int delay = 100; // 1000 milliseconds = 1 second
        // 'this' refers to the current class instance which implements ActionListener
        timer = new Timer(delay, this);

         /* Use a GridLayout with 4 rows and 1 column, and add all the
          components that have been created to the panel. */

        setBackground(Color.BLUE);
        setLayout(new GridLayout(4, 1, 2, 2));
        add(actualPI);
        add(piEstimateLabel);
        add(countLabel);
        add(buttonPanel);


        /* Add a blue border around the panel. */

        setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));

        new Thread(this).start();
        timer.start();

    } // end constructor

    @Override
    synchronized public void actionPerformed(ActionEvent evt) {

        Object source = evt.getSource();  // Object that generated
        //   the action event.

        if (source == runPauseButton) {
            if (status == GO) {  // Animation is running. Pause it.
                status = PAUSE;
                runPauseButton.setText("Run");
            } else {  // Animation is paused.  Start it running.
                status = GO;
                runPauseButton.setText("Pause");
            }
        }
        notify();
        piEstimateLabel.setText(" Current Estimate:  " + estimateForPI);
        countLabel.setText(" Number of trials: " + this.trialCount);
    }

//x`

    @Override
    public void run() {
        while (true) {
//            Continuously produce million round pi area trials
            runPauseButton.setText("Run");
            status = PAUSE;
            checkStatus(); // Returns only when user has clicked "Run"
//            try {
            trialGenerator();
//            } catch (IllegalStateException _) {

//            }
        }
    }

    /**
     * This method is called before starting the solution. If the status is PAUSE, it waits until
     * the status changes. When this method returns, the value of status must be RUN.
     * (Note that this method requires synchronization, since
     * otherwise calling wait() would produce an IllegalMonitorStateException.
     * However, in fact, it is only called from other synchronized methods,
     * so it would not be necessary to declare this method synchronized.
     * Any method that calls it already owns the synchronization lock.)
     */
    synchronized private void checkStatus() {
        while (status == PAUSE) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    synchronized void trialGenerator() {
//        long trialCount = 0;
//        long inCircleCount = 0;
        for (int i = 0; i < BATCH_SIZE; i++) {
            double x = Math.random();
            double y = Math.random();
            this.trialCount++;
            if (x * x + y * y < 1)
                inCircleCount++;
        }
        estimateForPI = 4 * ((double) inCircleCount / this.trialCount);
//        piEstimateLabel.setText(" Current Estimate:  " + estimateForPI);
//        countLabel.setText(" Number of trials: " + this.trialCount);
//        checkStatus();
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
}
