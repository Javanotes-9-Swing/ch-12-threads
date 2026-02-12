package exercise4;

import utility.TextIO;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * <p>
 * Tasks for an ExecutorService can be represented by objects of type Callable<T>, which is a parameterized functional interface that defines the method call() with no parameters and a return type of T. A Callable represents a task that outputs a value.
 * </p>
 * <p>The interface, Future<T>, gets the result of the computation when it completes. The Callable return type of T represents a value that might not be available until some future time. The method executor.submit(c) returns a Future that represents the result of the future computation.</p>
 * <p></p>
 */
public class MostDivisorsStream {

    static void main() {
        int processors = Runtime.getRuntime().availableProcessors();
        if (processors == 1)
            System.out.println("Your computer has only 1 available processor.\n");
        else
            System.out.println("Your computer has " + processors + " available processors.\n");
        System.out.println("This program breaks up the computation into a number of tasks.");
        System.out.println("For load balancing, the number of tasks should be at least");
        System.out.println("several times the number of processors.  (Try 100 tasks.)");
        System.out.println();
        int numberOfTasks = 0;
        while (numberOfTasks < 1 || numberOfTasks > 1000) {
            System.out.print("How many tasks do you want to use  (from 1 to 1000) ?  ");
            numberOfTasks = TextIO.getlnInt();
            if (numberOfTasks < 1 || numberOfTasks > 1000)
                System.out.println("Please enter a number in the range 1 to 1000 !");
        }
        System.out.println("Choose a number that's at least 10.");
        int chosenNumber = TextIO.getlnInt();
        System.out.println("The number of divisors for 1 up to the chosen number " + chosenNumber + ", will be found.");

        ArrayList<Integer> stuff = new ArrayList<>(Arrays.asList(3, 17, 23, 90));
        stuff.parallelStream();

        countDivisorsWithStream(numberOfTasks, chosenNumber);
    }

    private static MostDivisors countDivisorsWithStream(int numberOfTasks, int theNumber) {

        long startTime = System.currentTimeMillis();

        int processors = Runtime.getRuntime().availableProcessors();

        /* An ArrayList used to store the Futures that are created when the tasks
         * are submitted to the ExecutorService. */
        ArrayList<MostDivisors> results = new ArrayList<>();

        int theChunk = theNumber / numberOfTasks;
        int min = 1;
        int max = theChunk;

        for (int i = 1; i <= numberOfTasks; i++) {
            if (i == numberOfTasks && max < theNumber) {
                min = max + 1;
                max = theNumber;
            }

            long elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println(min + " is the number of divisors.");
            System.out.println(max + " is the number with most divisors.");
            System.out.println("\nTotal elapsed time:  " + (elapsedTime / 1000.0) + " seconds.\n");

        }
    }
        /**
         * An object belonging to this class will count divisors for each integer in a specified range of integers.  The range is from min to max, inclusive, where min and max are given as parameters to the constructor.  The counting is done in the call() method, which returns the number of primes that were found.
         */
        class MostDivisors {
            int min, max;

            int maxDiv = 0;
            long theNumDivided = 0;

            public MostDivisors(int min, int max) {
                this.min = min;
                this.max = max;
            }

            public MostDivisors(int maxDiv, long theNumDivided) {
                this.maxDiv = maxDiv;
                this.theNumDivided = theNumDivided;
            }
        }

        /**
         * Count the divisors between min and max, inclusive.
         */
        MostDivisors calculateDivisorsOfNum ( int min, int max){

            // loop from min to max getting each int
            int maxDivs = Integer.MIN_VALUE;
            long theNum = 0;
            for (int i = min; i <= max; i++) {
                // find the num divisors of each int
                int divCount = 0;
                for (int j = 1; j <= Math.sqrt(i); j++) {
                    if (i % j == 0) {
                        divCount++;
                        if (j != i / j) {
                            divCount++;
                        }
                    }
                }
                if (divCount > maxDivs) {
                    maxDivs = divCount;
                    theNum = i;
                }
            }
            // keep track of max divisors
            return new MostDivisors(maxDivs, theNum);
        }
    }
}

