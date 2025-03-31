package swappify;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Panels extends JPanel{
    public Font font;
    public final Font archivoblack = getFont("archivoblack"); 
    public final Font archivonarrow = getFont("archivonarrow"); 
    
    
    public BufferedImage img;
    public final BufferedImage logo = getImg("/img/logo.png");
    public final BufferedImage smallLogo = getImg("/img/logo_small.png");
    
    public final Icon logoIcon = new ImageIcon(logo);
    public final Icon smallLogoIcon = new ImageIcon(smallLogo);
    
    public final Color gray = new Color(118, 130, 138);
    public final Color white = Color.WHITE;
    public final Color black = Color.BLACK;
    public final Color green = new Color(24,121,76);
    public final Color darkgreen = new Color(35, 102, 46);
    public final Color red = new Color(188,24,35);   
    
    public Panels(){
        showComponents();
    }
    
    public void showComponents(){
        
    }
    
    public JButton createButton(int width, int height, String text, Color background, Color foreground, int size) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(archivoblack.deriveFont(Font.BOLD, size));
        button.setFocusable(false);
        button.setBorderPainted(false);
        return button;
    }
    
    public JButton createButton(int width, int height, String text, Color background, Color foreground, Color border, float size) {
        JButton button = new JButton(text);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setPreferredSize(new Dimension(width, height));
        button.setFont(archivoblack.deriveFont(size));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(foreground, 2));
        return button;
    }
    
    public Font getFont(String filepath){
        try {
            InputStream i = getClass().getResourceAsStream("/font/" + filepath + ".ttf");
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, i);
            } catch (FontFormatException ex) {
                System.out.println("no font");
            }
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            
        } catch (IOException ex) {
           ex.printStackTrace();
        }
        return font;
    }
    
    
    public BufferedImage getImg(String filepath){
        try{
            img = ImageIO.read(getClass().getResourceAsStream(filepath));
        }catch(Exception e){
            //e.printStackTrace();
        }
        return img;
    }
    
    public JLabel createLabel(int width, int height, Color foreground, String text, float size){
        JLabel label = new JLabel();
        label.setText(text);
        label.setForeground(foreground);
        label.setFont(archivoblack.deriveFont(size));
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }
    
    public JLabel createLabel(int width, int height, Color foreground, String text, Font font, float size){
        JLabel label = new JLabel();
        label.setText(text);
        label.setForeground(foreground);
        label.setFont(font.deriveFont(size));
        label.setPreferredSize(new Dimension(width, height));
        return label;
    }
    
    public JTextField createField(int width, int height, Color foreground, float size){
        JTextField textfield = new JTextField();
        textfield.setPreferredSize(new Dimension(width, height));
        textfield.setForeground(foreground);
        textfield.setFont(archivoblack.deriveFont(size));
        textfield.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(),
                BorderFactory.createEmptyBorder(0,10,0,0)));
        textfield.setBackground(Color.WHITE);
        textfield.setEditable(true);
        
        return textfield;
    }
            
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        img = getImg("/img/bg.jpg");
        g.drawImage(img, 0, 0, 1000, 800, null); 
    }
}
