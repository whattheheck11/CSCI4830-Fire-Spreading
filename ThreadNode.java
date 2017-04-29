
public class ThreadNode{
	
	public static FireNode current;
	public static Map m;
	
	public ThreadNode(FireNode current, Map m){
		this.current = current;
		this.m = m;
	}
	
	public void start(){
		m.fireUpdate(current);
		Thread t = new Thread(new MapBuildTest());
		t.start();
	}
}

