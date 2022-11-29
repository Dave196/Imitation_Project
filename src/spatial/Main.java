 package spatial;

public class Main {
	
	public GUI gui = new GUI();
	static Thread game;
	public static boolean end = false;
	
	public static void main(String[] args) {
       //Run the simulation
       Thread newGame = new Thread(new gameThread());
       newGame.setDaemon(true);
       newGame.start();
       
       //Present the results
       Thread endGame = new Thread(new endThread());
       
       //waits for first thread to run 
       try {
		newGame.join();
		
       } catch (InterruptedException e) {
    	   
    	   // TODO Auto-generated catch block
    	   e.printStackTrace();
       }
       //Produce a graph
       endGame.start();
       
	}
	
}
