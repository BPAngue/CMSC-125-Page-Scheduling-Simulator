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
    
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, footer, speedPanel, infoPanel, simulatorPanel, pageRefPanel, numPageFramePanel;
    public JPanel framePanel;
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
        header.setPreferredSize(new Dimension (1480, 200));
        header.setOpaque(false);
        
        logoLabel = new JLabel();
        logoLabel.setIcon(smallLogoIcon);
        
        leftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,10));
        leftPanel.setPreferredSize(new Dimension(493, 200));
        leftPanel.setOpaque(false);
        leftPanel.add(logoLabel);
        
        centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20,10));
        centerPanel.setPreferredSize(new Dimension(493, 200));
        centerPanel.setOpaque(false);
        
        titleLabel = createLabel(330, 80, white, "" + " " + simulator.getAlgorithm(), 20);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        titleLabel.setHorizontalAlignment(center);
        
        pdfButton = createButton(135, 40, "Save as PDF", darkgreen, white, white,14, this);
        imgButton = createButton(135, 40, "Save as Image", darkgreen, white, white,14, this);
        
        centerPanel.add(titleLabel);
        centerPanel.add(pdfButton);
        centerPanel.add(imgButton);
        
        rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0,50));
        rightPanel.setPreferredSize(new Dimension(493, 200));
        rightPanel.setOpaque(false);
        
        backButton = createButton(200, 40, "BACK", gray, white,20, null);
        
        timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        timerPanel.setPreferredSize(new Dimension(300, 40));
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
        //System.out.println("Saved as PNG: " + pngFile.getAbsolutePath());
        
        return pngFile;
    }
    
    public void savePanelAsPDF(String filePath) throws Exception {
        // Create a BufferedImage and paint the panel onto it

        BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        this.printAll(g2d);
        g2d.dispose();

        // Create PDF document
        Document document = new Document(new com.itextpdf.text.Rectangle(this.getWidth(), this.getHeight()));
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        PdfContentByte contentByte = writer.getDirectContent();
        com.itextpdf.text.Image pdfImage = com.itextpdf.text.Image.getInstance(image, null);
        pdfImage.setAbsolutePosition(0, 0);
        contentByte.addImage(pdfImage);

        document.close();
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
        
        else if (e.getSource() == pdfButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());

            try {
                savePanelAsPDF(timestamp + "_PG.pdf");
                JOptionPane.showMessageDialog(this, "File successfully saved as " + timestamp + "_PG.pdf", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
    }
    
}
