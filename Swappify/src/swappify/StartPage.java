package swappify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class StartPage extends Panels{
    
    private JPanel titlePanel, selectionPanel, algorithmPanel,outlinePanel, buttonPanel;
    private JLabel titleLabel, selectLabel, outlineLabel, algorithmLabel, framesLabel, lengthLabel, rstringLabel; 
    public JTextField algorithmField, framesField, lengthField, rstringField;
    public JButton backButton, fifoButton, lruButton, optButton, scButton, escButton, lfuButton, mfuButton, allButton, generateButton, resetButton;
    
    
    public StartPage(){
    
    }
    
    @Override
    public void showComponents(){
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 20));
        
        /// title panel ////
        
        titlePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 110,0));
        titlePanel.setPreferredSize(new Dimension(1000, 110));
        titlePanel.setOpaque(false);
        
        titleLabel = new JLabel();
        titleLabel.setPreferredSize(new Dimension(300, 100));
        titleLabel.setIcon(smallLogoIcon);
        backButton = createButton(150, 40, "BACK", gray, white, 20);
        
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
        
        fifoButton = createButton(192, 60, "FIFO", darkgreen, white,white, 16);
        lruButton = createButton(192, 60, "LRU", darkgreen, white, white,16);
        optButton = createButton(192, 60, "OPT", darkgreen, white, white, 16);
        scButton = createButton(192, 60, "<html><body style='text-align:center;'>Second<br>Chance</body></html>", 
                darkgreen, white, white, 16);
        escButton = createButton(192, 60, "<html><body style='text-align:center;'>Enhanced<br>Second Chance</body></html>", 
                darkgreen, white, white, 16);
        lfuButton = createButton(192, 60, "LFU", darkgreen, white, white,16);
        mfuButton = createButton(192, 60, "MFU", darkgreen, white, white,16);
        allButton = createButton(192, 60, "ALL", darkgreen, white, white,16);

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
        
        generateButton = createButton(190, 40, "Generate", darkgreen, white, white,16);
        resetButton = createButton(190, 40, "Reset", red, white,white, 16);
        
        buttonPanel.add(generateButton);
        buttonPanel.add(resetButton);
        
        selectionPanel.add(selectLabel);
        selectionPanel.add(algorithmPanel);
        selectionPanel.add(outlineLabel);
        selectionPanel.add(outlinePanel);
        selectionPanel.add(buttonPanel);
        
        add(titlePanel);
        add(selectionPanel);
    }
}
