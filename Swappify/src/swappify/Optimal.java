package swappify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

public class Optimal implements Runnable {
    
    // constructor set variables
    public ArrayList<Integer> referenceStringValues;
    public ArrayList<Integer> pageFrames;
    public ArrayList<Integer> pageNumberLabel;
    public ArrayList<String> hitMissLabel;
    public int numFrames = 0;
    public int pageFaults;
    public Draw draw;
    public ArrayList<ArrayList<Integer>> pageFramesPerColumn;
    public JLabel timerLabel;
    public JButton pdfButton, imgButton, restartButton, plusButton, minusButton;
    
    // local variables
    public Set<Integer> pageSet = new HashSet<>();
    public String referenceString;
    public Timer timer;
    public int time;
    public int seconds = 0;
    public int minutes = 0;
    public int simulationSpeed;
    
    public Optimal(ArrayList<Integer> referenceStringValues, ArrayList<Integer> pageFrames, 
            ArrayList<Integer> pageNumberLabel, ArrayList<String> hitMissLabel, 
            int numFrames, int pageFaults, Draw draw, ArrayList<ArrayList<Integer>> pageFramesPerColumn, 
            JLabel timerLabel, JButton pdfButton, JButton imgButton, JButton restartButton, JButton plusButton, JButton minusButton, int simulationSpeed) {
        this.referenceStringValues = referenceStringValues;
        this.pageFrames = pageFrames;
        this.pageNumberLabel = pageNumberLabel;
        this.hitMissLabel = hitMissLabel;
        this.numFrames = numFrames;
        this.pageFaults = pageFaults;
        this.draw = draw;
        this.pageFramesPerColumn = pageFramesPerColumn;
        this.timerLabel = timerLabel;
        this.pdfButton = pdfButton;
        this.imgButton = imgButton;
        this.restartButton = restartButton;
        this.plusButton = plusButton;
        this.minusButton = minusButton;
        this.simulationSpeed = simulationSpeed;
    }
    
    public void startSimulation() {
        timer = new Timer(simulationSpeed, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (time < referenceStringValues.size()) {
                    /*if (seconds == 60) {
                        seconds = 0;
                        minutes++;
                    }*/
                    
                    int elapsedMillis = (time + 1) * simulationSpeed;
                    minutes = (elapsedMillis / 1000) / 60;
                    seconds = (elapsedMillis / 1000) % 60;
                    String timeValue = String.format("%02d:%02d", minutes, seconds);
                    timerLabel.setText("Timer: " + timeValue);
                    
                    int page = referenceStringValues.get(time);
                    pageNumberLabel.add(page);
                    // System.out.println("Time " + time + ": Processing page " + page); // for debugging
                    
                    // check if the page is already in the frame
                    if (!pageFrames.contains(page)) {
                        pageFaults++;
                        // System.out.println("Page fault! Page " + page + " not in frames."); // for debugging
                        
                        // if frames are full, find the optimal page to replace
                        if (pageFrames.size() == numFrames) {
                            int indexToReplace = findOptimalReplacementIndex(time);
                            int removedPage = pageFrames.get(indexToReplace);
                            pageFrames.set(indexToReplace, page);
                            recordSnapShot();
                            // System.out.println("Removed page: " + removedPage); // for debugging
                        } else {
                            // Add page normally if there's still space
                            pageFrames.add(page);
                            recordSnapShot();
			}
                        
                        hitMissLabel.add("Miss");
                        draw.totalPageFault = pageFaults;
                    } else {
                        // System.out.println("Page hit! Page " + page + " already in frames."); // for debugging
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
                    
                    // enable buttons
                    pdfButton.setEnabled(true);
                    imgButton.setEnabled(true);
                    restartButton.setText("Restart");
                    plusButton.setEnabled(true);
                    minusButton.setEnabled(true);
                    
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
        
        /*// for debugging
	System.out.print("Current Frames: ");
	for (int frame : pageFrames) {
            System.out.print(frame + " ");
        }
	System.out.println();
	System.out.println();*/
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
        // System.out.println("Snapshot: " + pageFramesPerColumn);
    }
    
    public int findOptimalReplacementIndex(int currentIndex) {
        int farthestUse = -1, indexToReplace = -1;
        
        for (int i = 0; i < pageFrames.size(); i++) {
            int page = pageFrames.get(i);
            int nextUse = Integer.MAX_VALUE;
            
            // check when this page will be used again
            for (int j = currentIndex + 1; j < referenceStringValues.size(); j++) {
                if (referenceStringValues.get(j) == page) {
                    nextUse = j;
                    break;
                }
            }
            
            // replace the page that is used farthest in the future (or never used again)
            if (nextUse > farthestUse) {
                farthestUse = nextUse;
                indexToReplace = i;
            }
        }
        return indexToReplace;
    }
    
    public void stopSimulation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            // System.out.println("Simulation stopped!"); // for debugging
        }
    }
    
    public void restartSimulation() {
        // clear local values
        pageSet.clear();
        time = 0;
        seconds = 0;
        minutes = 0;
            
        // clear all frame and label data
        pageFrames.clear();
        pageNumberLabel.clear();
        hitMissLabel.clear();
        pageFramesPerColumn.clear();
            
        // clear draw Panel
        draw.clearValues();
        draw.removeAll();
        draw.revalidate();
        draw.repaint();
    }

    @Override
    public void run() {
        startSimulation();
    }
}
