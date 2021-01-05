import AMCWardPanels.Title;
import Methods.ControlCentre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LongStay {

    public LongStay(ControlCentre methods) {
    //Todo Input format is not nice - think of better way


        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        //title panel
        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("Long-stay Ward Overview" , backButton, refreshButton, 250, 250);
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
                //LongStay page = new LongStay(methods.getAllWardInfo() , methods);    // class will be called again
            }
        });


        JPanel infoPanel = new JPanel();

        JLabel LSinfo = new JLabel("Longstay ward table goes here");


        infoPanel.add(LSinfo);

        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
