import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import CUTablePanels.AMCInfoTablePanel;
import Methods.ControlCentre;
import Methods.GeneralWard;
import Methods.tableInfo.AMCInfoData;
import Methods.tableInfo.OtherTableData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

public class AMCInfo {

    private static final Logger log= Logger.getLogger(AMCInfo.class.getName());

    public AMCInfo(ControlCentre methods) {

        log.info("AMCInfo Started");

        //Frame and mainPanel where everything will be
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

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

        //Refreshbutton action
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                AMCInfo page = new AMCInfo(methods);               // class will be called again
            }
        });

        //TitlePanel containing title and back and refresh buttons
        Title titlePanel = new Title("AMC Ward Status" , backButton, refreshButton, 420, 420);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        //Panel displaying the table
        AMCInfoTablePanel amcInfoTablePanel = new AMCInfoTablePanel(methods);

        //MainPanel that contains panels on the sides to make table be at the centre
        MainPanel mainTablePanel = new MainPanel(false,4, "AMC");
        mainTablePanel.add(amcInfoTablePanel, BorderLayout.CENTER);


        //Setting mainPanel Layout and adding title and mainTablePanel
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

