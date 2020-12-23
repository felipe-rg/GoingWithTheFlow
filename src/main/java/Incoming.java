import Panels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Incoming {

    public Incoming() {

        JFrame f = new JFrame();                   //creates JFrame
        JPanel mainPanel = new JPanel();           // creates MainPanel
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        JButton backButton = new JButton("Go Back");    // creates back button
        Title titlePanel = new Title("Incoming Patients from A&E" , backButton);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    // closes incoming page
                ControlUnit control = new ControlUnit();        // opens control unit page (new JFrame
            }
        });

        JPanel infoPanel = new JPanel();                        // creates info panel

        JLabel AMU1info = new JLabel("A&E Patient table goes here");


        infoPanel.add(AMU1info);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}