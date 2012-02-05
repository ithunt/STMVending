
import akka.stm.Atomic;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 
 * @author christofferrosen
 * @author ian hunt
 *
 */
public class FatAlbert implements Callable {
	
	private final VendingMachine vendingMachine;
	private final Timer timer;
	private final CountDownLatch start;
    
    private int requestsToday;
    private int lastDay;

    public FatAlbert(final VendingMachine vendingMachine, Timer timer,
    		CountDownLatch start) {
        this.vendingMachine = vendingMachine;
        this.timer = timer;
        this.start = start;
       
    }

    public Object call() throws Exception {
        int day = timer.getDay();

        start.countDown();
        start.await();
        final long startTime = System.currentTimeMillis();
        
        while(day < Main.DAYS_TO_RUN){
        	
            new Atomic<Boolean>() {
                public Boolean atomically() {
                    final boolean candy = vendingMachine.dispenseCandy(1, this.getClass());
                    final boolean cookie = vendingMachine.dispenseCookie(1, this.getClass());

                    if(candy && cookie)
                        System.out.println("            Hey, hey hey!");
                    else if(candy)
                        System.out.println("            At least I got a Candy");
                    else if(cookie)
                        System.out.println("            At least I got a Cookie");
                    else System.out.println("            No food for me today");
                    return (candy && cookie);
                }
            }.execute();


            Thread.sleep(getNextWaitTime(startTime, day));
            day = timer.getDay();

        } return null;

    }
    
    
    /**
     * Fat Albert likes to have a cookie and a candy bar 2-4 times a day at randomly spaced intervals. 
     * @return	The amount of wait time in milliseconds thread must sleep until FA tries to get another
     * cookie and candy.
     */
    private long getNextWaitTime(final long startTime, int day) {
        
        if(day != lastDay) {
            requestsToday = 0;
            lastDay = day;
        }
        final long millisRemaining = (((day+2) * Main.SECONDS_IN_A_DAY)
                - (System.currentTimeMillis() - startTime));
        
        //if max requests, wait till beginning of next day
        if(requestsToday > 3) return millisRemaining;
        else {
            requestsToday++;
            if(requestsToday < 2) {
                return (long)(Math.random() * millisRemaining * .5);
            } else {
                return (long)(Math.random() * millisRemaining);
            }

        }

    }
}




