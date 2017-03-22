public class Map {
	public int rows;
	public int columns;
	public int perimeterNodes;
	private FireNode m[][];
	
	//constructor
	public Map(int height, int width){
		columns = width;
		rows = height;
		m = new FireNode[rows][columns];
		for(int i = 0; i < rows; i ++){
			for(int j = 0; j < columns; j++){
				m[i][j] = new FireNode();
			}
		}
		
		//establish pointer connections to acknowledge edge cases
		linker();
	}
	
	//prints a 2D representation of the map
	public void printMap(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j ++){
				System.out.print(m[i][j].onFire + " ");
			}
			System.out.println();
		}
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
