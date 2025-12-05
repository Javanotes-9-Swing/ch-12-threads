package exercise2;

import utility.TextIO;

public class MostDivisorsThreads {

	/**
	 *  Some number found in a range has the largest number of possible divisors
	 */
	static int largestNumDivs;

	/**The number that has the greatest number of divisors*/
	static int maxNum;

	/**
	 * A Thread belonging to this class will count divisors in a specified range
	 * of integers.  The range is from min to max, inclusive, where min and max
	 * are given as parameters to the constructor.  After counting, the thread
	 * outputs a message about the number of divisors that it has found.
	 */
	private class CountDivisorsThread extends Thread {
		MostDivisors mostDivisors;
		int min, max;
		public CountDivisorsThread(int min, int max) {
			this.min = min;
			this.max = max;
		}
		public void run() {
			mostDivisors = calculateNumAndDivisors(min, max);
			int maxDivisors = mostDivisors.getMaxDivisors();
			if (maxDivisors > largestNumDivs) {
				largestNumDivs = maxDivisors;
				maxNum = mostDivisors.getNumWithMax();
			}

		}
	}

	/**
	 * Counts how many times  using a specified number
	 * of threads.  The total elapsed time is printed.
	 * @param numberOfThreads
	 */
	private void incrementWithThreads(int numberOfThreads, int range) {
		int increment = range/numberOfThreads;
		System.out.println("\nCounting divisors between " + (1) + " and "
				+ range + " using " + numberOfThreads + " threads...\n");
		CountDivisorsThread[] worker = new CountDivisorsThread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			worker[i] = new CountDivisorsThread((increment * i) + 1, increment * (i + 1) );
		}

		for (int i = 0; i < numberOfThreads; i++) {
			worker[i].start();
		}

		for (int i = 0; i < numberOfThreads; i++) {
			while (worker[i].isAlive()) {
				try {
					worker[i].join();
				}
				catch (InterruptedException e) {
				}
			}
		}
	}

	public class MostDivisors {
		private int maxDivisors;
		private int numWithMax;

		public MostDivisors(int maxDivisors, int numWithMax) {
			this.maxDivisors = maxDivisors;
			this.numWithMax = numWithMax;
		}

		public int getMaxDivisors() {
			return maxDivisors;
		}

		public int getNumWithMax() {
			return numWithMax;
		}
	}

	public MostDivisors calculateNumAndDivisors(int min, int max) {
		int maxDivisors;  // Maximum number of divisors seen so far.
		int numWithMax;   // A value of N that had the given number of divisors.
		maxDivisors = 1;
		numWithMax = 1;
		int N;            // One of the integers whose divisors we have to count.

		/* Process all the remaining values of N from 2 to 10000, and
          update the values of maxDivisors and numWithMax whenever we
          find a value of N that has more divisors than the current value
          of maxDivisors.
       */

		for (N = min; N <= max; N++) {

			int D;  // A number to be tested to see if it's a divisor of N.
			int divisorCount;  // Number of divisors of N.

			divisorCount = 0;

			for (D = 1; D <= N; D++) {  // Count the divisors of N.
				if (N % D == 0)
					divisorCount++;
			}

			if (divisorCount > maxDivisors) {
				maxDivisors = divisorCount;
				numWithMax = N;
			}
		}

		return new MostDivisors(maxDivisors, numWithMax);
	}

	public static void main(String[] args) {
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors == 1)
			System.out.println("Your computer has only 1 available processor.\n");
		else
			System.out.println("Your computer has " + processors + " available processors.\n");
		int numberOfThreads = 0;
		int rangeNums = 1;
		while (numberOfThreads < 1 || numberOfThreads > processors) {
			System.out.print("How many threads do you want to use  (from 1 to 12) ?  ");
			numberOfThreads = TextIO.getlnInt();
			if (numberOfThreads < 1 || numberOfThreads > processors)
				System.out.println("Please enter a number from 1 to " + processors + ".");
		}

		System.out.print("What number of numbers to find divisors for?  ");
		rangeNums = TextIO.getlnInt();

		MostDivisorsThreads mostDivsClass = new MostDivisorsThreads();
		mostDivsClass.incrementWithThreads(numberOfThreads, rangeNums);
		System.out.println("Among integers between 1 and " + rangeNums);
		System.out.println("Elapsed time: ");
		System.out.println("The maximum number of divisors is " + largestNumDivs);
		System.out.println("A number with " + largestNumDivs + " divisors is " + maxNum);
	}
}
