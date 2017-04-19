/*Multithreading is working, but each thread dies after it burns its surroundings. It should continue burning until
* it hits an edge
*/

import java.util.Random;
import java.lang.Thread;
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
	
	@Override
	public void mouseClicked(MouseEvent e) {
	    int x = e.getX();
	    int y = e.getY();
	    //startFire(x,y);
	    startPropagation();
	    System.out.println("Mouse Clicked at X: " + x + " - Y: " + y);
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
	
    public static final int PREFERRED_GRID_SIZE_PIXELS = 10;


	public int rows;

	public int columns;
	
	public char wind;

	public int perimeterNodes;

	private FireNode m[][];

	public FireNode epicenter;
		
	int treeDensity;

	Random rand = new Random();

	static Lock l = new ReentrantLock();



	//constructor

	public Map(int height, int width, int density, char windDir){
		
        addMouseListener(this); //for mouse click events
        
        wind = windDir;

		columns = width;

		rows = height;

		treeDensity = density;

		m = new FireNode[rows][columns];

		for(int i = 0; i < rows; i ++){

			for(int j = 0; j < columns; j++){

				//generate new node

				m[i][j] = new FireNode();
				m[i][j].row = i;
				m[i][j].col = j;

				

				//determine whether this node houses a tree based on given density

				int n = rand.nextInt(100)+1;

				//low density

				if(treeDensity == 1){

					if(n > 65){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

				//mid density

				else if(treeDensity == 2){

					if(n > 30){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

				//high density

				else if(treeDensity == 3){

					if(n > 10){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

				//default density upon invalid input

				else{

					if(n > 50){

						m[i][j].hasTree = true;
						m[i][j].c = new Color(0,255,0); //Green for a Tree

					}
					else{
						m[i][j].c = new Color(153,102,0); //Brown for no Tree
					}

				}

			}
		}
		
		//Initial Graphics screen setup
		int preferredWidth = columns * PREFERRED_GRID_SIZE_PIXELS;
		int preferredHeight = rows * PREFERRED_GRID_SIZE_PIXELS;
		setPreferredSize(new Dimension(preferredWidth, preferredHeight));
        frame.add(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

		//establish pointer connections to acknowledge edge cases
		linker();

	}

	
	//set fire starting point
	public void startFire(int row, int column){
		m[row][column].onFire = true;
		epicenter = m[row][column];
	}
	
	public void fireUpdate(FireNode current){
		epicenter = current;
	}
	
	public void startPropagation(){
        FireNode current = epicenter;
        FireNode pred = null;
    
        //generate random direction
        //1:north 2:west 3:south 4:east
        //int n = rand.nextInt(4)+1;
        for (int i = 0; i < 4; i++){
            if(i == 0 && current.north != null && current.north.hasTree == true && current.north.onFire == false){
                //pred = current;
                //current = current.north;
                
                int n = rand.nextInt(100)+1;
                
                if (wind == 'n') {
                    current.north.onFire = true;
                    current.north.c  = new Color(255,0,0); //Orange red for fire
                    
                    
                    l.lock();
                    try{
                        //print map on graphics display
                        frame.invalidate();
                        frame.validate();
                        frame.repaint();
                        
                        //printTreeMap();
                    }finally{
                        l.unlock();
                    }
                    
                    ThreadNode tn = new ThreadNode(current.north, this);
                    tn.start();
                }
                else if (wind == 's') {
                    if (n > 70) {
                        current.north.onFire = true;
                        current.north.c  = new Color(255,0,0); //Orange red for fire
                        
                        
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        
                        ThreadNode tn = new ThreadNode(current.north, this);
                        tn.start();
                    }
                }
                
                else if (wind == 'e' || wind == 'w') {
                    if (n > 35) {
                        current.north.onFire = true;
                        current.north.c  = new Color(255,0,0); //Orange red for fire
                        
                        
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        
                        ThreadNode tn = new ThreadNode(current.north, this);
                        tn.start();
                    }
                }
                else{
                	 current.north.onFire = true;
                     current.north.c  = new Color(255,0,0); //Orange red for fire
                     
                     
                     l.lock();
                     try{
                         //print map on graphics display
                         frame.invalidate();
                         frame.validate();
                         frame.repaint();
                         
                         //printTreeMap();
                     }finally{
                         l.unlock();
                     }
                     
                     ThreadNode tn = new ThreadNode(current.north, this);
                     tn.start();
                }
            }
                
            else if(i == 1 && current.west != null && current.west.hasTree == true && current.west.onFire == false){
                //pred = current;
                //current = current.north;
                
                int n = rand.nextInt(100)+1;
                
                if (wind == 'w') {
                    current.west.onFire = true;
                    current.west.c  = new Color(255,0,0); //Orange red for fire
                    
                    l.lock();
                    try{
                        //print map on graphics display
                        frame.invalidate();
                        frame.validate();
                        frame.repaint();
                        
                        //printTreeMap();
                    }finally{
                        l.unlock();
                    }
                    ThreadNode tn = new ThreadNode(current.west, this);
                    tn.start();
                }
                
                else if (wind == 'e') {
                    if (n > 70) {
                        current.west.onFire = true;
                        current.west.c  = new Color(255,0,0); //Orange red for fire
                        
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        ThreadNode tn = new ThreadNode(current.west, this);
                        tn.start();
                    }
                }
                
                else if (wind == 'n' || wind == 's') {
                    if (n > 35) {
                        current.west.onFire = true;
                        current.west.c  = new Color(255,0,0); //Orange red for fire
                        
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        ThreadNode tn = new ThreadNode(current.west, this);
                        tn.start();
                    }
                }
                else{
                	current.west.onFire = true;
                    current.west.c  = new Color(255,0,0); //Orange red for fire
                    
                    l.lock();
                    try{
                        //print map on graphics display
                        frame.invalidate();
                        frame.validate();
                        frame.repaint();
                        
                        //printTreeMap();
                    }finally{
                        l.unlock();
                    }
                    ThreadNode tn = new ThreadNode(current.west, this);
                    tn.start();
                }
            }
            else if(i == 2 && current.south != null && current.south.hasTree == true && current.south.onFire == false){
                //pred = current;
                //current = current.north;
                
                int n = rand.nextInt(100)+1;
                
                if (wind == 's') {
                    current.south.onFire = true;
                    current.south.c  = new Color(255,0,0); //Orange red for fire
                    
                    l.lock();
                    try{
                        //print map on graphics display
                        frame.invalidate();
                        frame.validate();
                        frame.repaint();
                        
                        //printTreeMap();
                    }finally{
                        l.unlock();
                    }
                    ThreadNode tn = new ThreadNode(current.south, this);
                    tn.start();
                }
                
                else if (wind == 'n') {
                    if (n > 70) {
                        current.south.onFire = true;
                        current.south.c  = new Color(255,0,0); //Orange red for fire
                        
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        ThreadNode tn = new ThreadNode(current.south, this);
                        tn.start();
                    }
                }
                
                else if (wind == 'e' || wind == 'w') {
                    if (n > 35) {
                        current.south.onFire = true;
                        current.south.c  = new Color(255,0,0); //Orange red for fire
                        
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        ThreadNode tn = new ThreadNode(current.south, this);
                        tn.start();
                    }
                }
                else{
                	 current.south.onFire = true;
                     current.south.c  = new Color(255,0,0); //Orange red for fire
                     
                     l.lock();
                     try{
                         //print map on graphics display
                         frame.invalidate();
                         frame.validate();
                         frame.repaint();
                         
                         //printTreeMap();
                     }finally{
                         l.unlock();
                     }
                     ThreadNode tn = new ThreadNode(current.south, this);
                     tn.start();
                }
            }
            else if(i == 3 && current.east != null && current.east.hasTree == true && current.east.onFire == false){
                //pred = current;
                //current = current.north;
                
                int n = rand.nextInt(100)+1;
                
                if (wind == 'e') {
                    current.east.onFire = true;
                    current.east.c  = new Color(255,0,0); //Orange red for fire
                    l.lock();
                    try{
                        //print map on graphics display
                        frame.invalidate();
                        frame.validate();
                        frame.repaint();
                        //printTreeMap();
                    }finally{
                        l.unlock();
                    }
                    
                    ThreadNode tn = new ThreadNode(current.east, this);
                    tn.start();
                }
                
                else if (wind == 'w'){
                    if (n > 70) {
                        current.east.onFire = true;
                        current.east.c  = new Color(255,0,0); //Orange red for fire
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        
                        ThreadNode tn = new ThreadNode(current.east, this);
                        tn.start();
                    }
                }
                else if (wind == 'n' || wind == 's') {
                    if (n > 35) {
                        current.east.onFire = true;
                        current.east.c  = new Color(255,0,0); //Orange red for fire
                        l.lock();
                        try{
                            //print map on graphics display
                            frame.invalidate();
                            frame.validate();
                            frame.repaint();
                            //printTreeMap();
                        }finally{
                            l.unlock();
                        }
                        
                        ThreadNode tn = new ThreadNode(current.east, this);
                        tn.start();
                    }
                }
                else{
                    current.east.onFire = true;
                    current.east.c  = new Color(255,0,0); //Orange red for fire
                    l.lock();
                    try{
                        //print map on graphics display
                        frame.invalidate();
                        frame.validate();
                        frame.repaint();
                        //printTreeMap();
                    }finally{
                        l.unlock();
                    }
                    
                    ThreadNode tn = new ThreadNode(current.east, this);
                    tn.start();
                }
                
            }
        }
        
    }

	/*//start propagation

	public void startPropagation(){

		FireNode current = epicenter;

		FireNode pred = null;

		//generate random direction

		//1:north 2:west 3:south 4:east

		//int n = rand.nextInt(4)+1;

		for (int i = 0; i < 4; i++){

			if(i == 0 && current.north != null && current.north.hasTree == true && current.north.onFire == false){

				//pred = current;

				//current = current.north;

				current.north.onFire = true;
				current.north.c  = new Color(255,0,0); //Orange red for fire
				
				

				l.lock();

				try{
					//print map on graphics display
					frame.invalidate();
					frame.validate();
					frame.repaint();
					
					//printTreeMap();

				}finally{

					l.unlock();

				}
				
				ThreadNode tn = new ThreadNode(current.north, this);
				//tn.main(null);
				tn.start();
	
			}
				

			else if(i == 1 && current.west != null && current.west.hasTree == true && current.west.onFire == false){

				//pred = current;

				//current = current.north;

				current.west.onFire = true;
				current.west.c  = new Color(255,0,0); //Orange red for fire
				


				l.lock();

				try{
					//print map on graphics display
					frame.invalidate();
					frame.validate();
					frame.repaint();
					
					//printTreeMap();

				}finally{

					l.unlock();

				}

				ThreadNode tn = new ThreadNode(current.west, this);
				//tn.main(null);
				tn.start();

			}

			else if(i == 2 && current.south != null && current.south.hasTree == true && current.south.onFire == false){

				//pred = current;

				//current = current.north;

				current.south.onFire = true;
				current.south.c  = new Color(255,0,0); //Orange red for fire
				
				l.lock();

				try{
					//print map on graphics display
					frame.invalidate();
					frame.validate();
					frame.repaint();
					
					//printTreeMap();

				}finally{

					l.unlock();

				}

				ThreadNode tn = new ThreadNode(current.south, this);
				//tn.main(null);
				tn.start();


			}
			else if(i == 3 && current.east != null && current.east.hasTree == true && current.east.onFire == false){

				//pred = current;

				//current = current.north;

				current.east.onFire = true;
				current.east.c  = new Color(255,0,0); //Orange red for fire


				l.lock();

				try{
					//print map on graphics display
					frame.invalidate();
					frame.validate();
					frame.repaint();

					//printTreeMap();

				}finally{

					l.unlock();

				}
				
				ThreadNode tn = new ThreadNode(current.east, this);
				//tn.main(null);
				tn.start();
			}
		}
	}*/

	

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
                int y = j * rectHeight;
                Color terrainColor = m[i][j].c;
                g.setColor(terrainColor);
                g.fillRect(x, y, rectWidth, rectHeight);
            }
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

	

	/*private void linker(){

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

	}*/


}
