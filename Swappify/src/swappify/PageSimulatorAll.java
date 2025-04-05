package swappify;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

public class PageSimulatorAll extends Panels implements ActionListener{
    
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, footer, speedPanel;
    private JPanel fifoPanel, lruPanel, optPanel, scPanel, escPanel, lfuPanel, mfuPanel;
    private JLabel logoLabel, titleLabel, timerLabel, pageRefValues, pageRefValuesTitle, numPageFrameValue;
    public JButton pdfButton, imgButton, restartButton, plusButton, minusButton;
    public JButton backButton;
    private JTextField speedTextField;
    private JTabbedPane tabbedPane;
    private Simulator simulator;
    
    // for simulating
    private Draw draw;
    private ArrayList<Integer> pageNumberLabel = new ArrayList<>();
    private ArrayList<Integer> pageFrames = new ArrayList<>();
    private ArrayList<String> hitMissLabel = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumn = new ArrayList<>();
    private int totalPageFault;
    private Object currentSimulator;
    
    public PageSimulatorAll(Simulator simulator){
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
        
        titleLabel = createLabel(820, 80, white,   "FIFO", 20);
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
        
        // add frame panels //
        
        fifoPanel = addFramePanel(simulator, draw);
        lruPanel = addFramePanel(simulator, draw);
        optPanel = addFramePanel(simulator, draw);
        scPanel = addFramePanel(simulator, draw);
        escPanel = addFramePanel(simulator, draw);
        lfuPanel = addFramePanel(simulator, draw);
        mfuPanel = addFramePanel(simulator, draw);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(1480, 500));
        tabbedPane.setFocusable(false);
        tabbedPane.setBackground(white);
        tabbedPane.setForeground(green);
        tabbedPane.setFont(archivoblack.deriveFont(14f));
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
        tabbedPane.add("FIFO", fifoPanel);
        tabbedPane.add("LRU", lruPanel);
        tabbedPane.add("OPT", optPanel);
        tabbedPane.add("Second Chance", scPanel);
        tabbedPane.add("Enhanced Second Chance", escPanel);
        tabbedPane.add("LFU", lfuPanel);
        tabbedPane.add("MFU", mfuPanel);
        
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            String title = tabbedPane.getTitleAt(selectedIndex);
            titleLabel.setText(title);
        });
        
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
        add(tabbedPane);
        //add(framePanel);
        add(footer);
        
    }
    
    public void startSimulation(String algorithm) {
        // disable buttons
        pdfButton.setEnabled(false);
        imgButton.setEnabled(false);
        restartButton.setEnabled(false);
        plusButton.setEnabled(false);
        minusButton.setEnabled(false);
        
        int simulationSpeed = draw.speed;
        speedTextField.setText(String.valueOf(simulationSpeed));
        
        // for debugging
        System.out.println(simulationSpeed);
        switch(algorithm) {
            case "FIFO":
                currentSimulator = new FIFO(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton);
                ((FIFO) currentSimulator).startSimulation(simulationSpeed);
                break;
            case "LRU":
                currentSimulator = new LRU(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton);
                ((LRU) currentSimulator).startSimulation(simulationSpeed);
                break;
            case "OPT":
                currentSimulator = new Optimal(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton);
                ((Optimal) currentSimulator).startSimulation(simulationSpeed);
                break;
            case "LFU":
                currentSimulator = new LFU(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton);
                ((LFU) currentSimulator).startSimulation(simulationSpeed);
                break;
            case "MFU":
                currentSimulator = new MFU(simulator.getPages(), pageFrames, pageNumberLabel, 
                        hitMissLabel, simulator.getNumberOfFrames(), totalPageFault, draw, 
                        pageFramesPerColumn, timerLabel, pdfButton, imgButton, restartButton, 
                        plusButton, minusButton);
                ((MFU) currentSimulator).startSimulation(simulationSpeed);
                break;
               
        }
    }
    

    public void saveTabbedPaneAsPNG() throws Exception {
        if (tabbedPane == null || tabbedPane.getTabCount() == 0) {
            throw new Exception("TabbedPane is empty or null.");
        }

        // Use a fixed output directory called "screenshots"
        File dir = new File("screenshots");
        if (!dir.exists()) dir.mkdirs();

        SimpleDateFormat formatter = new SimpleDateFormat("MMddyy_HHmmss");

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setSelectedIndex(i); // Activate the tab

            // Refresh GUI
            tabbedPane.revalidate();
            tabbedPane.repaint();
            Thread.sleep(100); // give time to render

            // Capture entire frame
            BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            this.paint(g2);
            g2.dispose();

            // Build filename
            String timestamp = formatter.format(new Date());
            File outputFile = new File(dir, timestamp + ".png");

            // Save image
            ImageIO.write(image, "png", outputFile);

            Thread.sleep(1000); // ensure different timestamps
        }
    }
    
    public void saveTabbedPaneAsPDF(String filePath) throws Exception {
        if (tabbedPane == null || tabbedPane.getTabCount() == 0) {
            throw new Exception("JTabbedPane is empty or null!");
        }

        // Create a PDF document
        Document document = new Document(new com.itextpdf.text.Rectangle(this.getWidth(), this.getHeight()));
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Iterate through all tabs
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
                tabbedPane.setSelectedIndex(i);
                
                tabbedPane.revalidate();
                tabbedPane.repaint();
                Thread.sleep(100);

                // Capture the tab as an image
                BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                this.paint(g2);
                g2.dispose();

                // Save to a temporary file
                File tempFile = File.createTempFile("tab_" + i, ".png");
                ImageIO.write(image, "png", tempFile);

                // Convert to PDF image
                Image pdfImage = Image.getInstance(tempFile.getAbsolutePath());
                pdfImage.setAbsolutePosition(0, 0);
                pdfImage.scaleToFit(this.getWidth(), this.getHeight());

                // Add to PDF
                document.newPage();
                document.add(pdfImage);

                // Delete temp file
                tempFile.delete();
        }

        document.close();
    }

    private JPanel addFramePanel(Simulator simulator, Draw draw){
        JPanel framePanel = new JPanel();
        framePanel.setPreferredSize(new Dimension(1480, 470));
        framePanel.setBackground(green);
        framePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 0), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(10, 10, 10,10))));
        
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        infoPanel.setBackground(green);
        int infoPanelHeight;
        
        JPanel pageRefPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pageRefPanel.setBackground(green);
        JLabel pageRefValuesTitle = new JLabel("Page reference: ");
        pageRefValuesTitle.setFont(archivoblack.deriveFont(16f));
        pageRefValuesTitle.setForeground(white);
        pageRefPanel.add(pageRefValuesTitle);
        
        int count = 0;
        for (int page : simulator.getPages()) {
            String pageText = "" + page;
            if (count < simulator.getPages().size() - 1) {
                pageText += ",";
            }
            
            JLabel pageRefValues = new JLabel(pageText);
            pageRefValues.setFont(archivoblack.deriveFont(13f));
            pageRefValues.setForeground(white);
            pageRefPanel.add(pageRefValues);
            count++;
        }
        
        JPanel numPageFramePanel = new JPanel();
        numPageFramePanel.setBackground(green);
        
        JLabel numPageFrameValue = new JLabel("No. of Page frame: " + simulator.getNumberOfFrames());
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
        Draw draw1 = new Draw(simulator.getPages(), pageFramesPerColumn, hitMissLabel, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFault);
        draw1.setPreferredSize(new Dimension(1462, 434 - infoPanelHeight));
        draw1.setBackground(white);
        
        framePanel.add(infoPanel);
        framePanel.add(draw1);
        
        
        return framePanel;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == imgButton) {
            try {
                saveTabbedPaneAsPNG();
                JOptionPane.showMessageDialog(this, "Files successfully saved to \\Swappify\\screenshots!", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        else if (e.getSource() == pdfButton) {
            SimpleDateFormat sdf = new SimpleDateFormat("MMddyy_HHmmss");
            String timestamp = sdf.format(new Date());

            try {
                saveTabbedPaneAsPDF(timestamp + "_PG.pdf");
                JOptionPane.showMessageDialog(this, "File successfully saved as " + timestamp + "_PG.pdf", "Save Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
    }
    
}