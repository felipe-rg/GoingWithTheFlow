package AMCWardPanels.TableFrames.Incoming;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.TableFrames.Transfer.TransTable;
import AMCWardPanels.Title;
import Methods.GeneralWard;
import Methods.tableInfo.IncomingTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class InTable {

    public InTable(AMCWardPanels.Topography top, AMCWardPanels.WardInfo wardinfo, GeneralWard methods, IncomingTableData incomingTableData){
        //We create a new frame
        JFrame frame = new JFrame("Incoming Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Creating panel containing the table
        InTablePanel inTablePanel = new InTablePanel(methods, incomingTableData, wardinfo);

        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new InTable(top, wardinfo, methods, incomingTableData);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Incoming Patients", backButton, refreshButton, 350, 350);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //We create the mainPanel where everything will be
        MainPanel mainTablePanel = new MainPanel(true,  1);

        //We add the titlePanel and mainTablePanel to the mainPanel
        mainTablePanel.add(inTablePanel, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

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
