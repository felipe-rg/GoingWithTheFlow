import Client.Patient;
import Methods.ControlCentre;
import Methods.GeneralWard;
import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class DisTransPage {

    public DisTransPage(ArrayList<Patient> transfers, ArrayList<Patient> discharges , ControlCentre methods)  {

        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("Discharge and Transfer Lists" , backButton , refreshButton);
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
                try {
                    DisTransPage page = new DisTransPage(methods.seeTransferList(), methods.seeDischargeList(), methods);    // class will be called again
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout( 2 , 1));

        JLabel TransferList = new JLabel("Transfer Patient List goes here");
        JLabel DischargeList = new JLabel("Discharge Patient List goes here");

        infoPanel.add(TransferList);
        infoPanel.add(DischargeList);

        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    public void panelPadding(JPanel panel){                                              // adds padding to JLabels for better spacing
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }
}
