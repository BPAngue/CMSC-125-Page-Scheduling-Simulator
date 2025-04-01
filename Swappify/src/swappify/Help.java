package swappify;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Help extends Panels{
    
    private JPanel header, footer;
    private JLabel titleLabel, helpLabel, logoLabel;
    public JButton backButton;
    
    public Help(){
    
    }
    
    @Override
    public void showComponents(){
        header = new JPanel(new FlowLayout(FlowLayout.RIGHT, 70,5));
        header.setPreferredSize(new Dimension(1000, 200));
        header.setOpaque(false);
        
        titleLabel = createLabel(400, 200, white, "Help", 60);
        logoLabel = new JLabel();
        logoLabel.setIcon(smallLogoIcon);
        
        header.add(titleLabel);
        header.add(logoLabel);
        
        String helpString = "<html>Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc,</html>";
                
        helpLabel = createLabel(680, 420, white, helpString, archivonarrow, 19f);
        
        footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 70,30));
        footer.setPreferredSize(new Dimension(1000, 100));
        footer.setOpaque(false);
        
        backButton = createButton(150, 40, "BACK", gray, white, 20, null);
        footer.add(backButton);
        
        add(header);
        add(helpLabel);
        add(footer);
    }
}
