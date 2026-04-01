package exercise6;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SwingTimerWaitNotifyDemo implements ActionListener, Runnable {
    private final JLabel dataLabel = new JLabel("Initial Data: None");
    private final Object monitor = new Object(); // Shared monitor object for wait/notify
    private volatile String sharedData = null;
    private final Timer swingTimer;

    public SwingTimerWaitNotifyDemo() {
        // Swing Timer runs on the EDT and checks the data condition periodically
        swingTimer = new Timer(500, this); // Fires every 500 milliseconds
        
        // Setup GUI
        JFrame frame = new JFrame("Swing Wait/Notify Demo");
        frame.add(dataLabel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // Start the background thread for data generation
        new Thread(this).start();
        swingTimer.start();
    }

    public static void main(String[] args) {
        // Ensure GUI creation is on the EDT
        SwingUtilities.invokeLater(SwingTimerWaitNotifyDemo::new);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // This method runs on the EDT
        String dataToDisplay = null;
        synchronized (monitor) {
            if (sharedData != null) {
                dataToDisplay = sharedData;
                sharedData = null; // Clear data after retrieval
                // Notify the background thread that data was consumed and it can proceed
                monitor.notify(); 
            }
        }

        if (dataToDisplay != null) {
            dataLabel.setText("Updated Data: " + dataToDisplay); // Safely update GUI on EDT
        }
    }

    @Override
    public void run() {
        // This method runs on a separate background thread
        int count = 0;
        try {
            while (true) {
                // Simulate data generation
                final String newData = "Data " + count++;
                
                synchronized (monitor) {
                    // Wait if the previous data hasn't been consumed by the EDT yet
                    while (sharedData != null) {
                        monitor.wait();
                    }
                    sharedData = newData; // Place new data in the shared variable
                    // Notify the EDT (via the Timer's action listener) that new data is ready
                    // Note: The timer will check the data at its next tick.
                }

                // Simulate processing time
                Thread.sleep(2000); // Sleep for 2 seconds to simulate work
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
