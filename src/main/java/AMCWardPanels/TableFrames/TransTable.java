package AMCWardPanels.TableFrames;

import Methods.AMCWard;
import Methods.tableInfo.TransTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TransTable {

    public TransTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, AMCWard methods, TransTableData transTableData){


        JFrame frame = new JFrame("Transferring Patients");

        TransTablePanel transtablePanel = new TransTablePanel(methods, transTableData);

        MainPanel mainPanel = new MainPanel(false, "TRANSFERRING PATIENTS");

        mainPanel.add(transtablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        //When we close table, we refresh the homescreen
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                top.refresh(methods);
                wardinfo.refresh();
            }
        });
    }
}