package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class TransTable {

    public TransTable(){
        JFrame frame = new JFrame("Incoming Patients");


        TransTablePanel transtablePanel = new TransTablePanel();


        MainPanel mainPanel = new MainPanel(false, "Transferring Patients");

        mainPanel.add(transtablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}