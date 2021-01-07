package AMCWardPanels.TableFrames.Discharge;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.TableFrames.Others.OthTable;
import AMCWardPanels.TableFrames.Others.OthTablePanel;
import AMCWardPanels.Title;
import Methods.GeneralWard;
import Methods.tableInfo.DischargeTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DisTable {

    public DisTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, DischargeTableData dischargeTableData){
        //Create frame and mainPanel containing everything
        JFrame frame = new JFrame("Discharge Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Creating panel containing the table
        DisTablePanel disTablePanel = new DisTablePanel(methods, dischargeTableData);

        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new DisTable(top, wardinfo, methods, dischargeTableData);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Discharge Patients", backButton, refreshButton, 350, 360);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //We create the mainTablePanel where disTablePanel is introduced together with sidepanels
        MainPanel mainTablePanel = new MainPanel(false,  1);
        mainTablePanel.add(disTablePanel, BorderLayout.CENTER);

        //We add the titlePanel and mainTablePanel to the mainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);



        //Standard Frame editing
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