package swappify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartPage extends Panels implements ActionListener{
    
    private JPanel titlePanel, selectionPanel, algorithmPanel,outlinePanel, buttonPanel;
    private JLabel titleLabel, selectLabel, outlineLabel, algorithmLabel, framesLabel, lengthLabel, rstringLabel, errorLabel; 
    public JTextField algorithmField, framesField, lengthField, rstringField;
    public JButton backButton, fifoButton, lruButton, optButton, scButton, escButton, lfuButton, mfuButton, allButton, 
            generateButton, resetButton, randomButton, textButton;
    
    public ArrayList<String> stringDetails = new ArrayList<>();
    
    Simulator simulator;
    
    public StartPage(){
    
    }
    
    @Override
    public void showComponents(){
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 15));
        
        /// title panel ////
        
        titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 110,0));
        titlePanel.setPreferredSize(new Dimension(1000, 100));
        titlePanel.setOpaque(false);
        
        titleLabel = new JLabel();
        titleLabel.setPreferredSize(new Dimension(300, 100));
        titleLabel.setIcon(smallLogoIcon);
        backButton = createButton(150, 40, "BACK", gray, white,20, this);
        
        titlePanel.add(titleLabel);
        titlePanel.add(backButton);
        
        /// selection panel ///
        
        selectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,5));
        selectionPanel.setPreferredSize(new Dimension (900, 570));
        selectionPanel.setBackground(green);
        selectionPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 1), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(10, 10, 10,10))));
        selectLabel = createLabel(822, 40, white, "Select Algorithm", 16f);
        
        /// algorithm panel ///
        
        algorithmPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        algorithmPanel.setOpaque(false);
        algorithmPanel.setPreferredSize(new Dimension(870, 150));
        
        fifoButton = createButton(192, 60, "FIFO", darkgreen, white,white, 16, this);
        lruButton = createButton(192, 60, "LRU", darkgreen, white, white,16, this);
        optButton = createButton(192, 60, "OPT", darkgreen, white, white, 16, this);
        scButton = createButton(192, 60, "<html><body style='text-align:center;'>Second<br>Chance</body></html>", 
                darkgreen, white, white, 16, this);
        escButton = createButton(192, 60, "<html><body style='text-align:center;'>Enhanced<br>Second Chance</body></html>", 
                darkgreen, white, white, 16, this);
        lfuButton = createButton(192, 60, "LFU", darkgreen, white, white,16, this);
        mfuButton = createButton(192, 60, "MFU", darkgreen, white, white,16, this);
        allButton = createButton(192, 60, "ALL", darkgreen, white, white,16, this);

        algorithmPanel.add(fifoButton);
        algorithmPanel.add(lruButton);
        algorithmPanel.add(optButton);
        algorithmPanel.add(scButton);
        algorithmPanel.add(escButton);
        algorithmPanel.add(lfuButton);
        algorithmPanel.add(mfuButton);
        algorithmPanel.add(allButton);
        
        
        outlineLabel = createLabel(740, 40, white, "Computation Outline", 16f);
        outlineLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 0));
        
        outlinePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        outlinePanel.setPreferredSize(new Dimension(700, 220));
        outlinePanel.setBackground(darkgreen);
        
        algorithmLabel = createLabel(330, 40, white,  "Algorithm:", 16f);
        algorithmField = createField(330, 40, darkgreen, 16f);
        algorithmField.setEditable(false);
        
        framesLabel = createLabel(330, 40, white,  "Enter Number of Frames:", 16f);
        framesField = createField(330, 40, darkgreen, 16f);
        
        lengthLabel = createLabel(330, 40, white,  "Reference Length:", 16f);
        lengthField = createField(330, 40, darkgreen, 16f);
        
        rstringLabel = createLabel(330, 40, white,  "<html>Enter Reference String<br>(Separated by spaces):</html>", 16f);
        rstringField = createField(330, 40, darkgreen, 16f);
        
        outlinePanel.add(algorithmLabel);
        outlinePanel.add(algorithmField);
        outlinePanel.add(framesLabel);
        outlinePanel.add(framesField);
        outlinePanel.add(lengthLabel);
        outlinePanel.add(lengthField);
        outlinePanel.add(rstringLabel);
        outlinePanel.add(rstringField);
        
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setPreferredSize(new Dimension(900, 60));
        buttonPanel.setOpaque(false);
        
        generateButton = createButton(190, 40, "Generate", darkgreen, white, white,16, this);
        resetButton = createButton(190, 40, "Reset", red, white,white, 16, this);
        randomButton = createButton(190, 40, "Random Input", darkgreen, white, white,16, this);
        textButton = createButton(190, 40, "Text File Input", darkgreen, white, white,16, this);
        
        buttonPanel.add(randomButton);
        buttonPanel.add(textButton);
        buttonPanel.add(generateButton);
        buttonPanel.add(resetButton);
        
        selectionPanel.add(selectLabel);
        selectionPanel.add(algorithmPanel);
        selectionPanel.add(outlineLabel);
        selectionPanel.add(outlinePanel);
        selectionPanel.add(buttonPanel);
        
        errorLabel = createLabel(1000, 20, white, "", 20);
        errorLabel.setHorizontalAlignment(center);
        
        add(titlePanel);
        add(selectionPanel);
        add(errorLabel);
    }

    private void clearInputs(){
        algorithmField.setText("");
        framesField.setText("");
        lengthField.setText("");
        rstringField.setText("");
        errorLabel.setText("");
    }
    
    private void generateRandomInput(){
        Random r = new Random();
        int randomFrames = r.nextInt(3, 11);
        int randomLength = r.nextInt(10, 40);
        
        framesField.setText(String.valueOf(randomFrames));
        lengthField.setText(String.valueOf(randomLength));
        
        String randomString = "";
        
        for (int i=0; i<randomLength; i++){
            randomString = randomString + String.valueOf(r.nextInt(0, 21)) + " ";
        }
        
        rstringField.setText(randomString);
    }
    
    private File uploadFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("File selected: " + selectedFile.getAbsolutePath());
            return selectedFile;
        } else {
            System.out.println("File selection cancelled.");
            return null;
        }
    }
    
     private void readFile(File file) {
         
        if (file == null) {
            System.out.println("No file to read.");
            return;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                stringDetails.add(line.trim());
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }  
    }
     
    private void generateTextInput(){
        readFile(uploadFile());
        
        /* for debugging 
        for(String i : stringDetails){
            System.out.println(i);
        } */
        
        framesField.setText(stringDetails.get(0));
        lengthField.setText(stringDetails.get(1));
        rstringField.setText(stringDetails.get(2));
    }
    
    public boolean isNumeric(String string) {
            try {
                Double.parseDouble(string);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
    }
    
    private boolean validRefString(){
        String rstring = rstringField.getText();
        int length = rstring.length();
        
        for (int i=0; i<length; i++){
            if(!(Character.isDigit(rstring.charAt(i)) || Character.isSpaceChar(rstring.charAt(i)))){
                return false;
            }
        }
        return true;
    }
    
    public boolean validateInput(){
        String algorithm = algorithmField.getText();
        String frames = framesField.getText();
        String length = lengthField.getText();
        String rstring = rstringField.getText();
        
        if (((!algorithm.isEmpty() || !algorithm.isBlank())  && (!frames.isEmpty() || !frames.isBlank())
                && (!length.isEmpty() || !length.isBlank()) && (!rstring.isEmpty() || !rstring.isBlank()) 
                && isNumeric(frames) && isNumeric(length)) && validRefString()
                && (Integer.parseInt(frames) <= 10 && Integer.parseInt(frames) >= 3) 
                && (Integer.parseInt(length) <= 40 && Integer.parseInt(length) >= 10) 
                && (Integer.parseInt(length) <= 40 && Integer.parseInt(length) >= 10) ) {
            
            return true;
        }
        else{
            return false;
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fifoButton){
            errorLabel.setText("");
            algorithmField.setText(fifoButton.getText());
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/
        }
        else if(e.getSource()==lruButton){
            errorLabel.setText("");
            algorithmField.setText(lruButton.getText());
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/
            
        }
        else if(e.getSource()==optButton){
            errorLabel.setText("");
            algorithmField.setText(optButton.getText());
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/

        }
        else if(e.getSource()==scButton){
            errorLabel.setText("");
            algorithmField.setText("Second Chance");
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/

        }
        else if(e.getSource()==escButton){
            errorLabel.setText("");
            algorithmField.setText("Enhanced Second Chance");
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/
 
        }
        else if(e.getSource()==lfuButton){
            errorLabel.setText("");
            algorithmField.setText(lfuButton.getText());
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/
        }
        else if(e.getSource()==mfuButton){
            errorLabel.setText("");
            algorithmField.setText(mfuButton.getText());
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/

        }
        else if(e.getSource()==allButton){
            errorLabel.setText("");
            algorithmField.setText(allButton.getText());
            /*simulator.setAlgorithm(algorithmField.getText());
            simulator.setNumberOfFrames(framesField.getText());
            simulator.setReferenceLength(lengthField.getText());*/

        }
        else if(e.getSource()==randomButton){
            errorLabel.setText("");
            generateRandomInput();
        }
        else if(e.getSource()==textButton){
            errorLabel.setText("");
            generateTextInput();
        }
        else if(e.getSource()==generateButton && !validateInput()){
            errorLabel.setText("Invalid. Please check your inputs.");
        }
        else if(e.getSource()==resetButton || e.getSource()==backButton){
            clearInputs();
        }
    }
}
