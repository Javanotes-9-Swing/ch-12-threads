package exercise6;

public class PIApproximator {

    private static final int BATCH_SIZE = 1000;

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
        return 4 * ((double) inCircleCount / trialCount);
    }

//    countLabel.setText(      " Number of Trials:   " + trialCount);
//    piEstimateLabel.setText( " Current Estimate:   " + estimateForPi);
    }
