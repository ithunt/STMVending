import akka.stm.Atomic;
import akka.stm.Ref;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author ian hunt
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

    public boolean dispenseCookie(final int num) {
        return new Atomic<Boolean>() {
            public Boolean atomically() {
                int cookies = cookieLevel.get();
                if(num > 0 && cookies >= num) {
                    cookieLevel.swap(cookies - num);
                    return true;
                } else {
                    return false;
                }
            }
        }.execute();
    }

    public boolean dispenseCandy(final int num) {
        return new Atomic<Boolean>() {
            public Boolean atomically() {
                int candy = candyLevel.get();
                if(num > 0 && candy >= num) {
                    candyLevel.swap(candy - num);
                    return true;
                } else {
                    return false;
                }
            }
        }.execute();
    }
    
    public boolean dispenseCandyAndCookie(final int candy, final int cookies) {
        return new Atomic<Boolean>() {
            public Boolean atomically() {
                return dispenseCandy(candy) && dispenseCookie(cookies);
            }
        }.execute();
    }
    
    private void replenish() {
        new Atomic() {
            public Object atomically() {
                long currentCandy = candyLevel.get();
                long currentCookie = cookieLevel.get();
                if(currentCandy < MAX_INVENTORY) candyLevel.set(MAX_INVENTORY);
                if(currentCookie < MAX_INVENTORY) cookieLevel.set(MAX_INVENTORY);
                return null;
            }
        }.execute();
    }
    
    
    
}
