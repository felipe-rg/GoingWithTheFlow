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

        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

        JButton backButton = new JButton("Go Back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("AMC Ward Status" , backButton, refreshButton, 420, 420);
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
                AMCInfo page = new AMCInfo(methods);               // class will be called again
            }
        });

        MainPanel mainTablePanel = new MainPanel(false,4);


        AMCInfoTablePanel amcInfoTablePanel = new AMCInfoTablePanel(methods);

        mainTablePanel.add(amcInfoTablePanel, BorderLayout.CENTER);




        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(mainTablePanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}

