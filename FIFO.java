import java.util.*;
import javax.swing.Timer;
import javax.swing.*;
import java.awt.event.*;

public class FIFO {
	
	public static Scanner input = new Scanner(System.in);
	public static ArrayList<Integer> pageFrames = new ArrayList<>();
	public static Queue<Integer> queue = new LinkedList<>();
	public static Set<Integer> pageSet = new HashSet<>();
	public static ArrayList<Integer> referenceStringValues = new ArrayList<>();
	public static String referenceString;
	public static Timer timer;
	public static String[] tokens;
	public static int numFrames = 0;
	public static int time = 0;
	public static int pageFaults = 0;
	
	public static void main(String[] args) {
		System.out.print("Enter number of frames: ");
		numFrames = input.nextInt();
		input.nextLine();
		
		System.out.print("Enter reference string: ");
		referenceString = input.nextLine();
	
		tokens = referenceString.split(" ");
		
		// store reference string into an array list
		for (String token : tokens) {
			try {
				referenceStringValues.add(Integer.parseInt(token));
			} catch (NumberFormatException e) {
				System.out.println("Invalid number: " + token);
			}
		}
		
		// Create a JFrame to keep the program running
		JFrame frame = new JFrame("FIFO Simulation");
		frame.setSize(300, 100);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		startSimulation();
	}
	
	public static void startSimulation() {
		timer = new Timer(1000, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if (time < referenceStringValues.size()) {
					int page = referenceStringValues.get(time);
					System.out.println("Time " + time + ": Processing page " + page);
					
					// check if the page is already in the frame
					if (!pageSet.contains(page)) {
						pageFaults++;
						System.out.println("Page fault! Page " + page + " not in frames.");
						
						// if frames are full, remove the oldest page but replace it in the same position
						if (pageFrames.size() == numFrames) {
							int removedPage = queue.poll();
							int indexToReplace = pageFrames.indexOf(removedPage); // Get index of the oldest page
							pageSet.remove(removedPage);
							pageFrames.set(indexToReplace, page); // Replace the page in the same position
							queue.add(page);
							System.out.println("Removed page: " + removedPage);
						} else {
							// Add page normally if there's still space
							pageFrames.add(page);
							queue.add(page);
						}
						
						// Add the new page to the set
						pageSet.add(page);
					} else {
						System.out.println("Page hit! Page " + page + " already in frames.");
					}
					
					// display current state of the page frames
					printPageFrames();
					
					time++;
				} else {
					timer.stop();
					System.out.println("\nSimulation Complete!");
					System.out.println("Total Page Faults: " + pageFaults);
				}
			}
		});
		timer.start();
	}
	
	public static void printPageFrames() {
		System.out.print("Current Frames: ");
		for (int frame : pageFrames) {
			System.out.print(frame + " ");
		}
		System.out.println();
		System.out.println();
	}
}
