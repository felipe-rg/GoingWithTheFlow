package AMCWardPanels.TableFrames.Others;

import AMCWardPanels.TableFrames.MainPanel;
import Methods.GeneralWard;
import Methods.tableInfo.OtherTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class OthTable {

    public OthTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, OtherTableData otherTableData){
        JFrame frame = new JFrame("Others");

        OthTablePanel othTablePanel = new OthTablePanel(methods, otherTableData);



        MainPanel mainPanel = new MainPanel(false, "OTHER PATIENTS", 1);
        mainPanel.add(othTablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

        //When we close table, we refresh the homescreen
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                top.refresh(methods);
                wardinfo.refresh();
                otherTableData.refresh();
            }
        });
    }
}