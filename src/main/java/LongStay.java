import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LongStay {

    JFrame f;                                                           // field: JFrame for the Homepage
    JPanel mainPanel;                                                   // field: Panel in frame

    JPanel title;                                                       // field: Panels for frame

    public LongStay() {

        f = new JFrame();                                               // creates JFrame for Homepage
        mainPanel = new JPanel();

        JButton backButton = new JButton("Go Back");
        Title titlePanel = new Title("Long-stay Ward Overview" , backButton);

        backButton.addActionListener(new ActionListener() {                            // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                ControlUnit control = new ControlUnit();
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
