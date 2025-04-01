package swappify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PageSimulator extends Panels implements ActionListener{
    
    
    private JPanel header, leftPanel, centerPanel, rightPanel, timerPanel, framePanel, footer, speedPanel;
    private JLabel logoLabel, titleLabel, timerLabel;
    private JButton pdfButton, imgButton, restartButton, plusButton, minusButton;
    public JButton backButton;
    private JTextField speedTextField;
    
    public PageSimulator(){
    
    }
    
    @Override
    public void showComponents(){
        
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
        
        titleLabel = createLabel(330, 80, white, "" + " ALGORITHM", 20);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
        titleLabel.setHorizontalAlignment(center);
        
        pdfButton = createButton(135, 40, "Save as PDF", darkgreen, white, white,14, null);
        imgButton = createButton(135, 40, "Save as Image", darkgreen, white, white,14, null);
        
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
        
        framePanel = new JPanel();
        framePanel.setPreferredSize(new Dimension(900, 470));
        framePanel.setBackground(green);
        framePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(white, 1), 
                BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(black, 6), 
                        BorderFactory.createEmptyBorder(10, 10, 10,10))));
        
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

    @Override
    public void actionPerformed(ActionEvent e) {
        
    }
    
}
