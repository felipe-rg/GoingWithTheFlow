package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Title extends JPanel{

    public Title(String title , JButton back ){

        //Editing Panel itself
        this.setPreferredSize(new Dimension(1200,100));
        this.setBackground(Color.white);
        this.setLayout(new BorderLayout());

        //Adding refresh
        JLabel refreshLabel = new JLabel();
        add(refreshLabel, BorderLayout.WEST);

        //Adding label
        JLabel titleLabel = new JLabel(title , SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
        add(titleLabel , BorderLayout.CENTER);

        //Adding Back Button
        add(back , BorderLayout.EAST);
    }
}
