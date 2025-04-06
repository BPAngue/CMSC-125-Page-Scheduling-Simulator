package swappify;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
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
    public JButton pdfButton, imgButton, restartButton, plusButton, minusButton;
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
        header.setPreferredSize(new Dimension (1480, 180));
        header.setOpaque(false);
        
        logoLabel = new JLabel();
        logoLabel.setIcon(smallLogoIcon);
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        leftPanel.setPreferredSize(new Dimension(300, 200));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,10));
        centerPanel.setPreferredSize(new Dimension(880, 200));
        centerPanel.setOpaque(false);
        
        titleLabel = createLabel(820, 80, white,   simulator.getAlgorithm(), 20);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        titleLabel.setHorizontalAlignment(center);
        
        pdfButton = createButton(135, 40, "Save as PDF", darkgreen, white, white,14, this);
        imgButton = createButton(135, 40, "Save as Image", darkgreen, white, white,14, this);
        
        centerPanel.add(titleLabel);
        centerPanel.add(pdfButton);
        centerPanel.add(imgButton);
        
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,40));
        rightPanel.setPreferredSize(new Dimension(300, 200));
        rightPanel.setOpaque(false);
        
        backButton = createButton(155, 40, "BACK", gray, white,20, null);
        
        timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setPreferredSize(new Dimension(155, 40));
        timerPanel.setBorder(BorderFactory.createLineBorder(white, 1));
        timerPanel.setBackground(darkgreen);
        
        timerLabel = createLabel(110, 25, white, "Timer: 00:00", 16f);
        titleLabel.setHorizontalAlignment(center);
        timerPanel.add(timerLabel);
        
        rightPanel.add(backButton);
        rightPanel.add(timerPanel);
        
        // main simulator panel
        framePanel = new JPanel();
        framePanel.setPreferredSize(new Dimension(1480, 470));
        framePanel.setBackground(green);
        framePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 1), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(10, 10, 10,10))));
        
        infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        infoPanel.setBackground(green);
        int infoPanelHeight;
        
        pageRefPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pageRefPanel.setBackground(green);
        pageRefValuesTitle = new JLabel("Page reference: ");
        pageRefValuesTitle.setFont(archivoblack.deriveFont(16f));
        pageRefValuesTitle.setForeground(white);
        pageRefPanel.add(pageRefValuesTitle);
        
        int count = 0;
        for (int page : simulator.getPages()) {
            String pageText = "" + page;
            if (count < simulator.getPages().size() - 1) {
                pageText += ",";
            }
            
            pageRefValues = new JLabel(pageText);
            pageRefValues.setFont(archivoblack.deriveFont(13f));
            pageRefValues.setForeground(white);
            pageRefPanel.add(pageRefValues);
            count++;
        }
        
        numPageFramePanel = new JPanel();
        numPageFramePanel.setBackground(green);
        
        numPageFrameValue = new JLabel("No. of Page frame: " + simulator.getNumberOfFrames());
        numPageFrameValue.setFont(archivoblack.deriveFont(16f));
        numPageFrameValue.setForeground(white);
        numPageFramePanel.add(numPageFrameValue);
        
        infoPanel.add(pageRefPanel);
        infoPanel.add(numPageFramePanel);
        
        infoPanelHeight = pageRefPanel.getPreferredSize().height + numPageFramePanel.getPreferredSize().height;
        infoPanel.setPreferredSize(new Dimension(1460, infoPanelHeight));
        pageRefPanel.setPreferredSize(new Dimension(730, infoPanelHeight));
        numPageFramePanel.setPreferredSize(new Dimension(730, infoPanelHeight));
        
        // change this with the drawing panel
        draw = new Draw(simulator.getPages(), pageFramesPerColumn, hitMissLabel, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFault);
        draw.setPreferredSize(new Dimension(1462, 434 - infoPanelHeight));
        draw.setBackground(white);
        
        framePanel.add(infoPanel);
        framePanel.add(draw);
        
        header.add(leftPanel);
        header.add(centerPanel);
        header.add(rightPanel);
        
        footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 480,5));
        footer.setPreferredSize(new Dimension(1460, 60));
        footer.setOpaque(false);
        
        speedPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        speedPanel.setPreferredSize(new Dimension(320, 60));
        speedPanel.setOpaque(false);
        
        minusButton = createButton(60,40, "-", red, white,white, 20,1, this);
        plusButton = createButton(60,40, "+", green, white, white, 20, 1, this);
        speedTextField = createField(200, 40, darkgreen, 16);
        speedTextField.setEditable(false);
        speedTextField.setText("1000");
        speedTextField.setHorizontalAlignment(center);
        
        speedPanel.add(minusButton);
        speedPanel.add(speedTextField);
        speedPanel.add(plusButton);
        
        restartButton = createButton(150,40, "Restart", red, white,white, 16,1, this);
        
        footer.add(speedPanel);
        footer.add(restartButton);
        
        add(header);
        add(framePanel);
        add(footer);
    }
    
    public void startSimulation(String algorithm) {
        // disable buttons
        pdfButton.setEnabled(false);
        imgButton.setEnabled(false);
        restartButton.setText("Stop");
        plusButton.setEnabled(false);
        minusButton.setEnabled(false);
        
        int simulationSpeed = Integer.parseInt(speedTextField.getText());
        speedTextField.setText(String.valueOf(simulationSpeed));
        simulator.speed = simulationSpeed;
        
        switch(algorithm) {
            case "FIFO":
                currentSimulator = new FIFO(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton, simulationSpeed);
                ((FIFO) currentSimulator).startSimulation();
                break;
            case "LRU":
                currentSimulator = new LRU(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton, simulationSpeed);
                ((LRU) currentSimulator).startSimulation();
                break;
            case "OPT":
                currentSimulator = new Optimal(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton, simulationSpeed);
                ((Optimal) currentSimulator).startSimulation();
                break;
            case "Second Chance":
                currentSimulator = new SecondChance(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton, simulationSpeed);
                ((SecondChance) currentSimulator).startSimulation();
                break;
            case "LFU":
                currentSimulator = new LFU(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton, simulationSpeed);
                ((LFU) currentSimulator).startSimulation();
                break;
            case "MFU":
                currentSimulator = new MFU(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton, simulationSpeed);
                ((MFU) currentSimulator).startSimulation();
                break;
        }
    }
    
    private File makeScreenshotsDirectory() {
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        return dir;
    }

    private File saveAsPNG(String fileName) throws IOException {
        makeScreenshotsDirectory();
        
        // Capture panel as image
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.printAll(g2d);
        g2d.dispose();
    
        // Save the image as a PNG file
        File pngFile = new File("screenshots/" + fileName + ".png");
        ImageIO.write(image, "PNG", pngFile);
        
        return pngFile;
    }
    
    public void savePanelAsPDF(String fileName) throws Exception {
        makeScreenshotsDirectory();
        
        
        // Create a BufferedImage and paint the panel onto it
        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.printAll(g2d);
        g2d.dispose();

        File pdfFile = new File("screenshots/" + fileName + ".pdf");
        
        // Create PDF document
        Document document = new Document(new com.itextpdf.text.Rectangle(this.getWidth(), this.getHeight()));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(image, null);
        pdfImage.setAbsolutePosition(0, 0);
        contentByte.addImage(pdfImage);

        document.close();
    }
    
    public void stopCurrentSimulation() {
        if(currentSimulator instanceof FIFO fifo) {
            fifo.stopSimulation();
        } else if (currentSimulator instanceof LRU lru) {
            lru.stopSimulation();
        } else if (currentSimulator instanceof Optimal opt) {
            opt.stopSimulation();
        } else if (currentSimulator instanceof LFU lfu) {
            lfu.stopSimulation();
        } else if (currentSimulator instanceof MFU mfu) {
            mfu.stopSimulation();
        } else if (currentSimulator instanceof SecondChance sc) {
            sc.stopSimulation();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == imgButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());

            String fileName = timestamp + "_PG";
            try {
                File pngFile = saveAsPNG(fileName);
                JOptionPane.showMessageDialog(this, "PNG successfully saved to \\Swappify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException f) {
                f.printStackTrace();
            }
        }
        
        else if (e.getSource() == pdfButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());
            String fileName = timestamp + "_PG";

            try {
                savePanelAsPDF(fileName);
                JOptionPane.showMessageDialog(this, "PDF successfully saved to \\Swappify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
        
        else if (e.getSource() == restartButton) {
            if (restartButton.getText().equals("Stop")) {
                System.out.println("Stop Simulation!"); // for debugging
                stopCurrentSimulation();
                restartButton.setText("Restart");
                
                // enable buttons
                pdfButton.setEnabled(true);
                imgButton.setEnabled(true);
                restartButton.setText("Restart");
                plusButton.setEnabled(true);
                minusButton.setEnabled(true);
            } else if (restartButton.getText().equals("Restart")) {
                startSimulation(simulator.getAlgorithm());
                if (currentSimulator instanceof FIFO fifo) {
                    fifo.restartSimulation();
                } else if (currentSimulator instanceof LRU lru) {
                    lru.restartSimulation();
                } else if (currentSimulator instanceof Optimal opt) {
                    opt.restartSimulation();
                } else if (currentSimulator instanceof LFU lfu) {
                    lfu.restartSimulation();
                } else if (currentSimulator instanceof MFU mfu) {
                    mfu.restartSimulation();
                } else if (currentSimulator instanceof SecondChance sc) {
                    sc.restartSimulation();
                }
                // for debugging
                System.out.println(simulator.getPages());
                System.out.println("Restart Simulation!"); // for debugging
                System.out.println(simulator.getAlgorithm()); // for debugging
            }
        }
        
        else if (e.getSource() == plusButton) {
            int speed = Integer.parseInt(speedTextField.getText());
            if (speed < 2000) {
                if (speed == 100) {
                    speed += 150;
                } else if (speed + 250 > 2000) {
                    speed = 2000;
                } else {
                    speed += 250;
                }
                speedTextField.setText(String.valueOf(speed));
            }
        }
        
        else if (e.getSource() == minusButton) {
            int speed = Integer.parseInt(speedTextField.getText());
            if (speed > 100) {
                if (speed == 250) {
                    speed -= 150;
                } else{
                    speed -= 250;
                }
                
                if (speed < 100) {
                    speed = 100;
                }
                speedTextField.setText(String.valueOf(speed));
            }
        }
    }
}