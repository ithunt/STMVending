import java.util.concurrent.CountDownLatch;


public class Timer extends Thread {
	
	private int day = 0;
	private final CountDownLatch start;
	public Timer(CountDownLatch start){
		this.start = start;
		
	}

	@Override
	public void run() {
		try {
			start.await();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while(true){
			try {
				System.out.println("DAY ----------- " + day);
				Thread.sleep(1000);
				day++;
		
				
			} catch (InterruptedException e){}
			
		}
	}
	
	public int getDay() {
		return day;
	}
	
}
