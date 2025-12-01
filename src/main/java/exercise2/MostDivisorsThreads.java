package exercise2;

import utility.TextIO;

public class MostDivisorsThreads {

	/**
	 * A Thread belonging to this class will count primes in a specified range
	 * of integers.  The range is from min to max, inclusive, where min and max
	 * are given as parameters to the constructor.  After counting, the thread
	 * outputs a message about the number of primes that it has found, and it
	 * adds its count to the overall total by calling the addToTotal(int) method.
	 */
	private static class CountDivisorsThread extends Thread {
		MostDivisorsThreads numsDivs = new MostDivisorsThreads();
//		public CountDivisorsThread(int min, int max) {
//			this.min = min;
//			this.max = max;
//		}
//		public void run() {
//			count = countPrimes(min,max);
//			System.out.println("There are " + count +
//					" primes between " + min + " and " + max);
//			addToTotal(count);
//		}
	}

	/**
	 * Counts the primes in the range from (START+1) to (2*START), using a specified number
	 * of threads.  The total elapsed time is printed.
//	 * @param numberOfThreads
	 */
//	private static void countDivisorsWithThreads(int numberOfThreads) {
//		int increment = START/numberOfThreads;
//		System.out.println("\nCounting primes between " + (START+1) + " and "
//				+ (2*START) + " using " + numberOfThreads + " threads...\n");
//		long startTime = System.currentTimeMillis();
//		CountDivisorsThread[] worker = new CountDivisorsThread[numberOfThreads];
//		for (int i = 0; i < numberOfThreads; i++)
//			worker[i] = new CountDivisorsThread( START+i*increment+1, START+(i+1)*increment );
//		total = 0;
//		for (int i = 0; i < numberOfThreads; i++)
//			worker[i].start();
//		for (int i = 0; i < numberOfThreads; i++) {
//			while (worker[i].isAlive()) {
//				try {
//					worker[i].join();
//				}
//				catch (InterruptedException e) {
//				}
//			}
//		}
//		long elapsedTime = System.currentTimeMillis() - startTime;
//		System.out.println("\nThe number of primes is " + total + ".");
//		System.out.println("\nTotal elapsed time:  " + (elapsedTime/1000.0) + " seconds.\n");
//	}

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

	public MostDivisors calculateNumAndDivisors(int rangeNums) {
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

		for (N = 2; N <= rangeNums; N++) {

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

		System.out.print("How many times should each thread increment the counter?  ");
		rangeNums = TextIO.getlnInt();

		//TODO: Update method below to use multiple threads using countDivisorsWithThreads above as example.
//		MostDivisors mostDivsClass = numsDivs.calculateNumAndDivisors(rangeNums);
		System.out.println("Among integers between 1 and " + rangeNums);
		System.out.println("Elapsed time: ");
//		System.out.println("The maximum number of divisors is " + mostDivsClass.getMaxDivisors());
//		System.out.println("A number with " + mostDivsClass.getMaxDivisors() + " divisors is " + mostDivsClass.getNumWithMax());
	}
}
