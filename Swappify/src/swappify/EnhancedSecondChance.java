package swappify;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

public class EnhancedSecondChance implements Runnable {
    
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
    public Queue<HashMap<Integer, boolean[]>> queue = new LinkedList<>();
    public Timer timer;
    public int time;
    public int seconds = 0;
    public int minutes = 0;
    public int simulationSpeed;
    
    public EnhancedSecondChance(ArrayList<Integer> referenceStringValues, ArrayList<Integer> pageFrames, 
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
                    System.out.println("Time " + time + ": Processing page " + page); // for debugging
                    
                    // check if the page is already in the frame
                    if (!pageFrames.contains(page)) {
                        pageFaults++;
                        System.out.println("Page fault! Page " + page + " not in frames."); // for debugging
                        
                        // if frames are full, remove the oldest page but replace it in the same position
                        if (pageFrames.size() == numFrames) {
                            secondChanceReplacement(page);
                            recordSnapShot();
                        } else {
                            // add page normally if there is still space
                            pageFrames.add(page);
                            HashMap<Integer, boolean[]> newEntry = new HashMap<>();
                            boolean bits[] = new boolean[2];
                            bits[0] = false; // reference bit
                            bits[1] = new Random().nextBoolean();
                            newEntry.put(page, bits);
                            draw.setPageModification(page, bits[1]);
                            queue.add(newEntry);
                            recordSnapShot();
                        }
                        hitMissLabel.add("Miss");
                        draw.totalPageFault = pageFaults;
                    } else {
                        System.out.println("Page hit! Page " + page + " already in frames."); // for debugging
                        
                        // update reference bit of page
			for (HashMap<Integer, boolean[]> entry : queue) {
                            if (entry.containsKey(page)) {
				entry.get(page)[0] = true;
				break;
                            }
			}
                        hitMissLabel.add("Hit");
                        recordSnapShot();
                        draw.totalPageFault = pageFaults;
                    }
                    // for debugging
                    for (HashMap<Integer, boolean[]> entry : queue) {
                        for (Map.Entry<Integer, boolean[]> e2 : entry.entrySet()) {
                            System.out.println("Page " + e2.getKey() + " => Ref: " + (e2.getValue()[0] ? 1 : 0) + ", Mod: " + (e2.getValue()[1] ? 1 : 0));
			}
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
        // System.out.println("Snapshot: " + pageFramesPerColumn);
    }
    
    public void secondChanceReplacement(int page) {
        List<HashMap<Integer, boolean[]>> queueList = new ArrayList<>(queue);
        int size = queueList.size();
	
        int[][] priorities = {
            {0, 0}, {0, 1}, {1, 0}, {1, 1}
        };
        
        for (int[] prio : priorities) {
            for (int i = 0; i < size * 2; i++) {
                int idx = i % size;
                HashMap<Integer, boolean[]> entry = queueList.get(idx);
                
                int pageEntry = -1;
                boolean[] bits = null;
                
                for (Map.Entry<Integer, boolean[]> ef : entry.entrySet()) {
                    pageEntry = ef.getKey();
                    bits = ef.getValue();
                }
                
                if (bits[0] == (prio[0] == 1) && bits[1] == (prio[1] == 1)) {
                    if (bits[0]) {
                        bits[0] = false;
                        continue;
                    }
                    
                    int indexToReplace = pageFrames.indexOf(pageEntry);
                    pageFrames.set(indexToReplace, page);
                    
                    queueList.remove(idx);
                    HashMap<Integer, boolean[]> newEntry = new HashMap<>();
                    boolean[] newBits = new boolean[2];
                    newBits[0] = false;
                    newBits[1] = new Random().nextBoolean();
                    newEntry.put(page, newBits);
                    draw.setPageModification(page, newBits[1]);
                    queueList.add(newEntry);
                    
                    queue.clear();
                    queue.addAll(queueList);
                    return;
                }
            }
        }
    }
    
    public void stopSimulation() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
            // System.out.println("Simulation stopped!"); // for debugging
        }
    }
    
    public void restartSimulation() {
        // clear local values
        queue.clear();
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
