package exercise4;

import exercise2.MostDivisorsThreads;
import utility.TextIO;

import java.util.ArrayList;
import java.util.concurrent.*;

/**
 <p>
 Find the number with the most divisors, when given a number. For example, if given the number 10,000,
 work through all numbers from 1 to 10,000 to determine which number within that range has the most
 divisors.
 </p>
 <p>An ExecutorService is used to do the work of creating a thread pool and a LinkedBlockingQueue
 into which MostDivisors tasks will be added. ExecutorService is also used to get the tasks and invoke them.</p>
 <p>Results of tasks, in the form of MostDivisors will be added to LinkedBlockingQueue to wait for processing.</p>
 */
public class MostDivisorsExecutor {

	/*
	 * This queue holds all the tasks to be performed by the pool of threads. It's a Runnable type
	 * instead of MostDivisors so that the thread simply needs to call the run method to call the task.
	 */
//	ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

	/** The executor that executes the MostDivisors.
	 When a job is started, an executor is created to
	 execute the tasks that make up that job.
	 private ExecutorService executor;   */
	private ExecutorService executor;

	/** The resultQueue holds MostDivisors results.*/
	private final LinkedBlockingQueue<MostDivisors> resultQueue = new LinkedBlockingQueue<>();

	/**
	 * how many threads have not yet terminated?
	 */
	private volatile int threadsRunning = 12;

	/**
	 * The threads that compute MostDivisors objects.
	 */
//	private WorkerThread[] workers;

	/**
	 * for specifying the number of threads to be used
	 */
	private int threadCountSelect;

	private static int theNum = 0;
	private static int numDivisors = 0;

	public static void main(String[] args) throws InterruptedException {

		//Let user know how many processors there are.
		//Let user choose how many threads to create for the pool.
		int processors = Runtime.getRuntime().availableProcessors();
		if (processors == 1) {
			System.out.println("Your computer has only 1 available processor.\n");
		} else {
			System.out.println("Your computer has " + processors + " available processors.\n");
		}
		System.out.println("This program breaks up the computation into a number of tasks.");
		System.out.println("For load balancing, the number of tasks should be at least");
		System.out.println("several times the number of processors.  (Try 100 tasks.)");
		System.out.println();
		int numberOfThreads = 0;
		int rangeNums = 1;
		while (numberOfThreads < 1 || numberOfThreads > (processors * 2)) {
			System.out.print("How many threads do you want to use  (from 1 to " + processors * 2 + ") ?  ");
			numberOfThreads = TextIO.getlnInt();
			if (numberOfThreads < 1 || numberOfThreads > processors * 2) {
				System.out.println("Please enter a number from 1 to " + processors * 2 + ".");
			}
		}

		System.out.print("What number of numbers to find divisors for?  ");
		rangeNums = TextIO.getlnInt();

		MostDivisorsExecutor topClass = new MostDivisorsExecutor();

		long elapsedTime;
		long startTime = System.currentTimeMillis();
		topClass.startMostDivisorsWork(rangeNums, numberOfThreads);// Start the processing.
		topClass.getMostDivisorsResult(rangeNums);
		long endTime = System.currentTimeMillis();
		elapsedTime = (endTime - startTime)/1000;

		System.out.println("Among integers between 1 and " + rangeNums);
		System.out.println("Elapsed time: " + elapsedTime + " seconds.");
		System.out.println("The maximum number of divisors is " + numDivisors);
		System.out.println("The first number found with " + numDivisors + " divisors was " + theNum);
	}

	/**
	 * Loop through the resultQueue comparing number of divisors. Update the maxDivisor variable
	 * until the loop finishes. We know how many results there will be because we know the number
	 * that was originally given.
	 */
	private void getMostDivisorsResult(int rangeNums) throws InterruptedException {

		for (int i = 0; i < rangeNums; i++) {
			MostDivisors md = this.resultQueue.take();
			if (md.getMaxDivisors() > numDivisors) {
				numDivisors = md.getMaxDivisors();
				theNum = md.getNumWithMax();
			}
		}
	}

	public class MostDivisors implements Runnable {
		private int maxDivisors;
		private int numWithMax;
		private int aNumFromTheRange;

		public MostDivisors(int maxDivisors, int numWithMax) {
			this.maxDivisors = maxDivisors;
			this.numWithMax = numWithMax;
		}

		public MostDivisors(int aNumFromTheRange) {
			this.aNumFromTheRange = aNumFromTheRange;
		}

		public int getMaxDivisors() {
			return maxDivisors;
		}

		public int getNumWithMax() {
			return numWithMax;
		}

		@Override
		public void run() {
			resultQueue.add(calculateDivisorsOfNum(aNumFromTheRange));
		}
	}

	public MostDivisors calculateDivisorsOfNum(int aNumFromTheRange) {
		int divisorCount = calculateDivisorCount(aNumFromTheRange);
		return new MostDivisors(divisorCount, aNumFromTheRange);
	}

	/**
	 * Calculates how many numbers a passed in number has.
	 *
	 * @param N We need to find the number of divisors for this number.
	 * @return A MostDivisors object containing the passed in number and the number of its divisors.
	 */
	private int calculateDivisorCount(int N) {
		int D;
		int divisorCount = 0;  // Number of divisors of N.

		for (D = 1; D <= N; D++) {  // Count the divisors of N.
			if (N % D == 0)
				divisorCount++;
		}
		return divisorCount;
	}

	/**
	 * Worker threads remove tasks from a ConcurrentLinkedQueue and invoked the task to get a result.
	 */

	/**
	 * This method is called by each thread when it terminates. We keep track
	 * of the number of threads that have terminated, so that when they have
	 * all finished we can notify the user.
	 */
	synchronized private void threadFinished() {
		threadsRunning--;
		if (threadsRunning == 0) { // all threads have finished
			System.out.println("Computations have finished.");
		}
	}

	private void startMostDivisorsWork(int numRange, int numberOfTasks) {

		System.out.println("\nCounting divisors between " + (1) + " and "
				+ (numRange) + " using " + numberOfTasks + " tasks...\n");
		long startTime = System.currentTimeMillis();

		double increment = (double) numRange / numberOfTasks;

		int processors = Runtime.getRuntime().availableProcessors();
		executor = Executors.newFixedThreadPool(processors);

		/* An ArrayList used to store the Futures that are created when the tasks
		 * are submitted to the ExecutorService. */
		ArrayList<Future<Integer>> results = new ArrayList<>();

		/* Create the subtasks, add them to the executor, and save the Futures. */
		int min = 1; // The start of the range of integers for one subtask.
		int max;     // The end of the range of integers for one subtask.
		for (int i = 0; i < numberOfTasks; i++) {
			max = (int) (min + 1 + (i + 1) * increment);
			if (i == numberOfTasks - 1) {
				max = numRange;
			}
		}
	}
}
