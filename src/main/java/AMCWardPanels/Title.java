package AMCWardPanels;

import javax.swing.*;
import java.awt.*;

public class Title extends JPanel{

    //Declaring the two components
    JLabel titleLabel;

    public Title(String title, JButton backButton){
        //Editing Panel itself
        this.setPreferredSize(new Dimension(1200,100));
        this.setBackground(Color.white);
        //Setting layout
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        //Title Label
        titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        c.insets = new Insets(0,570,0,0);
        this.add(titleLabel, c);

        //Button
        c.insets = new Insets(0,430,0,0);
        this.add(backButton, c);

    }
}
