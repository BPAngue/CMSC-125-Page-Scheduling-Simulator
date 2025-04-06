package swappify;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JPanel;

public class Draw extends JPanel {
    private ArrayList<Integer> pageNumberLabel;
    private ArrayList<ArrayList<Integer>> pageFramesPerColumn;
    private ArrayList<String> hitMissLabel;
    public int referenceStringLength = 0;
    public int numberOfPageFrames = 0;
    public int totalPageFault = 0;
    private int currentColumn = -1;
    private int horizontalSpacing = 20;
    
    Panels panels = new Panels();
    
    public Draw(ArrayList<Integer> pageNumberLabel, ArrayList<ArrayList<Integer>> pageFramesPerColumn, ArrayList<String> hitMissLabel, int referenceStringLength, int numberOfPageFrames, int totalPageFault) {
        this.pageNumberLabel = pageNumberLabel;
        this.pageFramesPerColumn = pageFramesPerColumn;
        this.hitMissLabel = hitMissLabel;
        this.referenceStringLength = referenceStringLength;
        this.numberOfPageFrames = numberOfPageFrames;
        this.totalPageFault = totalPageFault;
    }

    public void nextStep() {
        if (currentColumn < referenceStringLength - 1) {
            currentColumn++;
            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (referenceStringLength <= 0 || numberOfPageFrames <= 0) {
            return;
        }

        int panelWidth = getWidth();
        int panelHeight = getHeight();

        int topLabelsHeight = 30;
        int bottomLabelsHeight = 80;

        int horizontalMargin = 20;
        int verticalMargin = 10 + topLabelsHeight;
        
        if (referenceStringLength < 15){
            horizontalSpacing = 55;
        }
        else if (referenceStringLength < 19){
            horizontalSpacing = 30;
        }
        else if (referenceStringLength > 29){
            horizontalSpacing = 5;
        }
        

        int availableWidth = panelWidth - (5 * horizontalMargin);
        int boxWidth = Math.min(50, (availableWidth - ((referenceStringLength - 1) * horizontalSpacing)) / referenceStringLength);
        int availableHeight = panelHeight - (2 * verticalMargin) - bottomLabelsHeight;
        int boxHeight = availableHeight / numberOfPageFrames;

        boxWidth = Math.max(30, boxWidth);
        boxHeight = Math.max(25, boxHeight);

        int x = horizontalMargin;

        for (int col = 0; col <= currentColumn && col < referenceStringLength; col++) {
            if (col == currentColumn) {
                g.setColor(new Color(144, 238, 144));
                g.fillRect(x, verticalMargin, boxWidth, boxHeight * numberOfPageFrames);
            }

            if (pageNumberLabel != null && col < pageNumberLabel.size()) {
                g.setColor(panels.green);
                g.setFont(panels.archivoblack.deriveFont(13f));
                g.drawString(String.valueOf(pageNumberLabel.get(col)), x + (boxWidth / 2) - 5, verticalMargin - 15);
            }

            int y = verticalMargin;

            ArrayList<Integer> currentFrame = pageFramesPerColumn.get(col);
            for (int row = 0; row < numberOfPageFrames; row++) {
                g.setColor(panels.green);
                g.drawRect(x, y, boxWidth, boxHeight);

                if (row < currentFrame.size()) {
                    Integer val = currentFrame.get(numberOfPageFrames - 1 - row);
                    if (val != null) {
                        if (hitMissLabel != null && col < hitMissLabel.size()) {
                            String hitMiss = hitMissLabel.get(col);
                            if (hitMiss.equalsIgnoreCase("MISS")) {
                                if (pageNumberLabel != null && col < pageNumberLabel.size()) {
                                    if (Objects.equals(val, pageNumberLabel.get(col))) {
                                        g.setColor(panels.red);
                                        System.out.println("number is colored red");
                                    }
                                }
                            }
                            
                        }
                        g.setFont(panels.archivoblack.deriveFont(13f));
                        g.drawString(String.valueOf(val), x + boxWidth / 2 - 5, y + boxHeight / 2 + 5);
                    }
                }

                y += boxHeight;
            }

            if (hitMissLabel != null && col < hitMissLabel.size()) {
                g.setColor(hitMissLabel.get(col).equalsIgnoreCase("HIT") ? panels.green : panels.red);
                g.setFont(panels.archivoblack.deriveFont(12f));
                g.drawString(hitMissLabel.get(col), x + (boxWidth / 2) - 15,
                        verticalMargin + (boxHeight * numberOfPageFrames) + 20);
            }

            x += boxWidth + horizontalSpacing;
        }

        g.setColor(panels.green);
        g.setFont(panels.archivoblack.deriveFont(16f));
        g.drawString("Total Page Faults: " + totalPageFault, horizontalMargin, panelHeight - 20);
    }
    
    public void clearValues() {
        pageFramesPerColumn.clear();
        hitMissLabel.clear();
        totalPageFault = 0;
        currentColumn = -1;
    }
}
