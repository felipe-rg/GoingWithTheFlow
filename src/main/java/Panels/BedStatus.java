package Panels;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class BedStatus extends JPanel{
    JLabel greenBeds;
    JLabel ambarBeds;
    JLabel redBeds;

    public BedStatus(Integer Empty, Integer Closed, Integer Full){
        this.setPreferredSize(new Dimension(300,100));

        greenBeds = new JLabel(Empty.toString());
        greenBeds.setBackground(Color.decode("#2ECC71"));

        ambarBeds = new JLabel(Closed.toString());
        ambarBeds.setBackground(Color.decode("#F39C12"));

        redBeds = new JLabel(Full.toString());
        redBeds.setBackground(Color.decode("#E74C3C"));

        setLabel(greenBeds, ambarBeds, redBeds);

        this.setLayout(new GridLayout(1,3));
        add(greenBeds);
        add(ambarBeds);
        add(redBeds);


    }

    //Function in which you introduce labels and they are automatically edited
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

    static public void updateStatuses(Integer ECount, Integer CCount, Integer FCount){
        new BedStatus(ECount, CCount, FCount);
        //empty, closed and full
    }
}