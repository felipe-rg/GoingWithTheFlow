package Panels;

import javax.swing.*;
import java.awt.*;

public class Title extends JPanel{
    //Declaring components
    JLabel titleLabel;

    public Title(){
        //Editing Panel itself
        this.setPreferredSize(new Dimension(1200,100));
        this.setBackground(Color.white);
        outline(this);

        //Adding label
        titleLabel = new JLabel("AMU Ward");
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        add(titleLabel);


    }

    public void outline(JPanel panel){
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
