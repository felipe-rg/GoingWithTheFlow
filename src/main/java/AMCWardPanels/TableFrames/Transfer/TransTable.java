package AMCWardPanels.TableFrames.Transfer;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import Methods.AMCWard;
import Methods.tableInfo.TransTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TransTable {

    public TransTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, AMCWard methods, TransTableData transTableData){

        //Frame containing everything
        JFrame frame = new JFrame("Transferring Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Panel containing the table
        TransTablePanel transtablePanel = new TransTablePanel(methods, transTableData);


        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new TransTable(top, wardinfo, methods,transTableData);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Transfer Patients", backButton, refreshButton, 350, 350);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));


        //MainPanel that contains the tablePanel and the titlePanel
        MainPanel mainTablePanel = new MainPanel(false, 2);


        //Adding the title and the transtablePanel to the mainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainTablePanel.add(transtablePanel, BorderLayout.CENTER);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

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