package exercise4;

import java.util.concurrent.Callable;

/**
 * <p>
 * Tasks for an ExecutorService can be represented by objects of type Callable<T>, which is a parameterized functional interface that defines the method call() with no parameters and a return type of T. A Callable represents a task that outputs a value.
 * </p>
 * <p>The interface, Future<T>, gets the result of the computation when it completes. The Callable return type of T represents a value that might not be available until some future time. The method executor.submit(c) returns a Future that represents the result of the future computation.</p>
 * <p></p>
 */
public class MostDivisorsExecutor {

    /**
     * An object belonging to this class will count divisors for each integer in a specified range of integers.  The range is from min to max, inclusive, where min and max are given as parameters to the constructor.  The counting is done in the call() method, which returns the number of primes that were found.
     */
    private static class MostDivisors implements Callable<MostDivisors> {
        int min, max;

        public MostDivisors(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public MostDivisors call() {
            MostDivisors md = calculateDivisorsOfNum(min, max);
            return md;
        }

        /**
         * Count the divisors between min and max, inclusive.
         */
        private MostDivisors calculateDivisorsOfNum(int min, int max) {
            return null;
        }
    }
}
