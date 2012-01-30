import java.util.concurrent.Callable;

/**
 * @author ian hunt
 * @date 1/30/12
 */
public class CookieMonster implements Callable<Object> {

    private final VendingMachine vendingMachine;

    public CookieMonster(final VendingMachine vendingMachine) {
        this.vendingMachine = vendingMachine;
    }

    public Object call() throws Exception {
        this.wait(500);

        while(true) {
            if(vendingMachine.dispenseCookie(1))
                System.out.println("Me love cookies");
            else System.out.println("Me hungry");
            this.wait(1500);

        }

    }
}
