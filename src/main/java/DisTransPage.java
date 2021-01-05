import Client.Patient;
import AMCWardPanels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DisTransPage {

    public DisTransPage(ArrayList<Patient> transfers, ArrayList<Patient> discharges) {

        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        JButton backButton = new JButton("Go Back");
        Title titlePanel = new Title("Discharge and Transfer Lists" , backButton);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                ControlUnit control = new ControlUnit();
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
}
