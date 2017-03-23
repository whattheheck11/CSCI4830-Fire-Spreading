/*
 * 
 */
public class MapBuildTest implements Runnable{
	
	//build the map
	//Density argument: 1: low, 2: mid, 3: high
	public static Map m = new Map(10,10,3);
	
	public void run(){
		m.startPropagation();
	}
	
	//main test function
	public static void main(String args[]) {
		System.out.println("Initial Forest Map:");
		m.printTreeMap();
		//m.testEdges();
		
		int thread_size = 8;
		m.startFire(5, 5);
		
		// declare object to hold the threads
		Thread t[] = new Thread[thread_size];

		// create each thread and start it
		for (int i = 0; i < thread_size; i++) {
			t[i] = new Thread(new MapBuildTest());
			t[i].start();
		}
		// join each thread to halt the process of the main thread until all
		// incrementation is complete
		for (int i = 0; i < thread_size; i++) {
			try {
				t[i].join();
			} catch (InterruptedException e) {
				System.out.println("Interrupted Exception Caught");
			}

		}
		System.out.println("Aftermath");
 	}
	
}
