import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author ian hunt
 * @date 1/30/12
 */
public class WillieWonka implements Callable<Object> {

	private final VendingMachine vendingMachine;
	private final Timer timer;
	private final int daysToRun;
	private final CountDownLatch start;

	public WillieWonka(final VendingMachine vendingMachine, Timer timer,
			int daysToRun, CountDownLatch start) {
		this.vendingMachine = vendingMachine;
		this.timer = timer;
		this.daysToRun = daysToRun;
		this.start = start;
	}

	public Object call() throws Exception {
		int i = 0;
		start.countDown();
		start.await();
		while (i < daysToRun) {
			long timeToWait = getNextWaitTime();
			Thread.sleep(timeToWait);
			
			if (vendingMachine.dispenseCandy(1))
				System.out.println("        The Candy Man Can");
			else
				System.out.println("        Violet - you're turning violet");

			i = timer.getDay();
			// Wait for the rest of the day
			Thread.sleep(1000 - timeToWait);

		}
		return null;

	}

	/**
	 * Willie Wonka likes to have 1 candy bar each day at some point during the
	 * day.
	 * 
	 * @return The amount of wait time in milliseconds thread must sleep until
	 *         WW tries to get another cookie and candy.
	 */
	private long getNextWaitTime() {

		// He can wait between nothing to a full day before he goes to the
		// vending machine.
		int minWait = 0;
		int maxWait = 1000;

		double random = new Random().nextDouble();
		double sleepyTime = minWait + (random * (maxWait - minWait));
		return ((long) sleepyTime);

	}
}
