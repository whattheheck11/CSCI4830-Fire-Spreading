
public class ThreadNode implements Runnable{
	
	public static FireNode current;
	public static Map m;
	
	public ThreadNode(FireNode current, Map m){
		this.current = current;
		this.m = m;
	}
	
	public void run(){

		m.startPropagation();

	}
	
	public static void main(String args[]) {
		

		//System.out.println("Initial Forest Map:");

		//m.printTreeMap();
		
		m.fireUpdate(current);

		// declare object to hold the threads

		Thread t = new Thread();

		t = new Thread(new MapBuildTest());
		t.start();

		for (int i = 0; i < 1; i++) {

			try {

				t.join();

			} catch (InterruptedException e) {

				System.out.println("Interrupted Exception Caught");

			}

		}

 	}
	
	
	
	
}
