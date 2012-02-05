
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

        //start.await();
        
        while( i <= daysToRun){
        	
            final boolean candy = vendingMachine.dispenseCandy(1);
            final boolean cookie = vendingMachine.dispenseCandy(1);

            if(candy && cookie)
                System.out.println("FatAlbert:\t\t Hey, hey hey!");
            else if(candy)
            	System.out.println("FatAlbert:\t\t At least I got a Candy");
            else if(cookie)
            	System.out.println("FatAlbert:\t\t At least I got a Cookie");
            else System.out.println("FatAlbert:\t\t No food for me today");

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




