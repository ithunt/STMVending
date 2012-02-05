import akka.stm.Atomic;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Willie Wonka, who goes to the candy machine at some point everyday
 * and tries to get one candy bar. 
 * 
 * @author ian hunt
 * @author christoffer rosen
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
			
            new Atomic<Boolean>() {
                public Boolean atomically() {
                    if (vendingMachine.dispenseCandy(1, this.getClass()))
                        System.out.println("        The Candy Man Can");
                    else
                        System.out.println("        Violet - you're turning violet");
                    return true;
                }
            }.execute();


			day = timer.getDay();
			// Wait for the rest of the day
			Thread.sleep(Main.SECONDS_IN_A_DAY - timeToWait);

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
	private static long getNextWaitTime() {

		// He can wait between nothing to a full day before he goes to the
		// vending machine.
		final int minWait = 0;
		final int maxWait = Main.SECONDS_IN_A_DAY;

		final double random = new Random().nextDouble();
		final double sleepyTime = minWait + (random * (maxWait - minWait));
		return ((long) sleepyTime);

	}
}
