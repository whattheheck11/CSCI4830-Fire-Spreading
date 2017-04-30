import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MapBuildTest implements Runnable{

	public static int height = 100;

	public static int width = 100;

	public static int density;


	//Wind argument (blowing these directions): 'n' - north, 's' - south, 'e' - east, 'w' - west, none otherwise.

	public static char wind;

	public static char humidity; //l: low, m: medium, h: high

	public static int windIntensity; //0: none 1: low, 2: mid, 3: high

	//build the map

	public static Map m;

	static Lock l;


	public void run(){

		m.startPropagation();

	}

	//main test function

	public static void main(String args[]) {
		
		JOptionPaneMultiInput j = new JOptionPaneMultiInput();
		j.main(null);
		
		m = new Map(height, width, density, wind, humidity);

		l = new ReentrantLock();
		
		m.startFire(width/2, height/2);

 	}

}
