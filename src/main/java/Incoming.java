import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Incoming {

    JFrame f;                                                           // field: JFrame for the Homepage
    JPanel mainPanel;                                                   // field: Panel in frame

    JPanel title;                                                       // field: Panels for frame

    public Incoming() {

        f = new JFrame();                                               // creates JFrame for Homepage
        mainPanel = new JPanel();

        JButton backButton = new JButton("Go Back");
        Title titlePanel = new Title("Incoming Patients from A&E" , backButton);

        backButton.addActionListener(new ActionListener() {                            // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                ControlUnit control = new ControlUnit();
            }
        });

        JPanel infoPanel = new JPanel();

        JLabel AMU1info = new JLabel("A&E Patient table goes here");


        infoPanel.add(AMU1info);

        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}