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
import java.util.HashMap;
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
    private Draw fifoDraw;
    private Draw lruDraw;
    private Draw optDraw;
    private Draw scDraw;
    private Draw escDraw;
    private Draw lfuDraw;
    private Draw mfuDraw;
    private ArrayList<Integer> pageNumberLabelFifo = new ArrayList<>();
    private ArrayList<Integer> pageNumberLabelLru = new ArrayList<>();
    private ArrayList<Integer> pageNumberLabelOpt = new ArrayList<>();
    private ArrayList<Integer> pageNumberLabelSc = new ArrayList<>();
    private ArrayList<Integer> pageNumberLabelEsc = new ArrayList<>();
    private ArrayList<Integer> pageNumberLabelLfu = new ArrayList<>();
    private ArrayList<Integer> pageNumberLabelMfu = new ArrayList<>();
    private ArrayList<Integer> pageFramesFifo = new ArrayList<>();
    private ArrayList<Integer> pageFramesLru = new ArrayList<>();
    private ArrayList<Integer> pageFramesOpt = new ArrayList<>();
    private ArrayList<Integer> pageFramesSc = new ArrayList<>();
    private ArrayList<Integer> pageFramesEsc = new ArrayList<>();
    private ArrayList<Integer> pageFramesLfu = new ArrayList<>();
    private ArrayList<Integer> pageFramesMfu = new ArrayList<>();
    private ArrayList<String> hitMissLabelFifo = new ArrayList<>();
    private ArrayList<String> hitMissLabelLru = new ArrayList<>();
    private ArrayList<String> hitMissLabelOpt = new ArrayList<>();
    private ArrayList<String> hitMissLabelSc = new ArrayList<>();
    private ArrayList<String> hitMissLabelEsc = new ArrayList<>();
    private ArrayList<String> hitMissLabelLfu = new ArrayList<>();
    private ArrayList<String> hitMissLabelMfu = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnFifo = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnLru = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnOpt = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnSc = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnEsc = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnLfu = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> pageFramesPerColumnMfu = new ArrayList<>();
    private int totalPageFaultFifo;
    private int totalPageFaultLru;
    private int totalPageFaultOpt;
    private int totalPageFaultSc;
    private int totalPageFaultEsc;
    private int totalPageFaultLfu;
    private int totalPageFaultMfu;
    
    // classes
    private FIFO fifo;
    private LRU lru;
    private Optimal opt;
    private SecondChance sc;
    private EnhancedSecondChance esc;
    private LFU lfu;
    private MFU mfu;
    
    // threads
    private Thread fifoThread;
    private Thread lruThread;
    private Thread optThread;
    private Thread scThread;
    private Thread escThread;
    private Thread lfuThread;
    private Thread mfuThread;
    
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
        
        // draw instances
        fifoDraw = new Draw(simulator.getPages(), pageFramesPerColumnFifo, hitMissLabelFifo, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultFifo, "FIFO");
        lruDraw = new Draw(simulator.getPages(), pageFramesPerColumnLru, hitMissLabelLru, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultLru, "LRU");
        optDraw = new Draw(simulator.getPages(), pageFramesPerColumnOpt, hitMissLabelOpt, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultOpt, "OPT");
        scDraw = new Draw(simulator.getPages(), pageFramesPerColumnSc, hitMissLabelSc, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultSc, "Second Chance");
        escDraw = new Draw(simulator.getPages(), pageFramesPerColumnEsc, hitMissLabelEsc, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultEsc, "Enhanced Second Chance");
        lfuDraw = new Draw(simulator.getPages(), pageFramesPerColumnLfu, hitMissLabelLfu, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultLfu, "LFU");
        mfuDraw = new Draw(simulator.getPages(), pageFramesPerColumnMfu, hitMissLabelMfu, simulator.getReferenceLength(), simulator.getNumberOfFrames(), totalPageFaultMfu, "MFU");
        
        // add frame panels //
        fifoPanel = addFramePanel(simulator, fifoDraw);
        lruPanel = addFramePanel(simulator, lruDraw);
        optPanel = addFramePanel(simulator, optDraw);
        scPanel = addFramePanel(simulator, scDraw);
        escPanel = addFramePanel(simulator, escDraw);
        lfuPanel = addFramePanel(simulator, lfuDraw);
        mfuPanel = addFramePanel(simulator, mfuDraw);
        
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
        speedTextField.setText("1000");
        speedTextField.setHorizontalAlignment(center);
        
        speedPanel.add(minusButton);
        speedPanel.add(speedTextField);
        speedPanel.add(plusButton);
        
        restartButton = createButton(150,40, "Restart", red, white,white, 16,1, this);
        
        footer.add(speedPanel);
        footer.add(restartButton);
        
        add(header);
        add(tabbedPane);
        add(footer);
        
        startSimulation();
    }
    
    public void startSimulation() {
        // disable buttons
        pdfButton.setEnabled(false);
        imgButton.setEnabled(false);
        restartButton.setText("Stop");
        plusButton.setEnabled(false);
        minusButton.setEnabled(false);
        
        int simulationSpeed = Integer.parseInt(speedTextField.getText());
        speedTextField.setText(String.valueOf(simulationSpeed));
        simulator.speed = simulationSpeed;
        
        // i want to simultaneously start all of the simulation classes here
        // call all of the simulator classes
        fifo = new FIFO(simulator.getPages(), pageFramesFifo, pageNumberLabelFifo, 
                hitMissLabelFifo, simulator.getNumberOfFrames(), totalPageFaultFifo, fifoDraw, 
                pageFramesPerColumnFifo, timerLabel, pdfButton, imgButton, restartButton, 
                plusButton, minusButton, simulationSpeed);
        fifoThread = new Thread(fifo);
        
        lru = new LRU(simulator.getPages(), pageFramesLru, pageNumberLabelLru, 
                hitMissLabelLru, simulator.getNumberOfFrames(), totalPageFaultLru, lruDraw, 
                pageFramesPerColumnLru, timerLabel, pdfButton, imgButton, restartButton, 
                plusButton, minusButton, simulationSpeed);
        lruThread = new Thread(lru);
        
        opt = new Optimal(simulator.getPages(), pageFramesOpt, pageNumberLabelOpt, 
                hitMissLabelOpt, simulator.getNumberOfFrames(), totalPageFaultOpt, optDraw, 
                pageFramesPerColumnOpt, timerLabel, pdfButton, imgButton, restartButton, 
                plusButton, minusButton, simulationSpeed);
        optThread = new Thread(opt);
        
        sc = new SecondChance(simulator.getPages(), pageFramesSc, pageNumberLabelSc, 
                hitMissLabelSc, simulator.getNumberOfFrames(), totalPageFaultSc, scDraw, 
                pageFramesPerColumnSc, timerLabel, pdfButton, imgButton, restartButton, 
                plusButton, minusButton, simulationSpeed);
        scThread = new Thread(sc);
        
        esc = new EnhancedSecondChance(simulator.getPages(), pageFramesEsc, pageNumberLabelEsc,
                hitMissLabelEsc, simulator.getNumberOfFrames(), totalPageFaultEsc, escDraw, 
                pageFramesPerColumnEsc, timerLabel, pdfButton, imgButton, restartButton,
                plusButton, minusButton, simulationSpeed);
        escThread = new Thread(esc);
        
        lfu = new LFU(simulator.getPages(), pageFramesLfu, pageNumberLabelLfu, 
                hitMissLabelLfu, simulator.getNumberOfFrames(), totalPageFaultLfu, lfuDraw, 
                pageFramesPerColumnLfu, timerLabel, pdfButton, imgButton, restartButton, 
                plusButton, minusButton, simulationSpeed);
        lfuThread = new Thread(lfu);
        
        mfu = new MFU(simulator.getPages(), pageFramesMfu, pageNumberLabelMfu, 
                hitMissLabelMfu, simulator.getNumberOfFrames(), totalPageFaultMfu, mfuDraw, 
                pageFramesPerColumnMfu, timerLabel, pdfButton, imgButton, restartButton, 
                plusButton, minusButton, simulationSpeed);
        mfuThread = new Thread(mfu);
        
        // start threads
        fifoThread.start();
        lruThread.start();
        optThread.start();
        scThread.start();
        escThread.start();
        lfuThread.start();
        mfuThread.start();
    }
    
    public void saveTabbedPaneAsPNG() throws Exception {
        if (tabbedPane == null || tabbedPane.getTabCount() == 0) {
            throw new Exception("TabbedPane is empty or null.");
        }

        // Calculate total height required for the combined image
        int totalHeight = 0;
        int width = this.getWidth();

        // Get the height of each tab by capturing its image
        ArrayList<BufferedImage> tabImages = new ArrayList<>();
        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setSelectedIndex(i); // Activate the tab
            tabbedPane.revalidate();
            tabbedPane.repaint();

            BufferedImage tabImage = new BufferedImage(width, this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = tabImage.createGraphics();
            this.paint(g2);
            g2.dispose();

            tabImages.add(tabImage);
            totalHeight += tabImage.getHeight(); // Add height of this tab to total height
        }

        // Create a new image with the combined height
        BufferedImage combinedImage = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = combinedImage.createGraphics();

        // Draw each tab's image onto the combined image
        int yOffset = 0;
        for (BufferedImage tabImage : tabImages) {
            g2.drawImage(tabImage, 0, yOffset, null);
            yOffset += tabImage.getHeight(); // Move the yOffset down by the height of the current tab
        }
        g2.dispose();

        // Save the combined image as a PNG
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MMddyy_HHmmss");
        String timestamp = formatter.format(new Date());
        File outputFile = new File(dir, timestamp + "_PG.png");

        ImageIO.write(combinedImage, "png", outputFile);
    }

    public void saveTabbedPaneAsPDF() throws Exception {
        if (tabbedPane == null || tabbedPane.getTabCount() == 0) {
            throw new Exception("JTabbedPane is empty or null!");
        }

        // Create screenshots directory if it doesn't exist
        File dir = new File("screenshots");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Generate filename with timestamp
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyy_HHmmss");
        String timestamp = formatter.format(new Date());
        String filename = timestamp + "_PG.pdf";
        File outputFile = new File(dir, filename);

        // Create PDF document
        Document document = new Document(new com.itextpdf.text.Rectangle(this.getWidth(), this.getHeight()));
        PdfWriter.getInstance(document, new FileOutputStream(outputFile));
        document.open();

        for (int i = 0; i < tabbedPane.getTabCount(); i++) {
            tabbedPane.setSelectedIndex(i);

            BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            this.paint(g2);
            g2.dispose();

            Image pdfImage = Image.getInstance(image, null);
            pdfImage.setAbsolutePosition(0, 0);
            pdfImage.scaleToFit(this.getWidth(), this.getHeight());

            document.newPage();
            document.add(pdfImage);
        }

        document.close();
    }

    private JPanel addFramePanel(Simulator simulator, Draw draw1){
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
        JLabel titlePagRef = new JLabel("Page reference: ");
        titlePagRef.setFont(archivoblack.deriveFont(16f));
        titlePagRef.setForeground(white);
        pageRefPanel.add(titlePagRef);
        
        int count = 0;
        for (int page : simulator.getPages()) {
            String pageText = "" + page;
            if (count < simulator.getPages().size() - 1) {
                pageText += ",";
            }
            
            JLabel valuesPageRef = new JLabel(pageText);
            valuesPageRef.setFont(archivoblack.deriveFont(13f));
            valuesPageRef.setForeground(white);
            pageRefPanel.add(valuesPageRef);
            count++;
        }
        
        JPanel numPageFramePanel = new JPanel();
        numPageFramePanel.setBackground(green);
        
        JLabel valueNumPageFrame = new JLabel("No. of Page frame: " + simulator.getNumberOfFrames());
        valueNumPageFrame.setFont(archivoblack.deriveFont(16f));
        valueNumPageFrame.setForeground(white);
        numPageFramePanel.add(valueNumPageFrame);
        
        infoPanel.add(pageRefPanel);
        infoPanel.add(numPageFramePanel);
        
        infoPanelHeight = pageRefPanel.getPreferredSize().height + numPageFramePanel.getPreferredSize().height;
        infoPanel.setPreferredSize(new Dimension(1460, infoPanelHeight));
        pageRefPanel.setPreferredSize(new Dimension(730, infoPanelHeight));
        numPageFramePanel.setPreferredSize(new Dimension(730, infoPanelHeight));
        
        // change this with the drawing panel
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
                saveTabbedPaneAsPDF();
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
                startSimulation();
                fifo.restartSimulation();
                lru.restartSimulation();
                opt.restartSimulation();
                sc.restartSimulation();
                esc.restartSimulation();
                lfu.restartSimulation();
                mfu.restartSimulation();
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
    
    public void stopCurrentSimulation() {
        fifo.stopSimulation();
        lru.stopSimulation();
        opt.stopSimulation();
        sc.stopSimulation();
        esc.stopSimulation();
        lfu.stopSimulation();
        mfu.stopSimulation();
    }
}