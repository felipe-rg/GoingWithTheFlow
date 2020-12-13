package Panels;

import javax.swing.*;
import java.awt.*;

public class WardInfo extends JPanel{

    public WardInfo(){
        this.setPreferredSize(new Dimension(300,600));
        this.setBackground(Color.white);
        outline(this);
    }
    public void outline(JPanel panel){
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }
}
