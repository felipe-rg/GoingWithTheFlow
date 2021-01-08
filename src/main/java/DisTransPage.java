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

public class DisTransPage {

    private GeneralWard disMethods;
    private AMCWard transMethods;
    private DisTransTablePanel disTransTablePanel;

    public DisTransPage(ControlCentre methods) {

        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("Discharge and Transfer Lists" , backButton, refreshButton, 220, 200);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                ControlUnit control = new ControlUnit();
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                DisTransPage page = new DisTransPage(methods);    // class will be called again
            }
        });


        MainPanel mainTablePanel = new MainPanel(false, " ", 3);

        //JLabel TransferList = new JLabel("Transfer Patient List goes here");
        //JLabel DischargeList = new JLabel("Discharge Patient List goes here");

        disTransTablePanel = new DisTransTablePanel(methods);

        mainTablePanel.add(disTransTablePanel, BorderLayout.CENTER);

        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
