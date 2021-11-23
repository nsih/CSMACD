public class Timer implements Runnable{
	int time = 0;
	boolean wake=false;
	public synchronized void wake() {wake=true;}
	public synchronized void inc() {time++;}
	public synchronized int now(){return time;}
	
	public void run() {
		try {
			while(wake==false) {Thread.sleep(10);}
			Thread.sleep(10);
			
			System.out.println("wake");
			while (true) {
				int tempTime = time;
				Thread.sleep(40);
				if (tempTime == time) time++;
				System.out.println(time);
			}

		} catch (InterruptedException e) {
			//e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {

	}
}