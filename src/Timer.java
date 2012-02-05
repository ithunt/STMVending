import akka.stm.Ref;

import java.util.concurrent.CountDownLatch;

/**
 * Timer class that keeps track on what day it is.
 * 
 * @author christoffer rosen
 * @author Patrick McAfee
 * @author ian hunt
 *
 */

public class Timer extends Thread {
	
	private Ref<Integer> day = new Ref<Integer>(0);
	private final CountDownLatch start;
	public Timer(CountDownLatch start){
		this.start = start;
		
	}

    @Override
    public void run() {

          try {
              start.await();
          } catch (InterruptedException e1) {
              e1.printStackTrace();
          }

        while(!this.isInterrupted() && day.get() < Main.DAYS_TO_RUN){

            System.out.println("DAY ----------- " + day.get());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            day.swap(day.get()+1);

        }
    }

    public int getDay() {
        return day.get();
    }



}
