import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author ian hunt
 * @date 1/30/12
 */
public class CookieMonster implements Callable<Object> {

    private final VendingMachine vendingMachine;
    private final int daysToRun;
    private final Timer timer;
    private final CountDownLatch start;

    public CookieMonster(final VendingMachine vendingMachine, Timer timer, int daysToRun,
    		CountDownLatch start) {
        this.vendingMachine = vendingMachine;
        this.daysToRun = daysToRun;
        this.timer = timer;
        this.start = start;
    }

    public Object call() throws Exception {
    	int i = 0;

    	//start.await();
        while(i <= daysToRun) {
        	
        	// He gets a cookie every half day.
        	
            if(vendingMachine.dispenseCookie(1))
                System.out.println("CookieMonster:\t Me love cookies");
            else System.out.println("CookieMonster:\t Me hungry");
            
            Thread.sleep(500);
            i = timer.getDay();

        } return null;

    }
}
    
 