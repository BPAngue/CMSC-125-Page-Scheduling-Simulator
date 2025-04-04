package swappify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.Timer;

public class FIFO {
    
    public ArrayList<Integer> pageFrames;
    public ArrayList<Integer> pageNumberLabel;
    public ArrayList<String> hitMissLabel;
    public ArrayList<ArrayList<Integer>> pageFramesPerColumn;
    public Queue<Integer> queue = new LinkedList<>();
    public Set<Integer> pageSet = new HashSet<>();
    public ArrayList<Integer> referenceStringValues;
    public String referenceString;
    public Timer timer;
    public int time;
    public int pageFaults;
    public int numFrames = 0;
    public Draw draw;
    public JLabel timerLabel;
    public int seconds;
    public int minutes;
    
    public FIFO(ArrayList<Integer> referenceStringValues, ArrayList<Integer> pageFrames, ArrayList<Integer> pageNumberLabel, ArrayList<String> hitMissLabel, int numFrames, int pageFaults, Draw draw, ArrayList<ArrayList<Integer>> pageFramesPerColumn, JLabel timerLabel) {
        this.referenceStringValues = referenceStringValues;
        this.pageFrames = pageFrames;
        this.pageNumberLabel = pageNumberLabel;
        this.hitMissLabel = hitMissLabel;
        this.numFrames = numFrames;
        this.pageFaults = pageFaults;
        this.draw = draw;
        this.pageFramesPerColumn = pageFramesPerColumn;
        this.timerLabel = timerLabel;
    }
    
    public void startSimulation() {
        timer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (seconds == 60) {
                    seconds = 0;
                    minutes++;
                }
                String timeValue = String.format("%02d:%02d", minutes, seconds);
                timerLabel.setText("Timer: " + timeValue);
                
                if (time < referenceStringValues.size()) {
                    int page = referenceStringValues.get(time);
                    pageNumberLabel.add(page);
                    System.out.println("Time " + time + ": Processing page " + page); // for debugging
                    
                    // check if the page is already in the frame
                    if (!pageSet.contains(page)) {
                        pageFaults++;
                        System.out.println("Page fault! Page " + page + " not in frames."); // for debugging
                        
                        // if frames are full, remove the oldest page but replace it in the same position
                        if (pageFrames.size() == numFrames) {
                            int removedPage = queue.poll();
                            int indexToReplace = pageFrames.indexOf(removedPage); // Get index of the oldest page
                            pageSet.remove(removedPage);
                            pageFrames.set(indexToReplace, page); // Replace the page in the same position
                            queue.add(page);
                            recordSnapShot();
                            System.out.println("Removed page: " + removedPage); // for debugging
                        } else {
                            // Add page normally if there's still space
                            pageFrames.add(page);
                            queue.add(page);
                            recordSnapShot();
			}
                        
                        // Add the new page to the set
                        pageSet.add(page);
                        hitMissLabel.add("Miss");
                        draw.totalPageFault = pageFaults;
                    } else {
                        System.out.println("Page hit! Page " + page + " already in frames."); // for debugging
                        hitMissLabel.add("Hit");
                        recordSnapShot();
                        draw.totalPageFault = pageFaults;
                    }
					
                    // display current state of the page frames
                    printPageFrames();		
                    time++;
                    seconds++;
                } else {
                    timer.stop();
                    draw.nextStep();
                    draw.totalPageFault = pageFaults;
                    
                    // for debugging
                    System.out.println("\nSimulation Complete!");
                    System.out.println("Total Page Faults: " + pageFaults);
                }
            }
        });
        timer.start();
    }
    
    public void printPageFrames() {
        draw.repaint();
        draw.nextStep();
        
        // for debugging
	System.out.print("Current Frames: ");
	for (int frame : pageFrames) {
            System.out.print(frame + " ");
        }
	System.out.println();
	System.out.println();
    }
    
    public void recordSnapShot() {
        ArrayList<Integer> snapshot = new ArrayList<>();
        for (int i = 0; i < numFrames; i++) {
            if (i < pageFrames.size()) {
                snapshot.add(pageFrames.get(i));
            } else {
                snapshot.add(null); // Fill empty slots with nulls
            }
        }
        pageFramesPerColumn.add(snapshot);
        
        // for debugging
        System.out.println("Snapshot: " + pageFramesPerColumn);
    }
}
