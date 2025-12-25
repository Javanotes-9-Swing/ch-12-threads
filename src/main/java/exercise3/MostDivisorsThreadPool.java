package exercise3;

import demos.MultiprocessingDemo2;
import exercise2.MostDivisorsThreads;

import javax.swing.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 <p>
 Find the number with the most divisors, when given a number. For example, if given the number 10,000,
 work through all numbers from 1 to 10,000 to determine which number within that range has the most
 divisors.
 </p>
 <p>
 Create a thread pool so the work of finding the number can be divided into enough subproblems such
 that no thread is doing far more or far less work than others. Use a blocking queue to hold the tasks and
 use another blocking queue to hold the results.
 </p>
 <p>Divisor tasks will have to be created and stored in the task queue. One of the threads will take
 that task, call it, and get the resulting MostDivisors object. Another waiting thread will take that
 result for finding the number with the most divisors.</p>
 <p>
 The results are in the form of an inner class that holds a number and its number of divisors. The
 inner class is the same as that used in {@link  MostDivisorsThreads.MostDivisors}. Each available
 thread will take the number, and it's number of divisors and compare that to the current number with
 most divisors. By the time all numbers have been compared the number with most divisors will be available
 as global variables.
 </p>
 <p>A startMostDivisorsWork method will initialize the taskQueue and add MostDivisors tasks to the queue</p>
 *
 */
public class MostDivisorsThreadPool {

	/**
	 * This queue holds all the tasks to be performed by the pool of threads. It's a Runnable type
	 * instead of MostDivisors so that the thread simply needs to call the run method to call the task.
	 */
	ConcurrentLinkedQueue<Runnable> taskQueue = new ConcurrentLinkedQueue<>();

	/** The resultQueue holds MostDivisors results.*/
	private LinkedBlockingQueue<MostDivisors> resultQueue;

	/**
	 * how many threads have not yet terminated?
	 */
	private volatile int threadsRunning = 12;

	/**
	 * The threads that compute MostDivisors objects.
	 */
	private WorkerThread[] workers;

	/**
	 * for specifying the number of threads to be used
	 */
	private int threadCountSelect;

	public static void main(String[] args) {

		/*
			TODO: Add stuff to get from user number to run divisor task on. Consider using GUI stuff
				from MultiprocessingDemo3 as guide/inspiration.
		 */

		getMostDivisorsResult();
	}

	/**
	 *TODO: Loop through the resultQueue comparing number of divisors. Update the maxDivisor variable
	 *  until the loop finishes. We know how many results there will be because we know the number
	 *  that was originally given.
	 */
	private static void getMostDivisorsResult() {
		for (int i = 0; i < 10000; i++) {
			System.out.println(i);
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

		for (N = 0; N <= aNumFromTheRange; N++) {

//			int D;  // A number to be tested to see if it's a divisor of N.
//			int divisorCount;  // Number of divisors of N.
//
//			divisorCount = 0;

			int divisorCount = calculateDivisorCount(N);

			if (divisorCount > maxDivisors) {
				maxDivisors = divisorCount;
				numWithMax = N;
			}
		}

		return new MostDivisors(maxDivisors, numWithMax);
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
	private class WorkerThread extends Thread {
		public void run() {
			try {
				while (true) { //TODO: true may be replaced with running variable
					Runnable task = taskQueue.poll(); // Get a task from the taskQueue.
					if (task == null)
						break; // (because the queue is empty)
					task.run();  // Execute the task to get a MostDivisors object as result;
				}
			} finally {
				threadFinished(); // Records fact that this thread has terminated.
				// Done in finally to make sure it gets called.
			}
		}
	}

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

	private void startMostDivisorsWork() {
		taskQueue = new ConcurrentLinkedQueue<Runnable>();

		//TODO: Create however many MostDivisor tasks and add them to the taskQueue.
		for (int i = 0; i < 10000; i++) {
			MostDivisors task = new MostDivisors(i);//TODO: Do we add task but not result here?
			taskQueue.add(task);
		}

		int threadCount = 12;
		if (threadCount == 11)
			threadCount = 20;
		workers = new WorkerThread[threadCount];
//		running = true; // Set the signal before starting the threads!
		threadsRunning = threadCount; // Records how many of the threads have not yet terminated.
		for (int i = 0; i < threadCount; i++) {
			workers[i] = new WorkerThread();
			try {
				workers[i].setPriority(Thread.currentThread().getPriority() - 1);
			} catch (Exception ignored) {
			}
			workers[i].start();
		}
	}
}
