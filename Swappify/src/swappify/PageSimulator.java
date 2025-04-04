package swappify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PageSimulator extends Panels implements ActionListener{
    
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, framePanel, footer, speedPanel, infoPanel, simulatorPanel, pageRefPanel, numPageFramePanel;
    private JLabel logoLabel, titleLabel, timerLabel, pageRefValues, pageRefValuesTitle, numPageFrameValue;
    private JButton pdfButton, imgButton, restartButton, plusButton, minusButton;
    public JButton backButton;
    private JTextField speedTextField;
    private Simulator simulator;
    
    // for simulating
    private Draw draw;
    private ArrayList<Integer> pageNumberLabel = new ArrayList<>();
    private ArrayList<Integer> pageFrames = new ArrayList<>();
    private ArrayList<String> hitMissLabel = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumn = new ArrayList<>();
    private int totalPageFault;
    private Object currentSimulator;
    
    public PageSimulator(Simulator simulator){
        this.simulator = simulator;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 0,0));
        
        header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,25));
        header.setPreferredSize(new Dimension (1000, 200));
        header.setOpaque(false);
        
        logoLabel = new JLabel();
        logoLabel.setIcon(smallLogoIcon);
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        leftPanel.setPreferredSize(new Dimension(330, 200));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,10));
        centerPanel.setPreferredSize(new Dimension(330, 200));
        centerPanel.setOpaque(false);
        
        titleLabel = createLabel(330, 80, white, "" + " " + simulator.getAlgorithm(), 20);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        titleLabel.setHorizontalAlignment(center);
        
        pdfButton = createButton(135, 40, "Save as PDF", darkgreen, white, white,14, null);
        imgButton = createButton(135, 40, "Save as Image", darkgreen, white, white,14, null);
        imgButton.addActionListener(this);
        
        centerPanel.add(titleLabel);
        centerPanel.add(pdfButton);
        centerPanel.add(imgButton);
        
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 45,50));
        rightPanel.setPreferredSize(new Dimension(330, 200));
        rightPanel.setOpaque(false);
        
        backButton = createButton(150, 40, "BACK", gray, white,20, null);
        
        timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setPreferredSize(new Dimension(150, 40));
        timerPanel.setBorder(BorderFactory.createLineBorder(white, 1));
        timerPanel.setBackground(darkgreen);
        
        timerLabel = createLabel(110, 25, white, "Timer: 00:00", 16f);
        titleLabel.setHorizontalAlignment(center);
        timerPanel.add(timerLabel);
        
        rightPanel.add(backButton);
        rightPanel.add(timerPanel);
        
        // main simulator panel
        framePanel = new JPanel();
        framePanel.setPreferredSize(new Dimension(900, 470));
        framePanel.setBackground(green);
        framePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 1), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(10, 10, 10,10))));
        
        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        infoPanel.setBackground(Color.white);
        int infoPanelHeight;
        
        pageRefPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pageRefPanel.setBackground(Color.white);
        pageRefValuesTitle = new JLabel("Page reference: ");
        pageRefValuesTitle.setFont(archivoblack.deriveFont(20f));
        pageRefValuesTitle.setForeground(new Color(0, 128, 0));
        pageRefPanel.add(pageRefValuesTitle);
        
        int count = 0;
        for (int page : simulator.getPages()) {
            String pageText = "" + page;
            if (count < simulator.getPages().size() - 1) {
                pageText += ",";
            }
            
            pageRefValues = new JLabel(pageText);
            pageRefValues.setFont(archivoblack.deriveFont(13f));
            pageRefValues.setForeground(new Color(0, 128, 0));
            pageRefPanel.add(pageRefValues);
            count++;
        }
        
        numPageFramePanel = new JPanel();
        numPageFramePanel.setBackground(Color.white);
        
        numPageFrameValue = new JLabel("No. of Page frame: " + simulator.getNumberOfFrames());
        numPageFrameValue.setFont(archivoblack.deriveFont(20f));
        numPageFrameValue.setForeground(new Color(0, 128, 0));
        numPageFramePanel.add(numPageFrameValue);
        
        infoPanel.add(pageRefPanel);
        infoPanel.add(numPageFramePanel);
        
        infoPanelHeight = pageRefPanel.getPreferredSize().height + numPageFramePanel.getPreferredSize().height;
        infoPanel.setPreferredSize(new Dimension(860, infoPanelHeight));
        pageRefPanel.setPreferredSize(new Dimension(430, infoPanelHeight));
        numPageFramePanel.setPreferredSize(new Dimension(430, infoPanelHeight));
        
        // change this with the drawing panel
        draw = new Draw(simulator.getPages(), pageFramesPerColumn, hitMissLabel, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFault);
        draw.setPreferredSize(new Dimension(860, 420 - infoPanelHeight));
        draw.setBackground(Color.yellow);
        
        framePanel.add(infoPanel);
        framePanel.add(draw);
        
        header.add(leftPanel);
        header.add(centerPanel);
        header.add(rightPanel);
        
        footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 100,5));
        footer.setPreferredSize(new Dimension(1000, 60));
        footer.setOpaque(false);
        
        speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        speedPanel.setPreferredSize(new Dimension(320, 60));
        speedPanel.setOpaque(false);
        
        minusButton = createButton(60,40, "-", red, white,white, 20,1, this);
        plusButton = createButton(60,40, "+", green, white, white, 20, 1, this);
        speedTextField = createField(200, 40, darkgreen, 16);
        speedTextField.setEditable(false);
        speedTextField.setText("SPEED");
        speedTextField.setHorizontalAlignment(center);
        
        speedPanel.add(minusButton);
        speedPanel.add(speedTextField);
        speedPanel.add(plusButton);
        
        restartButton = createButton(150,40, "Restart", red, white,white, 16,1, null);
        
        footer.add(speedPanel);
        footer.add(restartButton);
        
        add(header);
        add(framePanel);
        add(footer);
    }
    
    public void startSimulation(String algorithm) {
        switch(algorithm) {
            case "FIFO":
                currentSimulator = new FIFO(simulator.getPages(), pageFrames, pageNumberLabel, hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, pageFramesPerColumn, timerLabel);
                ((FIFO) currentSimulator).startSimulation();
                break;
            case "LRU":
                currentSimulator = new LRU(simulator.getPages(), pageFrames, pageNumberLabel, hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, pageFramesPerColumn, timerLabel);
                ((LRU) currentSimulator).startSimulation();
                break;
            case "OPT":
                currentSimulator = new Optimal(simulator.getPages(), pageFrames, pageNumberLabel, hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, pageFramesPerColumn, timerLabel);
                ((Optimal) currentSimulator).startSimulation();
                break;
            case "LFU":
                currentSimulator = new LFU(simulator.getPages(), pageFrames, pageNumberLabel, hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, pageFramesPerColumn, timerLabel);
                ((LFU) currentSimulator).startSimulation();
                break;
            case "MFU":
                currentSimulator = new MFU(simulator.getPages(), pageFrames, pageNumberLabel, hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, pageFramesPerColumn, timerLabel);
                ((MFU) currentSimulator).startSimulation();
                break;
        }
    }

    private File saveAsPNG(String fileName) throws IOException {
        // Capture the content of framePanel as an image
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.printAll(g2d);
        g2d.dispose();
    
        // Save the image as a PNG file
        File pngFile = new File(fileName + ".png");
        ImageIO.write(image, "PNG", pngFile);
        System.out.println("Saved as PNG: " + pngFile.getAbsolutePath());
        
        return pngFile;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == imgButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());

            String fileName = timestamp + "_PG";
            try {
                File pngFile = saveAsPNG(fileName);
                JOptionPane.showMessageDialog(this, "File successfully saved as " + pngFile.getAbsolutePath(), "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
    }
    
}
