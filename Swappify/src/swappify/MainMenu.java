package swappify;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainMenu extends JPanel implements ActionListener{
    public CardLayout cardLayout;
    public JPanel cardPanel, outerPanel, titlePanel, buttonPanel;
    public JButton startButton, helpButton, aboutButton, exitButton;
    public JLabel titleLabel;
    public BufferedImage img, logo, bg;
    public Icon logoIcon;
    public Font archivoblack;
    
    Simulator simulator = new Simulator();
    
    StartPage startPanel = new StartPage();
    Help helpPanel = new Help();
    About aboutPanel = new About();
    PageSimulator simulatorPanel = new PageSimulator();
    
    public MainMenu (CardLayout cardLayout, JPanel cardPanel){
        this.cardLayout = cardLayout;
        this.cardPanel = cardPanel;
        
        showComponents();
    }
    
    public void showComponents(){
        importFont();
        outerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        outerPanel.setOpaque(false);
        outerPanel.setPreferredSize(new Dimension(1000, 800));
        
        titlePanel = new JPanel(new GridBagLayout());
        logo = getImg("/img/logo.png");
        logoIcon = new ImageIcon(logo);
        
        titleLabel = new JLabel();
        titleLabel.setIcon(logoIcon);
        
        GridBagConstraints tgbc = new GridBagConstraints();
        tgbc.gridx = 0;
        tgbc.gridy = 0;
        tgbc.insets = new Insets(80, 0, 0, 0);

        titlePanel.add(titleLabel, tgbc);
        titlePanel.setOpaque(false);
        titlePanel.setPreferredSize(new Dimension (1000, 250));
       
        buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(new Dimension(1000, 500));
        
        startButton = createButton("START");
        helpButton = createButton("HELP");
        aboutButton = createButton("ABOUT");
        exitButton = createButton("EXIT");
        
        startButton.addActionListener(this);
        helpButton.addActionListener(this);
        aboutButton.addActionListener(this);
        exitButton.addActionListener(this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 25, 0);
        buttonPanel.add(startButton, gbc);
        
        gbc.gridy++;
        buttonPanel.add(helpButton, gbc);
        
        gbc.gridy++;
        buttonPanel.add(aboutButton, gbc);
        
        gbc.gridy++;
        buttonPanel.add(exitButton, gbc);
        
        outerPanel.add(titlePanel);
        outerPanel.add(buttonPanel);
        
        add(outerPanel);
        cardPanel.add(this, "MAIN_MENU");
        cardPanel.add(startPanel, "START");
        cardPanel.add(helpPanel, "HELP");
        cardPanel.add(aboutPanel, "ABOUT");
        cardPanel.add(simulatorPanel, "SIMULATOR");
        
        startPanel.backButton.addActionListener(this);
        startPanel.generateButton.addActionListener(this);
        helpPanel.backButton.addActionListener(this);
        aboutPanel.backButton.addActionListener(this);
        simulatorPanel.backButton.addActionListener(this);
    }
    
    public JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(118,130,138));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(320, 60));
        button.setFont(archivoblack.deriveFont(Font.BOLD, 30));
        button.setFocusable(false);
        button.setBorderPainted(false);
        return button;
    }
    
    public BufferedImage getImg(String filePath){
        try{
            img = ImageIO.read(getClass().getResourceAsStream(filePath));
        }catch(Exception e){
            e.printStackTrace();
        }
        return img;
    }
    
    public void importFont(){
        try {
            InputStream i = getClass().getResourceAsStream("/font/archivoblack.ttf");
            try {
                archivoblack = Font.createFont(Font.TRUETYPE_FONT, i);
            } catch (FontFormatException ex) {
                System.out.println("no font");
            }
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(archivoblack);
            
        } catch (IOException ex) {
           ex.printStackTrace();
        }
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        bg = getImg("/img/bg.jpg");
        g.drawImage(bg, 0, 0, 1000, 800, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource()==exitButton){
            System.exit(0);
        }
        else if (e.getSource()==startButton || e.getSource()==simulatorPanel.backButton){
            cardLayout.show(cardPanel, "START");
        }
        else if (e.getSource()==startPanel.backButton || e.getSource()==helpPanel.backButton
                || e.getSource()==aboutPanel.backButton){
            cardLayout.show(cardPanel, "MAIN_MENU");
        }
        else if (e.getSource()==helpButton){
            cardLayout.show(cardPanel, "HELP");
        }
        else if (e.getSource()==aboutButton){
            cardLayout.show(cardPanel, "ABOUT");
        }
        else if (e.getSource()==startPanel.generateButton && startPanel.validateInput()){
            cardLayout.show(cardPanel, "SIMULATOR");
        }
    }
}
