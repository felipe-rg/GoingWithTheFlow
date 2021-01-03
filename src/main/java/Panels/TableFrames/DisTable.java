package Panels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;

public class DisTable {

    public DisTable(GeneralWard methods){
        //Creating new frame
        JFrame frame = new JFrame("Discharge Patients");

        //Creating panel containing the table
        DisTablePanel distablePanel = new DisTablePanel(methods);

        //Creating the mainpanel that will contain all panels
        MainPanel mainPanel = new MainPanel(false, "DISCHARGE PATIENTS");
        //Adding the tablepanel to the main panel
        mainPanel.add(distablePanel, BorderLayout.CENTER);

        //Adding mainpanel to frame
        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}