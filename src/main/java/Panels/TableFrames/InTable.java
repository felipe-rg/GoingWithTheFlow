package Panels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InTable {

    public InTable(Panels.Topography top, Panels.WardInfo wardinfo, GeneralWard methods){
        //We create a new frame
        JFrame frame = new JFrame("Incoming Patients");

        //Creating panel containing the table
        InTablePanel inTablePanel = new InTablePanel(methods);

        //We create the mainPanel where everything will be
        MainPanel mainPanel = new MainPanel(true, "INCOMING PATIENTS");

        //We add the tablePanel to the mainPanel
        mainPanel.add(inTablePanel, BorderLayout.CENTER);

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
