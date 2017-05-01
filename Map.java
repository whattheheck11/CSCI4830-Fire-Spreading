/*
 * Chase Heck and Clint Olsen
 * ECEN 4313/CSCI 4830: Concurrent Programming
 * Final Project: The Simulation of Fire Spreading
 * Class Description: This class defines the map on which the fire will propagate. Functionality includes the
 * determination of fire spread direction per thread, as well as the probabilistic considerations made in regards to
 * wind and humidity
 */

//import needed libraries
import java.util.Random;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JFrame;


public class Map extends JPanel implements MouseListener{
	//Declare JPanel for GUI
	static JFrame frame = new JFrame("Map");
	
	//map size parameters
    public static final int PREFERRED_GRID_SIZE_PIXELS = 10; //pixel height and with for tree and non-tree display cells
	public int rows;
	public int columns;
	public int perimeterNodes;

	
	//Environmental factors
	public char wind;
	public int humidity;
	boolean windExists;
	int treeDensity;
	public int probBurnOp = 100;
	public int probBurnSide = 70;

	//keeps track of first call to start propagation to initiate timing
	private boolean firstCall;
	
	//map definition: array of FireNodes
	private FireNode m[][];
	public FireNode epicenter; //start cell of fire
		
	Random rand = new Random(); //variable used for probabilistic factors

	//lock for preventing  unnecessary thread generation
	static Lock l = new ReentrantLock();
	
	 //locks timing variables
	static Lock timeLock = new ReentrantLock();
	
	//benchmarking variables
	public static long last = System.currentTimeMillis();
	public static long total;
	public static int counter;


	//Allows the user to click any location on the map to indicate where the fire should start
	@Override
	public void mouseClicked(MouseEvent e) {
	    int x = e.getX();
	    int y = e.getY();
	    startFire(x/PREFERRED_GRID_SIZE_PIXELS,y/PREFERRED_GRID_SIZE_PIXELS);
	    startPropagation();
	}
	@Override
	public void mouseEntered(MouseEvent e) {
	}
	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}
	
	//constructor
	public Map(int height, int width, int density, char windDir, char getHumidity){
		
        addMouseListener(this); //for mouse click events
        
        firstCall = true;
        
        wind = windDir;

		columns = width;

		rows = height;

		treeDensity = density;

		m = new FireNode[rows][columns];
		
		if (wind == 'n' || wind == 's' || wind == 'e' || wind == 'w') {
        	windExists = true;
        } else windExists = false;
        
        //Get humidity and allocate probability
        if (getHumidity == 'l') {

			humidity = 30;
			if (windExists == true){
				humidity -= 20;
			}

		} else if (getHumidity == 'm') {

			humidity = 85;
			if (windExists == true){
				humidity -= 55;
			}

		} else {
			humidity = 95;
			if (windExists == true){
				humidity -= 50;
			}
		}
        //generate a random map based on the tree density
		for(int i = 0; i < rows; i ++){

			for(int j = 0; j < columns; j++){

				//generate new node
				m[i][j] = new FireNode();
				m[i][j].row = i;
				m[i][j].col = j;

				
				//determine whether this node houses a tree based on given density
				int n = rand.nextInt(100)+1;

				//Few Trees
				if(treeDensity == 1){

					if(n > 15){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

				//Many trees
				else if(treeDensity == 2){

					if(n > 10){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

				//Lotta Trees
				else if(treeDensity == 3){

					if(n > 5){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

				//default density upon invalid input (2)
				else{

					if(n > 10){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

			}
		}
		
		//Initial Graphics screen setup with JFrame
		int preferredWidth = columns * PREFERRED_GRID_SIZE_PIXELS;
		int preferredHeight = rows * PREFERRED_GRID_SIZE_PIXELS;
		setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

		//establish pointer connections between all nodes to acknowledge edge cases
		linker();

	}

	
	//set fire starting point
	public void startFire(int row, int column){
		m[row][column].onFire = true;
		epicenter = m[row][column];
		m[row][column].c  = new Color(255,0,0); //Orange red for fire
		frame.invalidate();
		frame.validate();
		frame.repaint();
	}
	
	//indicates where the thread is starting its burn
	public void fireUpdate(FireNode current){
		epicenter = current;
	}
	
	public void startPropagation(){
		//start timing
		if(firstCall == true){
        	last = System.currentTimeMillis();
        	firstCall = false;
		}
        FireNode current = epicenter;
        
        /*Here the thread will loop through each possible direction and based on the current status of the node, wind
        * humidity, and locked status will determine its next burn. It is possible for the thread not to burn at all
        * before exiting
        */
        
        //0:north 1:west 2:south 3:east       
        for (int i = 0; i < 4; i++){
        	
        	//check north
            if(i == 0 && current.north != null && current.north.hasTree == true && current.north.onFire == false){
                        
            	//generate random number for the wind influence
                int n = rand.nextInt(200)+1;
                
                if (wind == 'n') {
                    if(current.north.l.tryLock()){
                    	try{
							
							if (n > humidity) {
							
								current.north.onFire = true;
								current.north.c  = new Color(255,0,0); //Orange red for fire
															
								//print map on graphics display
								l.lock();
								try{
									frame.invalidate();
									frame.validate();
									frame.repaint();
								}finally{
									l.unlock();
								}
							}
		                    
                    	}finally{
                    		//current.north.unlock();
                    	}
		                //creates a new thread to start burning at this spot    		                    
                    	ThreadNode tn = new ThreadNode(current.north, this);
                    	tn.start();
                    }
                }
                else if (wind == 's') {
                    if (n > probBurnOp + humidity) {
                        if(current.north.l.tryLock()){
                        	try{
		                        current.north.onFire = true;
		                        current.north.c  = new Color(255,0,0); //Orange red for fire
		                       
			                    //print map on graphics display
			                    l.lock();
			                    try{
			                    	frame.invalidate();
			                    	frame.validate();
			                    	frame.repaint();
			                    }finally{
			                    	l.unlock();
			                    }
			                    
                        	} finally{
                        		//current.north.unlock();
                        	}
    		                //creates a new thread to start burning at this spot    		                                  
		                    ThreadNode tn = new ThreadNode(current.north, this);
		                    tn.start();
                    	}
                    }
                }
                
                else if (wind == 'e' || wind == 'w') {
                    if (n > probBurnSide + humidity) {
                        if(current.north.l.tryLock()){
                        	try{
		                        current.north.onFire = true;
		                        current.north.c  = new Color(255,0,0); //Orange red for fire
		                        
			                    //print map on graphics display
			                    l.lock();
			                    try{
			                    	frame.invalidate();
			                    	frame.validate();
			                    	frame.repaint();
			                    }finally{
			                    	l.unlock();
			                    }
			                    
                        	}finally{
                        		//current.north.unlock();
                        	}
    		                //creates a new thread to start burning at this spot    		                    
                        	ThreadNode tn = new ThreadNode(current.north, this);
                        	tn.start();
                        }
                    }
                }
                
                else{
					if (n > humidity) {
						if(current.north.l.tryLock()){
							try{
								 current.north.onFire = true;
								 current.north.c  = new Color(255,0,0); //Orange red for fire
								 
			
								//print map on graphics display
								l.lock();
								try{
									frame.invalidate();
									frame.validate();
									frame.repaint();
								}finally{
									l.unlock();
								}
									
							}finally{
								//current.north.unlock();
							}
			                //creates a new thread to start burning at this spot    		                    
							ThreadNode tn = new ThreadNode(current.north, this);
							tn.start();
						}
					}
				}
            }
            
        	//check west
            else if(i == 1 && current.west != null && current.west.hasTree == true && current.west.onFire == false){
                
                int n = rand.nextInt(200)+1;
                
	                if (wind == 'w') {
						if (n > humidity) {
							if(current.west.l.tryLock()){
								try{
									current.west.onFire = true;
									current.west.c  = new Color(255,0,0); //Orange red for fire
									
									//print map on graphics display
									l.lock();
									try{
										frame.invalidate();
										frame.validate();
										frame.repaint();
									}finally{
										l.unlock();
									}
									
								}finally{
									//current.west.unlock();
								}
				                //creates a new thread to start burning at this spot    		                    
								ThreadNode tn = new ThreadNode(current.west, this);
								tn.start();
							}
						}
	                }
	                
	                else if (wind == 'e') {
	                    if (n > probBurnOp + humidity) {
		                	if(current.west.l.tryLock()){
		                		try{
			                        current.west.onFire = true;
			                        current.west.c  = new Color(255,0,0); //Orange red for fire
	
				                    //print map on graphics display
				                    l.lock();
				                    try{
				                    	frame.invalidate();
				                    	frame.validate();
				                    	frame.repaint();
				                    }finally{
				                    	l.unlock();
				                    }
				                    
		                		}finally{
		                			//current.west.unlock();
		                		}
		                		//creates a new thread to start burning at this spot
		                        ThreadNode tn = new ThreadNode(current.west, this);
		                        tn.start();
			                }
	                    }
	                }
	                
	                else if (wind == 'n' || wind == 's') {
	                    if (n > probBurnSide + humidity) {
		                    if(current.west.l.tryLock()){
		                    	try{
			                        current.west.onFire = true;
			                        current.west.c  = new Color(255,0,0); //Orange red for fire
			                        
				                    //print map on graphics display
				                    l.lock();
				                    try{
				                    	frame.invalidate();
				                    	frame.validate();
				                    	frame.repaint();
				                    }finally{
				                    	l.unlock();
				                    }

		                    	}finally{
		                    		//current.west.unlock();
		                    	}
		                    	//creates a new thread to start burning at this spot
		                    	ThreadNode tn = new ThreadNode(current.west, this);
		                    	tn.start();
		                    }
	                    }
	                }
	                
	                else{
						if (n > humidity) {
							if(current.west.l.tryLock()){
								try{
									current.west.onFire = true;
									current.west.c  = new Color(255,0,0); //Orange red for fire
									
									//print map on graphics display
									l.lock();
									try{
										frame.invalidate();
										frame.validate();
										frame.repaint();
									}finally{
										l.unlock();
									}
									
								}finally{
									//current.west.unlock();
								}
								//creates a new thread to start burning at this spot
								ThreadNode tn = new ThreadNode(current.west, this);
								tn.start();
							}
						}
	                }
            	} 
            
        //check south
        else if(i == 2 && current.south != null && current.south.hasTree == true && current.south.onFire == false){
        	
            int n = rand.nextInt(200)+1;
              	
            if (wind == 's') {
				if (n > humidity){
					if(current.south.l.tryLock()){
						try{ 
							current.south.onFire = true;
							current.south.c  = new Color(255,0,0); //Orange red for fire
			
							//print map on graphics display
							l.lock();
							try{
								frame.invalidate();
								frame.validate();
								frame.repaint();
							}finally{
								l.unlock();
							}
							
						}finally{
							//current.south.unlock();
						}     
						//creates a new thread to start burning at this spot
						ThreadNode tn = new ThreadNode(current.south, this);
						tn.start();
					}
				}
            }
            
            else if (wind == 'n') {
                if (n > probBurnOp + humidity) {
                    if(current.south.l.tryLock()){
                    	try{ 
		                    current.south.onFire = true;
		                    current.south.c  = new Color(255,0,0); //Orange red for fire
		                    
		                    //print map on graphics display
		                    l.lock();
		                    try{
		                    	frame.invalidate();
		                    	frame.validate();
		                    	frame.repaint();
		                    }finally{
		                    	l.unlock();
		                    }
		                    
                    	}finally{
                    		//current.south.unlock();
                    	}
                    	//creates a new thread to start burning at this spot
                    	ThreadNode tn = new ThreadNode(current.south, this);
                    	tn.start();
                    }
                }
           }
            
            else if (wind == 'e' || wind == 'w') {
                if (n > probBurnSide + humidity) {
                    if(current.south.l.tryLock()){
                    	try{ 
		                    current.south.onFire = true;
		                    current.south.c  = new Color(255,0,0); //Orange red for fire
		
		                    //print map on graphics display
		                    l.lock();
		                    try{
		                    	frame.invalidate();
		                    	frame.validate();
		                    	frame.repaint();
		                    }finally{
		                    	l.unlock();
		                    }
		                    
                    	}finally{
                    		//current.south.unlock();
                    	}
                    	//creates a new thread to start burning at this spot
                    	ThreadNode tn = new ThreadNode(current.south, this);
                    	tn.start();
                    }
                }
            }
            else{
				if (n > humidity) {
					if(current.south.l.tryLock()){
						try{ 
							 current.south.onFire = true;
							 current.south.c  = new Color(255,0,0); //Orange red for fire
							 
								//print map on graphics display
								l.lock();
								try{
									frame.invalidate();
									frame.validate();
									frame.repaint();
								}finally{
									l.unlock();
								}
								
						}finally{
							//current.south.unlock();
						}
						//creates a new thread to start burning at this spot
						ThreadNode tn = new ThreadNode(current.south, this);
						tn.start();
					}
				}
            }
        }
            
        //check east
	    else if(i == 3 && current.east != null && current.east.hasTree == true && current.east.onFire == false){
	            
	            int n = rand.nextInt(200)+1;
                if (wind == 'e') {
					if (n > humidity) {
						if(current.east.l.tryLock()){
							try{
								current.east.onFire = true;
								current.east.c  = new Color(255,0,0); //Orange red for fire
								
								//print map on graphics display
								l.lock();
								try{
									frame.invalidate();
									frame.validate();
									frame.repaint();
								}finally{
									l.unlock();
								}
								
							}finally{
								//current.east.unlock();
							}
							//creates a new thread to start burning at this spot
							ThreadNode tn = new ThreadNode(current.east, this);
							tn.start();
						}
					}
                }
                
                else if (wind == 'w'){
                    if (n > probBurnOp + humidity) {
        	            if(current.east.l.tryLock()){
        	            	try{
		                        current.east.onFire = true;
		                        current.east.c  = new Color(255,0,0); //Orange red for fire
		                        
			                    //print map on graphics display
			                    l.lock();
			                    try{
			                    	frame.invalidate();
			                    	frame.validate();
			                    	frame.repaint();
			                    }finally{
			                    	l.unlock();
			                    }
			                    
        	            	}finally{
        	            		//current.east.unlock();
        	            	}
        	            	//creates a new thread to start burning at this spot
                        ThreadNode tn = new ThreadNode(current.east, this);
                        tn.start();
        	            }
                    }
                }
                else if (wind == 'n' || wind == 's') {
                    if (n > probBurnSide + humidity) {
        	            if(current.east.l.tryLock()){
        	            	try{
		                        current.east.onFire = true;
		                        current.east.c  = new Color(255,0,0); //Orange red for fire
		
			                    //print map on graphics display
			                    l.lock();
			                    try{
			                    	frame.invalidate();
			                    	frame.validate();
			                    	frame.repaint();
			                    }finally{
			                    	l.unlock();
			                    }
        	            	}finally{
        	            		//current.east.unlock();
        	            	}
        	            	//creates a new thread to start burning at this spot
        	            	ThreadNode tn = new ThreadNode(current.east, this);
        	            	tn.start();
        	            }
                    }
                }
                
                else{
					if (n > humidity) {
						if(current.east.l.tryLock()){
							try{
								current.east.onFire = true;
								current.east.c  = new Color(255,0,0); //Orange red for fire
			
								//print map on graphics display
								l.lock();
								try{
									frame.invalidate();
									frame.validate();
									frame.repaint();
								}finally{
									l.unlock();
								}
							}finally{
								//current.east.unlock();
							}
							//creates a new thread to start burning at this spot
							ThreadNode tn = new ThreadNode(current.east, this);
							tn.start();
						}
					}
                }
	    	}       
	    }
	    timeLock.lock();
        try {
        	total += (System.currentTimeMillis() - last);
        	last = System.currentTimeMillis();
        	counter++;
        }finally {timeLock.unlock();}	    
	}

	//prints a 2D representation of the map (pre-JFrame graphics method)
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
	
	//paints the current colors of nodes in the map
    @Override
    public void paintComponent(Graphics g) {
        // Important to call super class method
        super.paintComponent(g);
        // Clear the board
        g.clearRect(0, 0, getWidth(), getHeight());
        // Draw the grid
        int rectWidth = getWidth() / columns;
        int rectHeight = getHeight() / rows;
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                // Upper left corner of this terrain rect
                int x = i * rectWidth;
                int y = j * (rectHeight+3);
                Color terrainColor = m[i][j].c;
                g.setColor(terrainColor);
                g.fillRect(x, y, rectWidth, rectHeight+3);
            }
        }
    }

	//prints the number of perimeter node to test the validity of linking method
	public void testEdges(){

		if(perimeterNodes == 2*columns + 2*(rows-2)){

			System.out.println("Perimeter Nodes: " + perimeterNodes + " Map Valid");

		}
	}
	
	//links together all of the perimeters nodes of a given node to allow for traversal during the propagation
	// Also used to eliminate null pointer exceptions on edges of map.
	private void linker(){

		for(int i = 0; i < rows; i++){
	
			for(int j = 0; j < columns; j++){
	
				//top left corner
	
				if(i == 0 && j == 0){
	
					m[i][j].east = m[i+1][j];
	
					m[i][j].south = m[i][j+1];
	
					m[i][j].north = null;
	
					m[i][j].west = null;
	
					perimeterNodes++;
	
				}
	
				//top right corner
	
				else if(i == columns - 1 && j == 0){
	
					m[i][j].east = null;
	
					m[i][j].south = m[i][j+1];
	
					m[i][j].north = null;
	
					m[i][j].west = m[i-1][j];
	
					perimeterNodes++;
	
				}
	
				//bottom left corner
	
				else if(i == 0 && j == rows-1){
	
					m[i][j].east = m[i+1][j];
	
					m[i][j].south = null;
	
					m[i][j].north = m[i][j-1];
	
					m[i][j].west = null;
	
					perimeterNodes++;
	
				}
	
				//bottom right corner
	
				else if(i == columns - 1 && j == rows - 1){
	
					m[i][j].east = null;
	
					m[i][j].south = null;
	
					m[i][j].north = m[i][j-1];
	
					m[i][j].west = m[i-1][j];
	
					perimeterNodes++;
	
				}
	
				//middle cases on row 0
	
				else if(j == 0){
	
					m[i][j].east = m[i+1][j];
	
					m[i][j].south = m[i][j+1];
	
					m[i][j].north = null;
	
					m[i][j].west = m[i-1][j];
	
					perimeterNodes++;
	
				}
	
				//middle cases on row rows - 1
	
				else if(j == rows - 1){
	
					m[i][j].east = m[i+1][j];
	
					m[i][j].south = null;
	
					m[i][j].north = m[i][j-1];
	
					m[i][j].west = m[i-1][j];
	
					perimeterNodes++;
	
				}
	
				//middle cases on column 0
	
				else if(i == 0){
	
					m[i][j].east = m[i+1][j];
	
					m[i][j].south = m[i][j+1];
	
					m[i][j].north = m[i][j-1];
	
					m[i][j].west = null;
	
					perimeterNodes++;
	
				}
	
				//middle cases on column - 1
	
				else if(i == columns - 1){
	
					m[i][j].east = null;
	
					m[i][j].south = m[i][j+1];
	
					m[i][j].north = m[i][j-1];
	
					m[i][j].west = m[i-1][j];
	
					perimeterNodes++;
	
				}
	
				//all other middle nodes
	
				else{
	
					m[i][j].east = m[i+1][j];
	
					m[i][j].south = m[i][j+1];
	
					m[i][j].north = m[i][j-1];
	
					m[i][j].west = m[i-1][j];
	
				}
	
			}
	
		}
	
	}	

}