package Panels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;

public class OthTable {

    public OthTable(GeneralWard methods){
        JFrame frame = new JFrame("Others");

        OthTablePanel othTablePanel = new OthTablePanel(methods);



        MainPanel mainPanel = new MainPanel(false, "OTHER PATIENTS");
        mainPanel.add(othTablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}