package AMCWardPanels.TableFrames;

import Methods.GeneralWard;
import Methods.tableInfo.TotalTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TotTable {

    public TotTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, TotalTableData totalTableData){
        //We create a new frame
        JFrame frame = new JFrame("Total Patients in Ward");
        //We create the tablepanel with the table
        TotTablePanel totTablePanel = new TotTablePanel(methods, totalTableData);
        //We create the mainPanel where everything will be
        MainPanel mainPanel = new MainPanel(false, "TOTAL PATIENTS IN WARD");
        //We add the table to the mainPanel
        //mainPanel.add(totTablePanel, BorderLayout.CENTER);
        mainPanel.add(totTablePanel, BorderLayout.CENTER);
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