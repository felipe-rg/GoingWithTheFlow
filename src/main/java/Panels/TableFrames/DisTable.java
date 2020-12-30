package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class DisTable {

    public DisTable(){
        JFrame frame = new JFrame("Discharge Patients");


        DisTablePanel distablePanel = new DisTablePanel();

        MainPanel mainPanel = new MainPanel(false, "DISCHARGE PATIENTS");

        mainPanel.add(distablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}