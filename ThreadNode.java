import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadNode{
	
	public static FireNode current;
	public static Map m;
	
	public ThreadNode(FireNode current, Map m){
		this.current = current;
		this.m = m;
	}
	
	public void start(){
		m.fireUpdate(current);
		Thread t = new Thread(new MapBuildTest());
		t.start();
	}
}
	
	/*public void run(){

		m.startPropagation();

	}
	
	public static void main(String args[]) {
		

		//System.out.println("main");

		//m.printTreeMap();
		
		m.fireUpdate(current);

		// declare object to hold the threads

		Thread t = new Thread(new MapBuildTest());

		//t = new Thread(new MapBuildTest());
		t.start();

		/*for (int i = 0; i < 1; i++) {

			try {

				t.join();

			} catch (InterruptedException e) {

				System.out.println("Interrupted Exception Caught");

			}

		}

 	}*/
