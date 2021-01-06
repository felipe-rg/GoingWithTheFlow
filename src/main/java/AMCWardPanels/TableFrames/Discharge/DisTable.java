package AMCWardPanels.TableFrames.Discharge;

import AMCWardPanels.TableFrames.MainPanel;
import Methods.GeneralWard;
import Methods.tableInfo.DischargeTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DisTable {

    public DisTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, DischargeTableData dischargeTableData){
        //Creating new frame
        JFrame frame = new JFrame("Discharge Patients");

        //Creating panel containing the table
        DisTablePanel distablePanel = new DisTablePanel(methods, dischargeTableData);

        //Creating the mainpanel that will contain all panels
        MainPanel mainPanel = new MainPanel(false, "DISCHARGE PATIENTS", 1);
        //Adding the tablepanel to the main panel
        mainPanel.add(distablePanel, BorderLayout.CENTER);

        //Adding mainpanel to frame
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