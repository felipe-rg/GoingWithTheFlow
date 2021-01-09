import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import CUTablePanels.AMCInfoTablePanel;
import Methods.ControlCentre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AMCInfo {

    public AMCInfo(ControlCentre methods) {

        //Creating frame and mainPanel
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

        //Instantiating back and refresh buttons
        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Creating titlePanel and adding a border
        Title titlePanel = new Title("AMC Status" , backButton, refreshButton, 420, 420);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        //Backbutton action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    // closes incoming page
                ControlUnit control = new ControlUnit();        //Reopens Controlunit frame
            }
        });

        //Refreshbutton action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                AMCInfo page = new AMCInfo(methods);               // class will be called again
            }
        });


        //tablePanel containing the table
        AMCInfoTablePanel amcInfoTablePanel = new AMCInfoTablePanel(methods);
        //MainPanel that contains panels on the sides to make table be at the centre
        MainPanel mainTablePanel = new MainPanel(false,4);
        //Adding the table to the mainTablePanel
        mainTablePanel.add(amcInfoTablePanel, BorderLayout.CENTER);

        //Adding title and table panels to mainPanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(mainTablePanel , BorderLayout.CENTER);

        //Adding mainPanel to frame
        f.getContentPane().add(mainPanel);

        //Basic frame editing
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}

