package swappify;

import java.util.ArrayList;


public class Simulator {
    
    private final ArrayList<Integer> pages = new ArrayList<>();
    private String algorithm;
    private int frames, length;
    private int totalPageFault;
    
    public Simulator(){
    
    }
    
    public void addPage(String page){
        pages.add(Integer.valueOf(page));
    }
    
    public ArrayList<Integer> getPages(){
        return pages;
    }
    
    public void setAlgorithm(String algorithm){
        this.algorithm = algorithm;
    }
    
    public String getAlgorithm(){
        return algorithm;
    }
    
    public void setNumberOfFrames(String frames){
        this.frames = Integer.parseInt(frames);
    }
    
    public int getNumberOfFrames(){
        return frames;
    }
    
    public void setReferenceLength(String length){
        this.length = Integer.parseInt(length);
    }
    
    public int getReferenceLength(){
        return length;
    }
    
    public void setTotalPageFault(int totalPageFault) {
        this.totalPageFault = totalPageFault;
    }
    
    public int getTotalPageFault() {
        return totalPageFault;
    }
    
    // clear inputs
    public void clearReferenceString() {
        pages.clear();
    }
    
    public void clearNumberOfFrames() {
        frames = 0;
    }
    
    public void clearReferenceLength() {
        length = 0;
    }
    
    public void clearAlgorithm() {
        algorithm = "";
    }
}
