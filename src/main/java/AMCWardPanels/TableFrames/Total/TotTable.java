package AMCWardPanels.TableFrames.Total;

import AMCWardPanels.TableFrames.Discharge.DisTable;
import AMCWardPanels.TableFrames.Discharge.DisTablePanel;
import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import Methods.GeneralWard;
import Methods.tableInfo.TotalTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TotTable {

    public TotTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, TotalTableData totalTableData){

        //Create frame and mainPanel containing everything
        JFrame frame = new JFrame("Total Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Creating panel containing the table
        TotTablePanel totTablePanel = new TotTablePanel(methods, totalTableData);

        //Buttons that will be displayed in the title panel
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Actions of the buttons
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TotTable(top, wardinfo, methods, totalTableData);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Total Patients", backButton, refreshButton, 370, 370);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //We create the mainTablePanel where totTablePanel is introduced together with sidepanels
        MainPanel mainTablePanel = new MainPanel(false,  1);
        mainTablePanel.add(totTablePanel, BorderLayout.CENTER);

        //We add the titlePanel and mainTablePanel to the mainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);



        //Standard Frame editing
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);





        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                top.refresh(methods);
                wardinfo.refresh();
            }
        });
    }
}