
import java.util.Random;
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
        
        while(day < Main.DAYS_TO_RUN){
        	
            final boolean candy = vendingMachine.dispenseCandy(1);
            final boolean cookie = vendingMachine.dispenseCandy(1);

            if(candy && cookie)
                System.out.println("            Hey, hey hey!");
            else if(candy)
            	System.out.println("            At least I got a Candy");
            else if(cookie)
            	System.out.println("            At least I got a Cookie");
            else System.out.println("            No food for me today");

            Thread.sleep(getNextWaitTime());
            day = timer.getDay();

        } return null;

    }
    
    
    /**
     * Fat Albert likes to have a cookie and a candy bar 2-4 times a day at randomly spaced intervals. 
     * @return	The amount of wait time in milliseconds thread must sleep until FA tries to get another
     * cookie and candy.
     */
    private long getNextWaitTime() {
    	
    	
    	// Random interval greater than 1/4 of a day (as he can go as many times as 4 a day)
    	// and smaller than 1/2 a day (as he can go at least twice a day)
    	
    	int minWait = 249;
    	int maxWait = 501;
    	
    	double random = new Random().nextDouble();
    	double sleepyTime = minWait + (random * (maxWait - minWait));
    	
    	
    	return (long) sleepyTime;
										    	
    	
    	

        
    }
}




