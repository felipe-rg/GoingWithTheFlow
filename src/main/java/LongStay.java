import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import CUTablePanels.LongStayTablePanel;
import Methods.ControlCentre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

/*
This page is used to display the bed statistics of each long stay ward
 */

public class LongStay {

    private static final Logger log= Logger.getLogger(LongStay.class.getName());

    public LongStay(ControlCentre methods) {

        log.info("LongStay Started");

        //Frame and mainPanel where everything will be
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        //Buttons to go back and to refresh
        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Backbutton action
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    //Close current frame
                ControlUnit control = new ControlUnit();        //Open previous page
            }
        });

        //RefreshButton action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                LongStay page = new LongStay(methods);    // class will be called again
            }
        });

        //TitlePanel containing title and back and refresh buttons
        Title titlePanel = new Title("Long Stay Ward Overview" , backButton, refreshButton, 250, 250);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        //Panel displaying the table
        LongStayTablePanel longStayTablePanel = new LongStayTablePanel(methods);

        //MainPanel that contains panels on the sides to make table be at the centre
        MainPanel mainTablePanel = new MainPanel(false,  2, "AMC");
        mainTablePanel.add(longStayTablePanel, BorderLayout.CENTER);

        //Setting mainPanel Layout and adding title and mainTablePanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        f.getContentPane().add(mainPanel);

        //Basic frame editing
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}
