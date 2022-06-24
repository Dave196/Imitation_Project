package spatial;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class Move implements MouseMotionListener {

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		GUI.MX = e.getX();
		GUI.MY = e.getY();
		
		
		
	}

}
