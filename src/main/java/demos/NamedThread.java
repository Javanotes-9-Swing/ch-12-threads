package demos;


public class NamedThread extends Thread {
   private String name;  // The name of this thread.
   public NamedThread(String name) {  // Constructor gives name to thread.
      this.name = name;
   }
   public void run() {  // The run method prints a message to standard output.
      System.out.println("Greetings from thread '" + name + "'!");
   }

	public static void main(String[] args) {
		NamedThread greetings = new NamedThread("Fred");
		System.out.println("Thread " + Thread.currentThread() +  " has been started");
		greetings.start();

//		System.out.println("This computer has " + Runtime.getRuntime().availableProcessors() + " processors.");

		System.out.println("Default thread priority " + Thread.NORM_PRIORITY);
		System.out.println("Min thread priority " + Thread.MIN_PRIORITY);

		System.out.println("Priority of current thread " + Thread.currentThread().getPriority());
	}
}