import akka.stm.Ref;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author ian hunt
 * @date 1/30/12
 */
public class VendingMachine {
    
    private final int MAX_INVENTORY = 6;
    final Ref<Integer> cookieLevel = new Ref<Integer>(MAX_INVENTORY);
    final Ref<Integer> candyLevel = new Ref<Integer>(MAX_INVENTORY);
    final Ref<Boolean> keepRunning = new Ref<Boolean>(true);
    
    private static final ScheduledExecutorService inventoryTimer =
            Executors.newScheduledThreadPool(10);

    private VendingMachine() {}


    
    
    
}
