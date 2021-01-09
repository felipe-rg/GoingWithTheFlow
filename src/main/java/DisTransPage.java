import AMCWardPanels.TableFrames.MainPanel;
import CUTablePanels.DisTransTablePanel;
import Client.Patient;
import AMCWardPanels.Title;
import Methods.AMCWard;
import Methods.ControlCentre;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DisTransPage {

    private GeneralWard disMethods;
    private AMCWard transMethods;
    private DisTransTablePanel disTransTablePanel;

    private static final Logger log= Logger.getLogger(DisTransPage.class.getName());

    public DisTransPage(ControlCentre methods) {

        log.info("DisTransPage Started");

        //Frame and mainPanel where everything will be
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        //Buttons to go back and to refresh
        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Backbutton action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    //Close current frame
                ControlUnit control = new ControlUnit();        //Open previous page
            }
        });

        //Refreshbutton action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                DisTransPage page = new DisTransPage(methods);    //this class will be called again
            }
        });

        //TitlePanel containing title and back and refresh buttons
        Title titlePanel = new Title("Discharge and Transfer Lists" , backButton, refreshButton, 220, 200);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        //Panel displaying the tables
        disTransTablePanel = new DisTransTablePanel(methods);

        //MainPanel that contains panels on the sides to make table be at the centre
        MainPanel mainTablePanel = new MainPanel(false, 3, "AMC");
        mainTablePanel.add(disTransTablePanel, BorderLayout.CENTER);

        //Setting mainPanel Layout and adding title and mainTablePanel
        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel
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
