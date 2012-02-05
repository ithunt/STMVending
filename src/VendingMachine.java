import akka.stm.Atomic;
import akka.stm.Ref;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Vending machince that holds Cookies and Candy.
 * Customers can request either one of these items, or both.
 * Does not accept payment. 
 * 
 * @author ian hunt
 * @author christoffer rosen
 * @author Patrick McAfee
 * @date 1/30/12
 */
public class VendingMachine {
    
    private final int MAX_INVENTORY = 6;
    private static final long REPLENISH_INVENTORY_SECS = 3;
    final Ref<Integer> cookieLevel = new Ref<Integer>(MAX_INVENTORY);
    final Ref<Integer> candyLevel = new Ref<Integer>(MAX_INVENTORY);
    final Ref<Boolean> keepRunning = new Ref<Boolean>(true);
    
    private static final ScheduledExecutorService replenishTimer =
            Executors.newScheduledThreadPool(10);

    private VendingMachine() {}

    private void init() {
        replenishTimer.schedule(new Runnable() {
            public void run() {
                replenish();
                if (keepRunning.get()) 
                    replenishTimer.schedule(
                        this, REPLENISH_INVENTORY_SECS, TimeUnit.SECONDS);
                
            }
        }, REPLENISH_INVENTORY_SECS, TimeUnit.SECONDS);
    }
    
    public static VendingMachine create() {
        final VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.init();
        return vendingMachine;
    }

    public boolean dispenseCookie(final int num, final Class customer) {
        return new Atomic<Boolean>() {
            public Boolean atomically() {
                int cookies = cookieLevel.get();
                if(num > 0 && cookies >= num) {
                    cookieLevel.swap(cookies - num);
                    System.out.println(customer.getName() + " dispensed " + num + " cookie(s). " +
                            cookieLevel.get() + " remain.");
                    return true;
                } else {
                    return false;
                }
            }
        }.execute();
    }

    public boolean dispenseCandy(final int num, final Class customer) {
        return new Atomic<Boolean>() {
            public Boolean atomically() {
                int candy = candyLevel.get();
                if(num > 0 && candy >= num) {
                    candyLevel.swap(candy - num);
                    System.out.println(customer.getName() + " dispensed " + num + " candybar(s). " +
                        candyLevel.get() + " remain.");
                    return true;
                } else {
                    return false;
                }
            }
        }.execute();
    }
    
    private void replenish() {
        new Atomic() {
            public Object atomically() {
                final long currentCandy = candyLevel.get();
                final long currentCookie = cookieLevel.get();
                if(currentCandy < MAX_INVENTORY) candyLevel.swap(MAX_INVENTORY);
                if(currentCookie < MAX_INVENTORY) cookieLevel.swap(MAX_INVENTORY);
                System.out.println("CurrentCandy: " + currentCandy + " - CurrentCookies: " + currentCookie + " - Replenished To: " + MAX_INVENTORY);
                return null;
            }
        }.execute();
    }
    
    public void stopVendingMachine() { keepRunning.swap(false); }
    
    public int getCookiesAvailable() { return cookieLevel.get(); }
    
    public int getCandyAvailable() { return candyLevel.get();  }

}
