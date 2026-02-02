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

}
