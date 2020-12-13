package Panels;

import javax.swing.*;
import java.awt.*;

public class WardInfo extends JPanel{
    JLabel inLabel;
    JLabel transLabel;
    JLabel disLabel;
    JLabel totLabel;

    public WardInfo(){
        this.setPreferredSize(new Dimension(300,600));
        this.setBackground(Color.white);

        this.setLayout(new GridLayout(4,1));

        //Labels
        inLabel = new JLabel("Incoming Patients");
        transLabel = new JLabel("Transferring Patients");
        disLabel = new JLabel("Discharge Patients");
        totLabel = new JLabel("Total Patients");

        setLabel(inLabel, transLabel, disLabel, totLabel);

        //Adding into Panel
        addLabel(inLabel,transLabel,disLabel,totLabel);


    }

    public void setLabel(JLabel... a){
        for (JLabel i:a){
            //i.setOpaque(true);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 20));
        }
    }

    public void addLabel(JLabel ... a){
        for(JLabel i:a){
            add(i);
        }
    }


}
