import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AMCInfo {

    public AMCInfo() {

        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("AMC Status" , backButton , refreshButton);
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
                AMCInfo page = new AMCInfo();               // class will be called again
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

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(infoPanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}

