import AMCWardPanels.TableFrames.MainPanel;
import CUTablePanels.IncomingTablePanel;
import AMCWardPanels.Title;
import Methods.ControlCentre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/*
This page shows the list of patients coming from A&E
 */

public class Incoming {

    private static final Logger log= Logger.getLogger(Incoming.class.getName());

    public Incoming(ControlCentre methods) {

        log.info("Incoming Started");

        //Frame and mainPanel where everything will be
        JFrame f = new JFrame();                   //creates JFrame
        JPanel mainPanel = new JPanel();           // creates MainPanel
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        //Buttons to go back and to refresh
        JButton backButton = new JButton("Go Back");    // creates back button
        JButton refreshButton = new JButton("Refresh Page");

        //Backbutton action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    // closes incoming page
                ControlUnit control = new ControlUnit();        // opens control unit page (new JFrame
            }
        });

        //Refresh button action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                Incoming page = new Incoming(methods);    // class will be called again
            }
        });

        //TitlePanel containing title and back and refresh buttons
        Title titlePanel = new Title("Incoming Patients from A&E" , backButton, refreshButton, 240, 210);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        //TablePanel containing the incoming table scrollpane
        JPanel incomingTablePanel = new IncomingTablePanel(methods);

        //MainPanel that contains panels on the sides to make table be at the centre
        MainPanel mainTablePanel = new MainPanel(true, 2, "AMC");
        mainTablePanel.add(incomingTablePanel, BorderLayout.CENTER);

        //Setting mainPanel Layout and adding title and mainTablePanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        f.getContentPane().add(mainPanel);

        //Basic frame editing
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}