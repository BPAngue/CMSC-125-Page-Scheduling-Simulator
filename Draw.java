import javax.swing.*;
import java.awt.*;

public class Draw extends JPanel {
	
	public int referenceStringLength = 0;
	public int numberOfPageFrames = 0;
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int y = 10;
		int x = 20;
		
		// simulator gui
		for (int i = 0; i < referenceStringLength * numberOfPageFrames; i++) {
			g.drawRect(x, y, 50, 40); // (x, y, width, height)
			y = y + 40;
			if ((i + 1) % numberOfPageFrames == 0) {
				x = x + 70;
				y = 10;
			}
		}
	}
}