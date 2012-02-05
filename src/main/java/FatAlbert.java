
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * 
 * @author christofferrosen
 *
 */
public class FatAlbert implements Callable {
	
	private final VendingMachine vendingMachine;
	private final Timer timer;
	private final int daysToRun;
	private final CountDownLatch start;

    public FatAlbert(final VendingMachine vendingMachine, Timer timer, int daysToRun,
    		CountDownLatch start) {
        this.vendingMachine = vendingMachine;
        this.timer = timer;
        this.daysToRun = daysToRun;
        this.start = start;
       
    }

    public Object call() throws Exception {
        int i = 0;
        start.countDown();
        start.await();
        
        while( i <= daysToRun){
        	
            if(vendingMachine.dispenseCandy(1) && vendingMachine.dispenseCookie(1))
                System.out.println("            Hey, hey hey!");
            else if(vendingMachine.dispenseCandy(1))
            	System.out.println("           At least I got a Candy");
            else if(vendingMachine.dispenseCookie(1))
            	System.out.println("            At least I got a Cookie");
            else System.out.println("            No food for me today");
            Thread.sleep(getNextWaitTime());
            i = timer.getDay();

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




