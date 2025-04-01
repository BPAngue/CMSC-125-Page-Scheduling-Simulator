import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JFrame;
import java.util.Set;
import java.util.HashSet;

public class FIFO {
	
	public static Scanner input = new Scanner(System.in);
	public static Queue<Integer> pageQueue = new LinkedList<>();
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
						
						// if the queue is full, remove the oldest page
						if (pageQueue.size() == numFrames) {
							int removedPage = pageQueue.poll();
							pageSet.remove(removedPage);
							System.out.println("Removed page: " + removedPage);
						}
						
						// add the new page to the queue and set
						pageQueue.add(page);
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
		for (int frame : pageQueue) {
			System.out.print(frame + " ");
		}
		System.out.println();
		System.out.println();
	}
}