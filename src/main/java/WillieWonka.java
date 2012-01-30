import java.util.concurrent.Callable;

/**
 * @author ian hunt
 * @date 1/30/12
 */
public class WillieWonka implements Callable<Object> {

    private final VendingMachine vendingMachine;


    
    public WillieWonka(final VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
        //dayIndex = -1;
    }

    public Object call() throws Exception {
        while(true) {
            if(vendingMachine.dispenseCookie(1))
                System.out.println("Me love cookies");
            else System.out.println("Me hungry");
            this.wait(500);

        }

    }
    
    private long getNextWaitTime(long ) {

        //(dayIndex * 1000) + (long)(Math.random() * 1000);
    }
}
