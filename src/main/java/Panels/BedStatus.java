package Panels;

import javax.swing.*;
import java.awt.*;

public class BedStatus extends JPanel{
    JLabel greenBeds;
    JLabel ambarBeds;
    JLabel redBeds;

    public BedStatus(){
        this.setPreferredSize(new Dimension(300,100));

        greenBeds = new JLabel("2");
        greenBeds.setBackground(Color.decode("#2ECC71"));

        ambarBeds = new JLabel("3");
        ambarBeds.setBackground(Color.decode("#F39C12"));

        redBeds = new JLabel("3");
        redBeds.setBackground(Color.decode("#E74C3C"));

        setLabel(greenBeds, ambarBeds, redBeds);

        this.setLayout(new GridLayout(1,3));
        add(greenBeds);
        add(ambarBeds);
        add(redBeds);

    }

    public void setLabel(JLabel ... a){
        for (JLabel i:a){
            i.setOpaque(true);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 30));
        }
    }

    public void setGreenBedsNum(String numberIn){
        greenBeds.setText(numberIn);
    }

    public void setAmbarBedsNum(String numberIn){
        ambarBeds.setText(numberIn);
    }

    public void setRedBedsNum(String numberIn){
        redBeds.setText(numberIn);
    }
}
