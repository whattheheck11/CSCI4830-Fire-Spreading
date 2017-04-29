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
	int probabilityOfCatchingFire;
	boolean hasTree;
	public Lock l;

	
	public FireNode(){
		onFire = false;
		l = new ReentrantLock();
	}
	
	public void lock(){
		l.lock();
	}
	public void unlock(){
		l.unlock();
	}
	
}
