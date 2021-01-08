package AMCWardPanels.TableFrames.Incoming;

import AMCWardPanels.TableFrames.MainPanel;
import Methods.GeneralWard;
import Methods.tableInfo.IncomingTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InTable {

    public InTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, IncomingTableData incomingTableData){
        //We create a new frame
        JFrame frame = new JFrame("Incoming Patients");

        //Creating panel containing the table
        InTablePanel inTablePanel = new InTablePanel(methods, incomingTableData, wardinfo);

        //We create the mainPanel where everything will be
        MainPanel mainPanel = new MainPanel(true, "INCOMING PATIENTS", 1);

        //We add the tablePanel to the mainPanel
        mainPanel.add(inTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
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
