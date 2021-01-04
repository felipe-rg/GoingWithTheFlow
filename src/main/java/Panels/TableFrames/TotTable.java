package Panels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TotTable {

    public TotTable(Panels.Topography top, Panels.WardInfo wardinfo, GeneralWard methods){
        //We create a new frame
        JFrame frame = new JFrame("Total Patients in Ward");
        //We create the tablepanel with the table
        TotTablePanel totTablePanel = new TotTablePanel(methods);
        //We create the mainPanel where everything will be
        MainPanel mainPanel = new MainPanel(false, "TOTAL PATIENTS IN WARD");
        //We add the table to the mainPanel
        mainPanel.add(totTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                top.refresh();
                wardinfo.refresh();
            }
        });
    }
}