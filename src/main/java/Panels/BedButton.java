package Panels;

import javax.swing.*;
import java.awt.*;

public class BedButton extends JButton{
    String BedId;
    char status;    //Character saying if bed is free (f), if it is occupied (o) or closed (c)

    public BedButton(String BedId, int x, int y, char status){
        this.status = status;
        this.BedId = BedId;

        //Editing button
        this.setText(BedId);
        this.setBounds(x, y, 75, 140);

        //Changing Color
        this.setOpaque(true);
        this.setBackground(Color.RED);


    }




}
