package Panels.TableFrames;

import javax.swing.*;

public class TotTable {

    public TotTable(){
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setTitle("Total Patients in the Ward");
        //frame.setResizable(false);

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}