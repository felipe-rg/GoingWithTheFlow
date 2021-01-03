package Panels.TableFrames;

import Methods.AMCWard;

import javax.swing.*;
import java.awt.*;

public class TransTable {

    public TransTable(AMCWard methods){
        JFrame frame = new JFrame("Transferring Patients");


        TransTablePanel transtablePanel = new TransTablePanel(methods);


        MainPanel mainPanel = new MainPanel(false, "TRANSFERRING PATIENTS");

        mainPanel.add(transtablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}