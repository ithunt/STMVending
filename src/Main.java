import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Main class, which will start the simulation that will run for 1/2 a month.
 * 
 * @author ian hunt
  * @author christoffer rosen
 * @author Patrick McAfee
 * @date 1/30/12
 */
public class Main {
	
	public static final int SECONDS_IN_A_DAY = 1000;  //millis
	public static final int DAYS_TO_RUN = 15;
    
    
    public static void main(String[] args) throws InterruptedException {
    	
    	
    	VendingMachine vm = VendingMachine.create();
    	
    	CountDownLatch start = new CountDownLatch(3);
    	Timer timer = new Timer(start);
    	
    	
    	WillieWonka ww = new WillieWonka(vm, timer, start);
    	FatAlbert fa = new FatAlbert(vm, timer, start);
    	CookieMonster cm = new CookieMonster(vm, timer, start);
    	
    	
    	List<Callable<Object>> tasks = new ArrayList<Callable<Object>>();   
    	tasks.add(cm);
    	tasks.add(fa);
    	tasks.add(ww);

    	final ExecutorService service = Executors.newFixedThreadPool(10);
    	timer.start();
        service.invokeAll(tasks);

        
        vm.stopVendingMachine();
        service.shutdown();
        timer.interrupt();
    	System.exit(0);

    	

    }
}
