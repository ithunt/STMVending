import akka.stm.Atomic;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Cookie Monster, who goes to the vending machine twice a day at 
 * even intervals. Tries to get a cookie.
 * 
 * @author ian hunt
 * @author christoffer rosen
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
        	
            new Atomic<Boolean>() {
                public Boolean atomically() {
                    if(vendingMachine.dispenseCookie(1, this.getClass())) System.out.println("    Me love cookies");
                    else System.out.println("    Me hungry");
                    return true;
                }
            }.execute();
            
            Thread.sleep((long)(Main.SECONDS_IN_A_DAY * .5));
            day = timer.getDay();

        } return null;

    }
}
    
 