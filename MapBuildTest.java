/*
 * Chase Heck and Clint Olsen
 * ECEN 4313/CSCI 4830: Concurrent Programming
 * Final Project: The Simulation of Fire Spreading
 * Class Description: This is the main test class for the fire propagation problem. This class gathers input data
 * from the user in order to configure the start of the fire propagation.
 */


public class MapBuildTest implements Runnable{

	//map dimensions
	public static int height = 100;
	public static int width = 100;


	//environmental parameters 
	public static char wind; //Wind argument (blowing these directions): 'n' - north, 's' - south, 'e' - east, 'w' - west, none otherwise.
	public static char humidity; //l: low, m: medium, h: high
	public static int density; //Tree Density 1: low, 2:medium, 3:high 


	//build the map
	public static Map m;
	
	public void run(){
		//thread run method
		m.startPropagation();

	}

	//main test function
	public static void main(String args[]) {
		
		//shutdown hook that displays the total runtime and threads generated on program exit
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                System.out.println((float)m.total/(float)1000 + " seconds");
                System.out.println(m.counter + " Threads Generated");
            }
        });
		
		//JPanel to display menu for user input
		JOptionPaneMultiInput j = new JOptionPaneMultiInput();
		j.main(null);
		
		//map construction with gathered input parameters
		m = new Map(height, width, density, wind, humidity);
		
 	}

}