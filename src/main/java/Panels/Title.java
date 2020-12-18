package Panels;

import javax.swing.*;
import java.awt.*;

public class Title extends JPanel{

    public Title(String title){
        //Editing Panel itself
        this.setPreferredSize(new Dimension(1200,100));
        this.setBackground(Color.white);

        //Adding label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        add(titleLabel);
    }
}
