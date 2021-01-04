package Panels;

import javax.swing.*;
import java.awt.*;

public class Title extends JPanel{

    public Title(String title , JButton back , JButton refresh ){

        //Editing Panel itself
        this.setPreferredSize(new Dimension(1200,100));
        this.setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Adding a refresh button
        c.insets = new Insets(0,50,0,0);
        add(refresh , c);

        //Adding label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        c.insets = new Insets(0,250,0,0);
        add(titleLabel , c);

        //Adding Back Button
        c.insets = new Insets(0,200,0,0);
        add(back , c);
    }
}
