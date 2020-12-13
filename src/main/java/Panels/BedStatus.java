package Panels;

import javax.swing.*;
import java.awt.*;

public class BedStatus extends JPanel{
    JLabel greenBeds;
    JLabel ambarBeds;
    JLabel redBeds;

    public BedStatus(){
        this.setPreferredSize(new Dimension(300,100));

        greenBeds = new JLabel("3");
        greenBeds.setBackground(Color.decode("#2ECC71"));
        greenBeds.setOpaque(true);
        greenBeds.setHorizontalAlignment(JLabel.CENTER);
        greenBeds.setVerticalAlignment(JLabel.CENTER);
        greenBeds.setFont(new Font("Verdana", Font.PLAIN, 30));

        ambarBeds = new JLabel("4");
        ambarBeds.setBackground(Color.decode("#F39C12"));
        ambarBeds.setOpaque(true);
        ambarBeds.setHorizontalAlignment(JLabel.CENTER);
        ambarBeds.setVerticalAlignment(JLabel.CENTER);
        ambarBeds.setFont(new Font("Verdana", Font.PLAIN, 30));

        redBeds = new JLabel("8");
        redBeds.setBackground(Color.decode("#E74C3C"));
        redBeds.setOpaque(true);
        redBeds.setHorizontalAlignment(JLabel.CENTER);
        redBeds.setVerticalAlignment(JLabel.CENTER);
        redBeds.setFont(new Font("Verdana", Font.PLAIN, 30));

        this.setLayout(new GridLayout(1,3));
        add(greenBeds);
        add(ambarBeds);
        add(redBeds);

    }
}
