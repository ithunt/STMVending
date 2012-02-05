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
	private final CountDownLatch start;

	public WillieWonka(final VendingMachine vendingMachine, Timer timer, CountDownLatch start) {
		this.vendingMachine = vendingMachine;
		this.timer = timer;
		this.start = start;
	}

	public Object call() throws Exception {
		int day = timer.getDay();
        start.countDown();
		start.await();
		while (day < Main.DAYS_TO_RUN) {
			final long timeToWait = getNextWaitTime();
			Thread.sleep(timeToWait);
			
			if (vendingMachine.dispenseCandy(1))
				System.out.println("        The Candy Man Can");
			else
				System.out.println("        Violet - you're turning violet");

			day = timer.getDay();
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
		final int minWait = 0;
		final int maxWait = 1000;

		final double random = new Random().nextDouble();
		final double sleepyTime = minWait + (random * (maxWait - minWait));
		return ((long) sleepyTime);

	}
}
