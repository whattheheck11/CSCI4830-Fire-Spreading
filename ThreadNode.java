/*
 * Chase Heck and Clint Olsen
 * ECEN 4313/CSCI 4830: Concurrent Programming
 * Final Project: The Simulation of Fire Spreading
 * Class Description: This is the object that acts as the generator for the individual threads created
 * along the fire's propagation. The class stores it starting node and generates a thread to begin its 
 * propagation
 */
public class ThreadNode{
	
	public static FireNode current;
	public static Map m;
	
	public ThreadNode(FireNode current, Map m){
		this.current = current;
		this.m = m;
	}
	
	public void start(){
		m.fireUpdate(current); //set  unique starting point.
		Thread t = new Thread(new MapBuildTest());
		t.start();
	}
}

