/*

 * 

 */
 
public class MapBuildTest implements Runnable{

	//build the map

	//Density argument: 1: low, 2: mid, 3: high

	public static int height = 200;
	public static int width = 200;
	public static int density = 2;	
	
	public static Map m = new Map(height, width, density);


	public void run(){

		m.startPropagation();

	}

	

	//main test function

	public static void main(String args[]) {
		

		System.out.println("Initial Forest Map:");

		m.printTreeMap();

		//m.testEdges();

		m.startFire(height/2, width/2);

	
		// declare object to hold the threads

		Thread t = new Thread();

		t = new Thread(new MapBuildTest());
		t.start();

		// create each thread and start it

		// join each thread to halt the process of the main thread until all

		// incrementation is complete

		for (int i = 0; i < 1; i++) {

			try {

				t.join();

			} catch (InterruptedException e) {

				System.out.println("Interrupted Exception Caught");

			}

		}

		System.out.println("Aftermath");

 	}

	

}
