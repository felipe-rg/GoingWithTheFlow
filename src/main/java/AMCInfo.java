import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AMCInfo {

    JFrame f;                                                           // field: JFrame for the Homepage
    JPanel mainPanel;                                                   // field: Panel in frame

    JPanel title;                                                       // field: Panels for frame

    public AMCInfo() {

        f = new JFrame();                                               // creates JFrame for Homepage
        mainPanel = new JPanel();

        JButton backButton = new JButton("Go Back");
        Title titlePanel = new Title("AMC Status" , backButton);

        backButton.addActionListener(new ActionListener() {                            // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                ControlUnit control = new ControlUnit();
            }
        });


        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1,3));

        JLabel AMU1info = new JLabel("AMU 1 table goes here");
        JLabel AMU2info = new JLabel("AMU 2 table goes here");
        JLabel AAU1info = new JLabel("AAU 1 table goes here");

        infoPanel.add(AMU1info);
        infoPanel.add(AMU2info);
        infoPanel.add(AAU1info);

        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(infoPanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    public void outline(JPanel panel) {                                             // adds outline to JPanels
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void padding(JLabel label) {                                              // adds padding to JLabels for better spacing
        label.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

    }
}

