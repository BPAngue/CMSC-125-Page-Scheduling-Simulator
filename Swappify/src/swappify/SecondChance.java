package swappify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import javax.swing.JLabel;
import javax.swing.Timer;


public class SecondChance{
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
    
    public SecondChance(ArrayList<Integer> referenceStringValues,
            ArrayList<Integer> pageFrames, ArrayList<Integer> pageNumberLabel,
            ArrayList<String> hitMissLabel, int numFrames, int pageFaults,
            Draw draw, ArrayList<ArrayList<Integer>> pageFramesPerColumn,
            JLabel timerLabel) {
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
    
    
    
}
