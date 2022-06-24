package spatial;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Click implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {
		GUI.NCX = e.getX();
		GUI.NCY = e.getY();

	}

	@Override
	public void mousePressed(MouseEvent e) {
		GUI.NCX = e.getX();
		GUI.NCY = e.getY();	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		
	}

}

