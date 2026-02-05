package exercise4;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 * <p>
 * Tasks for an ExecutorService can be represented by objects of type Callable<T>, which is a parameterized functional interface that defines the method call() with no parameters and a return type of T. A Callable represents a task that outputs a value.
 * </p>
 * <p>The interface, Future<T>, gets the result of the computation when it completes. The Callable return type of T represents a value that might not be available until some future time. The method executor.submit(c) returns a Future that represents the result of the future computation.</p>
 * <p></p>
 */
public class MostDivisorsExecutor {

    static void main() {
        int numberOfTasks = 100;
        countDivisorsWithExecutor(numberOfTasks, 100000);
    }

    private static void countDivisorsWithExecutor(int numberOfTasks, int theNumber) {
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(processors);

        /* An ArrayList used to store the Futures that are created when the tasks
         * are submitted to the ExecutorService. */
        ArrayList<Future<MostDivisors>> results = new ArrayList<>();

        int theChunk = theNumber/numberOfTasks;
        int min = 1;
        int max = theChunk;

        for (int i = 1; i <= numberOfTasks; i++) {
            MostDivisors aTask = new MostDivisors(min, max);
            Future<MostDivisors> aResult = executor.submit(aTask);
            results.add(aResult); // Save the Future representing the (future) result.
            min = max + 1;
            max = theChunk * i;
        }

        int maxMax = 0;
        int maxMaxNum =0;
        for (Future<MostDivisors> res : results) {
            try {
                int maxDiv = res.get().maxDiv;
                int theNum = res.get().theNumDivided;
                if (maxDiv > maxMax) {
                    maxMaxNum = theNum;
                    maxMax = maxDiv;
                }
            } catch (Exception e) {
                // Should not occur in this program.  An exception can
                // be thrown if the task was canceled, if an exception
                // occurred while the task was computing, or if the
                // thread that is waiting on get() is interrupted.
                System.out.println("Error occurred while computing: " + e);
            }
        }
        System.out.println(maxMax + " is the number of divisors.");
        System.out.println(maxMaxNum + " is the number with most divisors.");

        executor.shutdown();

    }

    /**
     * An object belonging to this class will count divisors for each integer in a specified range of integers.  The range is from min to max, inclusive, where min and max are given as parameters to the constructor.  The counting is done in the call() method, which returns the number of primes that were found.
     */
    private static class MostDivisors implements Callable<MostDivisors> {
        int min, max;

        int maxDiv = 0;
        int theNumDivided = 0;

        public MostDivisors(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public MostDivisors(short maxDiv, short theNumDivided) {
            this.maxDiv = maxDiv;
            this.theNumDivided = theNumDivided;
        }

        @Override
        public MostDivisors call() {
            return calculateDivisorsOfNum(min, max);
        }

        /**
         * Count the divisors between min and max, inclusive.
         */
        private MostDivisors calculateDivisorsOfNum(int min, int max) {

            // loop from min to max getting each int
            short maxDivs = (short) Integer.MIN_VALUE;
            short theNum = 0;
            for (int i = min; i <= max; i++) {
                // find the num divisors of each int
                short divCount = 0;
                for (int j = 1; j <= i; j++) {
                    if (i % j == 0) {
                        divCount++;
                    }
                }
                if (divCount > maxDivs) {
                    maxDivs = divCount;
                    theNum = (short) i;
                }
            }
            // keep track of max divisors
            return new MostDivisors(maxDivs, theNum);
        }
    }
}
