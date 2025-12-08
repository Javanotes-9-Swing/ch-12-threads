package exercise1;

import utility.TextIO;

public class ThreadUnsafeCounter {

	private int bigCount;
	static Counter counterClass;

	public static void main(String[] args) {

		counterClass = new Counter();

		int processors = Runtime.getRuntime().availableProcessors();
		if (processors == 1)
			System.out.println("Your computer has only 1 available processor.\n");
		else
			System.out.println("Your computer has " + processors + " available processors.\n");
		int numberOfThreads = 0;
		int incs = 0;

		// - User enters the number of threads and the number of times that each thread will increment
			// the timer.
		while (numberOfThreads < 1 || numberOfThreads > processors) {
			System.out.print("How many threads do you want to use  (from 1 to 12) ?  ");
			numberOfThreads = TextIO.getlnInt();
			if (numberOfThreads < 1 || numberOfThreads > processors)
				System.out.println("Please enter a number from 1 to " + processors + ".");
		}

		System.out.print("How many times should each thread increment the counter?  ");
		incs = TextIO.getlnInt();

		incrementWithThreads(numberOfThreads, incs);
		// - Each thread needs to call counterClass.inc()
		// - Print the final value of the counter once all threads have terminated
		System.out.println("Total number of increments was " + counterClass.getCount());
		System.out.println("Expected: " + numberOfThreads * incs);
	}

	/**
	 * Counts how many times  using a specified number
	 * of threads.  The total elapsed time is printed.
	 * @param numberOfThreads
	 */
	private static void incrementWithThreads(int numberOfThreads, int numberOfIncs) {
		CounterThread[] worker = new CounterThread[numberOfThreads];
		for (int i = 0; i < numberOfThreads; i++) {
			worker[i] = new CounterThread(numberOfIncs);
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

	/**
	 * A Thread belonging to this class will increment a counter.
	 */
	private static class CounterThread extends Thread {
		int incs;
		public CounterThread(int incs) {
			this.incs = incs;
		}
		public void run() {
			for (int i = 0; i < incs; i++) {
				counterClass.inc();
			}
		}
	}

	static class Counter {
		int count;
		synchronized void inc() {
			count = count+1;
		}
		int getCount() {
			return count;
		}
	}
}
