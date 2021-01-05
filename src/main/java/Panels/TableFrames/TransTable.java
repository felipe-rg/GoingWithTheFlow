package Panels.TableFrames;

import Methods.AMCWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TransTable {

    public TransTable(Panels.Topography top, Panels.WardInfo wardinfo, AMCWard methods){
        JFrame frame = new JFrame("Transferring Patients");


        TransTablePanel transtablePanel = new TransTablePanel(methods);


        MainPanel mainPanel = new MainPanel(false, "TRANSFERRING PATIENTS");

        mainPanel.add(transtablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        //When we close table, we refresh the homescreen
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                top.refresh();
                wardinfo.refresh();
            }
        });
    }
}