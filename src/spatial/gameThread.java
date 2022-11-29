package spatial;

public class gameThread implements Runnable {
	public GUI gui = new GUI();

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			//Calls a repaint method on a gui
			 gui.repaint();	
			 if (Main.end ==true) {
				 System.out.println("Simulation is over");
				 return;
				 
			 }
		}
	}
}
