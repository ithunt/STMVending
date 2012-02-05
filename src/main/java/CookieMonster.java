import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author ian hunt
 * @date 1/30/12
 */
public class CookieMonster implements Callable<Object> {

    private final VendingMachine vendingMachine;
    private final Timer timer;
    private final CountDownLatch start;

    public CookieMonster(final VendingMachine vendingMachine, Timer timer,
    		CountDownLatch start) {
        this.vendingMachine = vendingMachine;
        this.timer = timer;
        this.start = start;
    }

    public Object call() throws Exception {
    	int day = timer.getDay();

        start.countDown();
    	start.await();
        while(day < Main.DAYS_TO_RUN) {
        	
        	// He gets a cookie every half day.
        	
            if(vendingMachine.dispenseCookie(1))
                System.out.println("    Me love cookies");
            else System.out.println("    Me hungry");
            
            Thread.sleep(500);
            day = timer.getDay();

        } return null;

    }
}
    
 