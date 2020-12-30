package Panels.TableFrames.Incoming;

import javax.swing.*;
import java.awt.*;

public class ColorCodePanel extends JPanel {

    public ColorCodePanel(){
        JLabel greenLabel = new JLabel("<2 hours");
        JLabel ambarLabel = new JLabel("2-3 hours");
        JLabel redLabel = new JLabel(">3 hours");

        this. setLayout(new GridLayout(1,3));
        this.add(greenLabel);
        this.add(ambarLabel);
        this.add(redLabel);



    }
}
