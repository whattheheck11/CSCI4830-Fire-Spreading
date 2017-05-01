/*
 * Chase Heck and Clint Olsen
 * ECEN 4313/CSCI 4830: Concurrent Programming
 * Final Project: The Simulation of Fire Spreading
 * Class Description: This class is the definition of the FireNode objects that make up each element of the map. 
 * Each node holds values that other threads use to determine its propagation direction.
 */
//Import needed libraries
import java.awt.Color;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FireNode {
	FireNode north;
	FireNode south;
	FireNode east;
	FireNode west;
	public int row;
	public int col;
	
	Color c;
	
	Boolean onFire;
	boolean hasTree;
	public Lock l;

	
	public FireNode(){
		onFire = false;
		l = new ReentrantLock();
	}
	
	//locking to prevent the generation of unnecessary threads.
	public void lock(){
		l.lock();
	}
	public void unlock(){
		l.unlock();
	}
	
}
