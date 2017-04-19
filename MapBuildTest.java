import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapBuildTest implements Runnable{

	//build the map

	//Density argument: 1: low, 2: mid, 3: high

	public static int height = 100;
	public static int width = 100;
	public static int density = 3;
	
	//Wind argument: 'n' - north, 's' - south, 'e' - east, 'w' - west, none otherwise.
	public static char wind = 'o';
	public static int windIntensity = 2; //1: low, 2: mid, 3: high

	
	public static Map m = new Map(height, width, density, wind);
	static Lock l = new ReentrantLock();

	

	public void run(){
			m.startPropagation();
	}

	//main test function

	public static void main(String args[]) {
		m.startFire(width/2, height/2);
 	}

}
