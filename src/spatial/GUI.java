package spatial;


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.stream.Stream;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame implements ActionListener,ChangeListener {
	
	private static final int Width=1000, Height=700;
	//Controls the amount of grids
	private int resolution = 6;
	//Current mouse clicked coordinates
	private static int CCX=-2,CCY=-2;
	//Game State
	private enum STATE{
		  START,
		  PLAY,
		  PAUSE,
		  STOP,
		  RESET
		}
	// This makes the beginning state START
	private STATE State = STATE.START;
	//Labels 
	private JLabel cooperatorLabel, defectorLabel, cooperatorCounter, defectorCounter, roundCounter;
	//counter variables
	private int coopCount =0, defectCount=0, rounds=0;
	//Button variables
	private JButton play,pause,reset;
	private JSlider speed;
	private int currentSpeed = 250;
	private Hashtable<Integer, JLabel> speedFigures = new Hashtable<Integer, JLabel>();
	private boolean beenReset = false;
	private Game game = new Game();

	//New mouse clicked coordinates
	public static int NCX=-1,NCY=-1;
	//mouse position coordinates
	public static int MX=-1, MY=-1;
	
	public GUI()
	{
		this.setTitle("Cooperation and Defection");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		//The game
		this.setContentPane(game);
		//Move class listener
		Move move = new Move();
		this.addMouseMotionListener(move);
		//click class listener
		Click click = new Click();
		this.addMouseListener(click);
		//Round counter
		roundCounter = new JLabel(rounds + "");
		roundCounter.setBounds(Width/34, Height-50, 100, 50);
		Font font = new Font("Courier", Font.BOLD,12);
		roundCounter.setFont(font);
		this.add(roundCounter);
		
		//Cooperator labels
		cooperatorLabel = new JLabel("Cooperators:");
		cooperatorLabel.setBounds(Width/16, Height-50, 100, 50);
		this.add(cooperatorLabel);
		//Cooperator counter
		cooperatorCounter = new JLabel(coopCount + "");
		cooperatorCounter.setBounds(Width/6, Height-50, 100, 50);
		this.add(cooperatorCounter);
		//Defector labels
		defectorLabel = new JLabel("Defectors:");
		defectorLabel.setBounds(Width/4, Height-50, 100, 50);
		this.add(defectorLabel);
		//Defector counter
		defectorCounter = new JLabel(defectCount + "");
		defectorCounter.setBounds(Width/3, Height-50, 100, 50);
		this.add(defectorCounter);
		//play button
		play = new JButton("Play");
		play.setBounds((int) (Width/2.5), Height-40, 100, 30);
		play.addActionListener(this);
		this.add(play);
		//pause button
		pause = new JButton("Pause");
		pause.setBounds(Width/2, Height-40, 100, 30);
		pause.addActionListener(this);
		this.add(pause);
		//speed slider
		speedFigures.put(490, new JLabel("0.2x"));
	    speedFigures.put(370, new JLabel("0.6x"));
	    speedFigures.put(250, new JLabel("1x"));
	    speedFigures.put(130, new JLabel("1.4x"));
	    speedFigures.put(10, new JLabel("1.8x"));
		speed = new JSlider(JSlider.HORIZONTAL,10,490,250);
		speed.setBounds((int) (Width/1.5),Height-50, 300,50);
		speed.setLabelTable(speedFigures);
		speed.setMajorTickSpacing(100);
		speed.setMinorTickSpacing(20);
		speed.setPaintLabels(true);
		speed.setPaintTicks(true);
		speed.addChangeListener(this);
		speed.setInverted(true);		
		this.add(speed);
		//reset button
		reset = new JButton("Reset");
		reset.setBounds((int) (Width/1.2), Height-50, 100, 50);
		reset.addActionListener(this);
		//this.add(reset);
		
		this.pack();
		this.setVisible(true);	
		}
		
	public class Game extends JPanel{
		//Adjusting square to resolution
		private int square; {
			if(resolution >= 30) {
				square = 1;
			}
			else if (resolution < 30 && resolution > 10) {
				square = 4;
			}
			else
			{
				square = 6;
			}
		}
		//col and row would adjust to the dimensions of the screen and resolution
		private int col = Width / resolution ;
		private int row = (Height / resolution) -square;
		//Colours
		private Color black = Color.BLACK;
		private Color w = Color.WHITE;
		private Color gr = Color.DARK_GRAY;
		private Color cyan = Color.CYAN;
		private Color red = Color.RED;
		private Color green = Color.GREEN;
		
		private Random rand = new Random();
		private Color states[] = new Color[]{red,green};
		private int counter =0;
		
		private int startRound = 0, endRound;

		//Array of cells used for the game
		public Color grid[][] = new Color[col][row]; {
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					//All the cells are randomly assigned green or red
					int number = rand.nextInt(20);
					if(number != 0) {
						number = 1;
					}
					//red dominant
					/*if(number != 1) {
						number = 0;
					}*/
					grid[i][j] = states[number];
					
					//Count the amount of states for each
					if(grid[i][j] == green) {
						coopCount++;
					}
					else if(grid[i][j] == red) {
						defectCount++;
					}
					
				}
			}
			endThread.coopPop.add(coopCount);
			endThread.defectPop.add(defectCount);
			endThread.rounds.add(rounds);
		
		};
		
		public int activeGridScore[][] = new int[col][row]; {
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					//All the cells are assigned a score of 0 at the start
					activeGridScore[i][j] = 0;			
				}
			}
		}
		
		public int oneStepGridScore[][] = new int[col][row]; {
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					//All the cells are assigned a score of 0 at the start
					oneStepGridScore[i][j] = 0;			
				}
			}
		}
		
		public int twoStepGridScore[][] = new int[col][row]; {
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					//All the cells are assigned a score of 0 at the start
					twoStepGridScore[i][j] = 0;			
				}
			}
		}
		
		private int spacing = 1;
	    private Timer timer = new Timer();
	    
		public Game() {
			this.setPreferredSize(new Dimension(Width,Height));
			this.setLayout(null);
			timer.scheduleAtFixedRate(new task(), 0, currentSpeed);
		}
		
		private void updateTimer() {
			timer.cancel();
			timer = new Timer();
			timer.scheduleAtFixedRate(new task(), 0, currentSpeed);
		}
		
		private void click(Graphics g,int i,int j) {
			
			if(CCX!=NCX && CCY!=NCY) {
				
				if(NCX >= (spacing +i*resolution )&& NCX<((i*resolution) + resolution - spacing) && NCY >= (spacing +j*resolution )+29&& NCY<(j*resolution)+ resolution - spacing+29) {
					if(grid[i][j] == red) {
						g.setColor(green);
						grid[i][j]= green;
						//Change the count
						coopCount++;
						defectCount--;
						cooperatorCounter.setText(coopCount + "");
						defectorCounter.setText(defectCount + "");
					}
					else if (grid[i][j] == green){
						g.setColor(red);
						grid[i][j]= red;
						//Change the count
						defectCount++;
						coopCount--;
						cooperatorCounter.setText(coopCount + "");
						defectorCounter.setText(defectCount + "");
					}
					CCX = NCX;
					CCY = NCY;
				}
			}	
		}
		
		private void move(Graphics g,int i,int j) {
			
			if(MX >= (spacing +i*resolution )&& MX<((i*resolution) + resolution - spacing) && MY >= (spacing +j*resolution )+29&& MY<(j*resolution)+ resolution - spacing+29) {
				g.setColor(gr);
			}
		}
		
		private void start(Graphics g) {
			spacing = 1;

			g.setColor(w);
			g.fillRect(0,0,Width,Height);
			
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					g.setColor(grid[i][j]);
					move(g,i,j);
					click(g,i,j);
					g.fillRect(spacing +i*resolution,spacing+ j*resolution,resolution-1*spacing ,resolution-1*spacing);
				}
			}	
		}
		
		private Color[][] baseImitationRule(Color[][] grid, Color[][] newgrid) {
			//Randomiser
			Random mutationSeed = new Random();
			//Compute the scores of all the cells
			rounds++;
			counter++;
			roundCounter.setText(rounds + "");
			
			endThread.rounds.add(rounds);
			endThread.coopPop.add(coopCount);
			endThread.defectPop.add(defectCount);
		
			//Chart building
			if(counter==50) {
				endRound = rounds;
				endThread.cycles.add(startRound+"-"+endRound);
				startRound = rounds;
				counter = 0;
			}
			
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					int sumScore = score(grid,i,j);
					activeGridScore[i][j] = sumScore;
					//System.out.println("i:"+i+"j:"+j+" "+activeGridScore[i][j]);
				}
			}
			
			//imitate
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					newgrid[i][j] = randomImitate(grid,i,j);
					//standardImitate
					//randomImitate
					//meanImitate
					
					//update the counter if the state has been changed
					if(grid[i][j] != newgrid[i][j]) {
						if(newgrid[i][j] == green) {
							//Change the count
							coopCount++;
							defectCount--;
							cooperatorCounter.setText(coopCount + "");
							defectorCounter.setText(defectCount + "");
						}
						else if (newgrid[i][j] == red) {
							//Change the count
							defectCount++;
							coopCount--;
							cooperatorCounter.setText(coopCount + "");
							defectorCounter.setText(defectCount + "");
						}
					}
				}
			}
			return newgrid;		
		}
		
		
		
		private int score(Color[][] grid, int x, int y) {
			//standard PD matrix
			int DC=5, CC=3, DD=1, CD=0;
			//Chicken game
			//int DC=4, CC=3, DD=0, CD=1;
			int score = 0;
			
			for (int i = -1; i<2; i++) {
				 for(int j=-1; j<2; j++) {
					 if(!(i==0 & j==0)) {
						 int cols = (x+i + col) % col;
						 int rows = (y+j + row) % row;

						 //CC
						 if(grid[x][y] == green & grid[cols][rows]== green) {
							 score= score + CC; 
						 }
						 //CD
						 else if(grid[x][y] == green & grid[cols][rows]== red) {
							 score= score + CD; 
						 }
						 //DD
						 else if(grid[x][y] == red & grid[cols][rows]== red) {
							 score= score + DD; 
						 }
						 //DC
						 else if(grid[x][y] == red & grid[cols][rows]== green) {
							 score= score + DC; 
						 }			 
					 }
				 }
			}
			return score;
		}
		
		private Color standardImitate(Color[][] grid, int x, int y) {
			Color newColour = grid[x][y];
			//Largest score 
			int LS = 0;
			int mutation = rand.nextInt(400);//mutation seed
			//iterate through each neighbour and set the current highest score as the new colour
			for (int i = -1; i<2; i++) {
				 for(int j=-1; j<2; j++) {
					 if(!(i ==0 && j==0)) {
						 int cols = (x+i + col) % col;
						 int rows = (y+j + row) % row;
						 
						 if(activeGridScore[cols][rows] > LS) {
							 LS = activeGridScore[cols][rows];
							 newColour = grid[cols][rows];
						 }
					 }
				 }
			}
			//if mutation occurs
			if(mutation == 0) {
				if(newColour == green) {
					newColour = red;
				}
				else if (newColour == red) {
					newColour = green;
				}
				
			} 
			
			//System.out.println("x: "+x+" y:"+y+"highest score"+LS+" Colour:"+newColour);
			return newColour;
		}
		
		private Color randomImitate(Color[][] grid, int x, int y) {
			Color newColour = grid[x][y];
			
			int count = 0;
			int randomiser = rand.nextInt(8);
			
			int mutation = rand.nextInt(400);
			
			for (int i = -1; i<2; i++) {
				 for(int j=-1; j<2; j++) {
					 if(!(i ==0 && j==0)) {
						 int cols = (x+i + col) % col;
						 int rows = (y+j + row) % row;
						 //If the randomiser matches the count select that neighbour to copy
						 if(randomiser == count) {
							 newColour = grid[cols][rows];
							 
						 }
						 count++;
					 
					 }

				 }
			}
			
			
			if(mutation == 0) {
				if(newColour == green) {
					newColour = red;
				}
				else if (newColour == red) {
					newColour = green;
				}
				
			} 
			//System.out.println("x: "+x+" y:"+y+"highest score"+LS+" Colour:"+newColour);
			
			return newColour;
		}
		
		private Color meanImitate(Color[][] grid, int x, int y) {
			Color newColour = grid[x][y];
			//Total score
			int TS = 0;
			//Mean score 
			int MS = 0;
			int smallestDistance= 100;
			//mutation
			int mutation = rand.nextInt(400);
			
			for (int i = -1; i<2; i++) {
				 for(int j=-1; j<2; j++) {
					 if(!(i ==0 && j==0)) {
						 int cols = (x+i + col) % col;
						 int rows = (y+j + row) % row;
						 
						 TS = TS + activeGridScore[cols][rows];
					 }
					 
				 }
			}
			
			MS = TS / 8;
			//Find the score closest to the average
			for (int i = -1; i<2; i++) {
				 for(int j=-1; j<2; j++) {
					 if(!(i ==0 && j==0)) {
						 int cols = (x+i + col) % col;
						 int rows = (y+j + row) % row;
						 int distance = Math.abs(MS - activeGridScore[cols][rows]);
						 
						 if(distance< smallestDistance){
							 smallestDistance = distance;
							 newColour = grid[cols][rows];
						 }	
					 }					 
				 }
			}
			
			if(mutation == 0) {
				if(newColour == green) {
					newColour = red;
				}
				else if (newColour == red) {
					newColour = green;
				}
				
			} 
			return newColour;	
		}
		
		private Color[][] memoryImitationRule(Color[][] grid, Color[][] newgrid) {
			rounds++;
			counter++;
			roundCounter.setText(rounds + "");
			
			endThread.rounds.add(rounds);
			endThread.coopPop.add(coopCount);
			endThread.defectPop.add(defectCount);
			
			//Chart building
			if(counter==50) {
				endRound = rounds;
				endThread.cycles.add(startRound+"-"+endRound);
				startRound = rounds;
				counter = 0;
			}
			
			int sumGridScore[][] = new int[col][row];
			//Compute the scores of all the cells
			//For Round 1
			if(rounds == 1) {
				for(int i=0; i<col; i++) {
					for(int j=0; j<row; j++) {
						int sumScore = score(grid,i,j);
						activeGridScore[i][j] = sumScore;
						sumGridScore[i][j] = activeGridScore[i][j];//Grid score to compare
						//System.out.println("i:"+i+"j:"+j+" "+sumGridScore[i][j]);
					}
				}
			}//For Round 2
			else if(rounds == 2) {
				for(int i=0; i<col; i++) {
					for(int j=0; j<row; j++) {
						int sumScore = score(grid,i,j);
						oneStepGridScore[i][j] = activeGridScore[i][j];
						activeGridScore[i][j] = sumScore;
						sumGridScore[i][j] = activeGridScore[i][j] + oneStepGridScore[i][j];
						//System.out.println("i:"+i+"j:"+j+" "+sumGridScore[i][j]);
					}
				}
			}//For round 3 and onwards
			else {
				for(int i=0; i<col; i++) {
					for(int j=0; j<row; j++) {
						int sumScore = score(grid,i,j);
						twoStepGridScore[i][j] = oneStepGridScore[i][j];
						oneStepGridScore[i][j] = activeGridScore[i][j];
						activeGridScore[i][j] = sumScore;
						sumGridScore[i][j] = activeGridScore[i][j] + oneStepGridScore[i][j] + twoStepGridScore[i][j];
						//System.out.println("i:"+i+"j:"+j+" "+sumGridScore[i][j]);
					}
				}
			}
			
			//imitate
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					newgrid[i][j] = memoryStandardImitate(grid,i,j,sumGridScore);
					
					//update the counter if the state has been changed
					if(grid[i][j] != newgrid[i][j]) {
						if(newgrid[i][j] == green) {
							//Change the count
							coopCount++;
							defectCount--;
							cooperatorCounter.setText(coopCount + "");
							defectorCounter.setText(defectCount + "");
						}
						else if (newgrid[i][j] == red) {
							//Change the count
							defectCount++;
							coopCount--;
							cooperatorCounter.setText(coopCount + "");
							defectorCounter.setText(defectCount + "");
						}
					}
				}
			}
			return newgrid;
			
		}
		
		private Color memoryStandardImitate(Color[][] grid, int x, int y, int[][] sumGridScore) {
			Color newColour = grid[x][y];
			//Largest score 
			int LS = 0;
			
			int mutation = rand.nextInt(400);
			
			for (int i = -1; i<2; i++) {
				for(int j=-1; j<2; j++) {
					if(!(i ==0 && j==0)) {
						int cols = (x+i + col) % col;
						int rows = (y+j + row) % row;
						 
						if(sumGridScore[cols][rows] > LS) {
							LS = sumGridScore[cols][rows];
							newColour = grid[cols][rows];
						}
					
					}
					
				}
			}
			 
			if(mutation == 0) {
				if(newColour == green) {
					newColour = red;
				}
				else if (newColour == red) {
					newColour = green;
				}
			}
				
			
			//System.out.println("x: "+x+" y:"+y+"highest score"+LS+" Colour:"+newColour);
			
			return newColour;
				
		}
		
		private void play(Graphics g) {
			spacing = 0;
			
			g.setColor(w);
			g.fillRect(0,0,Width,Height);
			
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {	
				click(g,i,j);		
				}	
			}
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					g.setColor(grid[i][j]);
					move(g,i,j);
					g.fillRect(spacing +i*resolution,spacing+ j*resolution,resolution-1*spacing ,resolution-1*spacing);
				}
			}
		}
		
		public void pause(Graphics g) {
			spacing = 0;
			
			g.setColor(w);
			g.fillRect(0,0,Width,Height);
			
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					g.setColor(grid[i][j]);
					move(g,i,j);
					click(g,i,j);
					g.fillRect(spacing +i*resolution,spacing+ j*resolution,resolution-1*spacing ,resolution-1*spacing);
				}	
			}
		}
		
		public void stop(Graphics g) {
			spacing = 1;
			
			g.setColor(w);
			g.fillRect(0,0,Width,Height);
			
			for(int i=0; i<col; i++) {
				for(int j=0; j<row; j++) {
					g.setColor(grid[i][j]);
					move(g,i,j);
					click(g,i,j);
					g.fillRect(spacing +i*resolution,spacing+ j*resolution,resolution-1*spacing ,resolution-1*spacing);
				}	
			}
		
		}
		
		public class task extends TimerTask  {
			//conwayRules
			//socialDistanceRule
			 
		     @Override
		     public void run() {
		    	 Color newGrid[][] = new Color[col][row];
		    	 
		    	 if(State == State.PLAY) {
		    		 grid =memoryImitationRule(grid, newGrid);
		    		 //Stop the game after 500 rounds
		    	 } 
		    	 
		    	 if(rounds == 502){
		    		 State = STATE.STOP;
		    		 Main.end = true;
		    	 }
		    	 
		     }
		}
		
		public void paintComponent(Graphics g)
		{
			switch(State){
			
		    case START:
		    	start(g);
		      break;  
		    case PLAY:
		    	play(g);
		      break; 
		    case PAUSE:
		    	pause(g);
		      break;
		    case STOP:
		    	stop(g);
		      break;
		    case RESET:
		    	if(beenReset == false) {
		    		coopCount = 0;
					defectCount = 0;
		    		for(int i=0; i<col; i++) {
						for(int j=0; j<row; j++) {
							//All the cells are assigned black at the start
							int number = rand.nextInt(2);
							grid[i][j] = states[number];		
							//Count the amount of states for each
							if(grid[i][j] == green) {
								coopCount++;
							}
							else if(grid[i][j] == red) {
								defectCount++;
							}		
						}
					}
		    		cooperatorCounter.setText(coopCount + "");
					defectorCounter.setText(defectCount + "");
		    		beenReset = true;	
		    	}
		    	start(g);
		      break;
		   default: 
			}		
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == play) {
			State = STATE.PLAY;
		}
		else if (e.getSource() == pause) {
			State = STATE.PAUSE;
		}
		else if (e.getSource() == reset) {
			beenReset = false;
			State = STATE.RESET;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == speed) {
			currentSpeed = speed.getValue();
			game.updateTimer();		
		}
	}
}


