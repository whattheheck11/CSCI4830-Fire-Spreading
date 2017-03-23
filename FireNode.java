//Import needed libraries
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FireNode {
	FireNode north;
	FireNode south;
	FireNode east;
	FireNode west;
	
	Boolean onFire;
	int probabilityOfCatchingFire;
	boolean hasTree;
	private Lock l = new ReentrantLock();

	
	public FireNode(){
		onFire = false;
	}
	
}
