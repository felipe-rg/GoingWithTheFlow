package AMCWardPanels;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;

/*
        This panel displays the number of free (green), occupied (red) and about to become free (amber) beds.
 */

public class BedStatus extends JPanel{
    JLabel greenBeds;
    JLabel ambarBeds;
    JLabel redBeds;
    JLabel blueBeds;
    JLabel blackBeds;

    public BedStatus(GeneralWard methods){
        this.setPreferredSize(new Dimension(300,100));


        //Creating and changing color of labels
        greenBeds = new JLabel("0");
        greenBeds.setBackground(Color.decode("#2ECC71"));

        ambarBeds = new JLabel("0");
        ambarBeds.setBackground(Color.decode("#F39C12"));

        redBeds = new JLabel("0");
        redBeds.setBackground(Color.decode("#E74C3C"));

        blueBeds = new JLabel("0");
        blueBeds.setBackground(Color.decode("#1531e8"));

        blackBeds = new JLabel("0");
        blackBeds.setBackground(Color.decode("#000000"));


        //
        setLabel(greenBeds, ambarBeds, redBeds, blackBeds, blueBeds);
        blackBeds.setForeground(Color.white);

        this.setLayout(new GridLayout(1,5));
        //Editing labels
        setLabel(greenBeds, ambarBeds, redBeds);

        //Defining layout and  adding labels
        this.setLayout(new GridLayout(1,3));
        add(greenBeds);
        add(ambarBeds);
        add(redBeds);
        add(blueBeds);
        add(blackBeds);

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

    //Functions allowing us to change the number in each of the labels
    public void setGreenBedsNum(Integer numberIn){
        greenBeds.setText(numberIn.toString());
    }

    public void setAmbarBedsNum(Integer numberIn){
        ambarBeds.setText(numberIn.toString());
    }

    public void setRedBedsNum(Integer numberIn){ redBeds.setText(numberIn.toString()); }

    public void setBlackBedsNum(Integer numberIn){ blackBeds.setText(numberIn.toString()); }

    public void setBlueBedsNum(Integer numberIn){ blueBeds.setText(numberIn.toString()); }

}
