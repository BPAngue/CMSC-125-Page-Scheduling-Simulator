package swappify;

import java.util.ArrayList;


public class Simulator {
    
    private final ArrayList<String> pages = new ArrayList<>();
    
    private String algorithm;
    private int frames, length;
    
    public Simulator(){
    
    }
    
    public void addPage(String page){
        pages.add(page);
    }
    
    public ArrayList<String> getPages(){
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
}
