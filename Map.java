import java.util.Random;
import java.lang.Thread;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Map {
	public int rows;
	public int columns;
	public int perimeterNodes;
	private FireNode m[][];
	public FireNode epicenter;
	int treeDensity;
	Random rand = new Random();
	static Lock l = new ReentrantLock();

	//constructor
	public Map(int height, int width, int density){
		columns = width;
		rows = height;
		treeDensity = density;
		m = new FireNode[rows][columns];
		for(int i = 0; i < rows; i ++){
			for(int j = 0; j < columns; j++){
				//generate new node
				m[i][j] = new FireNode();
				
				//determine whether this node houses a tree based on given density
				int n = rand.nextInt(100)+1;
				//low density
				if(treeDensity == 1){
					if(n > 80){
						m[i][j].hasTree = true;
					}
				}
				//mid density
				else if(treeDensity == 2){
					if(n > 40){
						m[i][j].hasTree = true;
					}
				}
				//high density
				else if(treeDensity == 3){
					if(n > 10){
						m[i][j].hasTree = true;
					}
				}
				//default density upon invalid input
				else{
					if(n > 50){
						m[i][j].hasTree = true;
					}
				}
			}
		}
		
		//establish pointer connections to acknowledge edge cases
		linker();
	}
	
	//set fire starting point
	public void startFire(int row, int column){
		m[row][column].onFire = true;
		epicenter = m[row][column];
	}
	
	//start propagation
	public void startPropagation(){
		FireNode current = epicenter;
		FireNode pred = null;
		while(true){
			//generate random direction
			//1:north 2:west 3:south 4:east
			int n = rand.nextInt(4)+1;
			if(n == 1 && pred != current.south){
				if(current.north == null || current.north.hasTree == false){
					return;
				}
				pred = current;
				current = current.north;
				current.onFire = true;
				l.lock();
				try{
					printTreeMap();
				}finally{
					l.unlock();
				}
			}
			else if(n == 2 && pred != current.east){
				if(current.west == null || current.west.hasTree == false){
					return;
				}
				pred = current;
				current = current.west;
				current.onFire = true;
				l.lock();
				try{
					printTreeMap();
				}finally{
					l.unlock();
				}
			}
			else if(n == 3 && pred != current.north){
				if(current.south == null || current.south.hasTree == false){
					return;
				}
				pred = current;
				current = current.south;
				current.onFire = true;
				l.lock();
				try{
					printTreeMap();
				}finally{
					l.unlock();
				}
			}
			else if(n == 4 && pred != current.west){
				if(current.east == null || current.east.hasTree == false){
					return;
				}
				pred = current;
				current = current.east;
				current.onFire = true;
				l.lock();
				try{
					printTreeMap();
				}finally{
					l.unlock();
				}
			}
		}
	}
	
	//prints a 2D representation of the map
	public void printTreeMap(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j ++){
				if(m[i][j].hasTree == true && m[i][j].onFire == false){
					System.out.print("T ");
				}
				else if(m[i][j].hasTree == true && m[i][j].onFire == true){
					System.out.print("W ");
				}
				else{
					System.out.print("0 ");
				}
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
	}

	//prints the number of perimeter node to test the validity of linking method
	public void testEdges(){
		if(perimeterNodes == 2*columns + 2*(rows-2)){
			System.out.println("Perimeter Nodes: " + perimeterNodes + " Map Valid");
		}
		
	}
	
	private void linker(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				//top left corner
				if(i == 0 && j == 0){
					m[i][j].east = m[i][j+1];
					m[i][j].south = m[i+1][j];
					m[i][j].north = null;
					m[i][j].west = null;
					perimeterNodes++;
				}
				//top right corner
				else if(i == 0 && j == columns - 1){
					m[i][j].east = null;
					m[i][j].south = m[i+1][j];
					m[i][j].north = null;
					m[i][j].west = m[i][j-1];
					perimeterNodes++;
				}
				//bottom left corner
				else if(i == rows - 1 && j == 0){
					m[i][j].east = m[i][j+1];
					m[i][j].south = null;
					m[i][j].north = m[i-1][j];
					m[i][j].west = null;
					perimeterNodes++;
				}
				//bottom right corner
				else if(i == rows - 1 && j == columns - 1){
					m[i][j].east = null;
					m[i][j].south = null;
					m[i][j].north = m[i-1][j];
					m[i][j].west = m[i][j-1];
					perimeterNodes++;
				}
				//middle cases on row 0
				else if(i == 0){
					m[i][j].east = m[i][j+1];
					m[i][j].south = m[i+1][j];
					m[i][j].north = null;
					m[i][j].west = m[i][j-1];
					perimeterNodes++;
				}
				//middle cases on row rows - 1
				else if(i == rows - 1){
					m[i][j].east = m[i][j+1];
					m[i][j].south = null;
					m[i][j].north = m[i-1][j];
					m[i][j].west = m[i][j-1];
					perimeterNodes++;
				}
				//middle cases on column 0
				else if(j == 0){
					m[i][j].east = m[i][j+1];
					m[i][j].south = m[i+1][j];
					m[i][j].north = m[i-1][j];
					m[i][j].west = null;
					perimeterNodes++;
				}
				//middle cases on column 0
				else if(j == columns - 1){
					m[i][j].east = null;
					m[i][j].south = m[i+1][j];
					m[i][j].north = m[i-1][j];
					m[i][j].west = m[i][j-1];
					perimeterNodes++;
				}
				//all other middle nodes
				else{
					m[i][j].east = m[i][j+1];
					m[i][j].south = m[i+1][j];
					m[i][j].north = m[i-1][j];
					m[i][j].west = m[i][j-1];
				}
			}
		}
	}

}
