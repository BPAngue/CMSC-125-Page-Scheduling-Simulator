import java.util.Scanner;
import javax.swing.*;

public class Main {
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		Draw draw = new Draw();
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1000, 750);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		System.out.print("Enter length of reference string: ");
		draw.referenceStringLength = input.nextInt();
		
		System.out.print("Enter number of page frames: ");
		draw.numberOfPageFrames = input.nextInt();
		
		frame.add(draw);
		frame.setVisible(true);
	}
	
}