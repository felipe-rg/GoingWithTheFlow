import Methods.ControlCentre;
import AMCWardPanels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

/*
This page presents the overall picture of the flow of patients to, through and from the AMC
 */

public class ControlUnit {

    JFrame f;                                                           // creates relevant fields
    JPanel mainPanel;
    JPanel incomingPanel;
    JPanel longStayPanel;
    JPanel AMCPanel;
    ControlCentre methods;

    private static final Logger log= Logger.getLogger(ControlUnit.class.getName());

    public ControlUnit() {
        try {
            methods = new ControlCentre();              //Methods for control centre
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        log.info("Control Unit Started");

        f = new JFrame();
        mainPanel = new JPanel();
        outline(mainPanel);                                            // adds a border to panel

        // Title Panel
        JButton backButton = new JButton("Go Back To User Page");  // creates back button
        JButton refreshButton = new JButton("Refresh Page");
        // calls title class to create panel for title
        Title titlePanel = new Title("AMC Status" , backButton, refreshButton, 400, 380);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        backButton.addActionListener(new ActionListener() {             // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                            // closes control page
                try {
                    UserPage user = new UserPage();                     // creates user page (new JFrame)
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                ControlUnit control = new ControlUnit();    // class will be called again
            }
        });

        // Incoming Panel
        incomingPanel = new JPanel();
        outline(incomingPanel);
        panelPadding(incomingPanel);                                    // adds padding to the JPanel

        incomingPanel.setLayout(new GridLayout(8,1));  // sets layout type of panel to be vertical

        JLabel incomingTitle = new JLabel("Incoming from A&E");
        incomingTitle.setFont (incomingTitle.getFont ().deriveFont (24.0f));
        labelPadding(incomingTitle);

        // incoming from A&E info
        JLabel text1 = new JLabel("Number of Patients coming from A&E:");
        text1.setFont(text1.getFont ().deriveFont (14.0f));

        JLabel total = new JLabel("Total = " + String.valueOf(methods.getRedPatients()+methods.getGreenPatients()+methods.getOrangePatients()) , SwingConstants.CENTER);
        total.setFont (text1.getFont ().deriveFont (16.0f));
        labelPadding(text1);

        // traffic light visuals to show criticality of those coming from A&E
        JButton r = new JButton("Arrived more than 3 hours ago:   " + String.valueOf(methods.getRedPatients()));
        r.setFont(text1.getFont ().deriveFont (16.0f));
        r.setBackground(Color.decode("#E74C3C"));
        JButton y = new JButton("Arrived 2-3 hours ago:   " + String.valueOf(methods.getOrangePatients()));
        y.setFont(text1.getFont ().deriveFont (16.0f));
        y.setBackground(Color.decode("#F39C12"));
        JButton g = new JButton("Arrived less than 2 hours ago:   " + String.valueOf(methods.getGreenPatients()));
        g.setFont(text1.getFont ().deriveFont (16.0f));
        g.setBackground(Color.decode("#2ECC71"));

        JLabel blank1 = new JLabel("   ");      // here for spacing
        labelPadding(blank1);

        // button to access incoming patient list
        JButton ipDetails = new JButton("Click here to see Incoming Patients Details");
        ipDetails.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        ipDetails.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                Incoming ipList = new Incoming(methods);           // opens incoming patient list (new JFrame)
            }
        });

        incomingPanel.add(incomingTitle);                   // adds components to incoming panel
        incomingPanel.add(text1);
        incomingPanel.add(total);
        incomingPanel.add(r);
        incomingPanel.add(y);
        incomingPanel.add(g);
        incomingPanel.add(blank1);
        incomingPanel.add(ipDetails);

        // Long-stay Panel
        longStayPanel = new JPanel();
        outline(longStayPanel);
        panelPadding(longStayPanel);
        longStayPanel.setLayout(new GridLayout(6,1));

        JLabel titleLS = new JLabel("Long Stay Wards");
        titleLS.setFont (titleLS.getFont ().deriveFont (24.0f));
        labelPadding(titleLS);

        JLabel lcap = new JLabel("Longstay Ward Capacity: "+methods.getLongstayCapacityPerc()+"%");
        lcap.setFont (lcap.getFont ().deriveFont (14.0f));
        labelPadding(lcap);

        JLabel freeBed2 = new JLabel("Number of Free Beds: "+methods.getLongstayFreeBeds());
        freeBed2.setFont (freeBed2.getFont ().deriveFont (14.0f));
        labelPadding(freeBed2);

        JLabel blank2 = new JLabel("   ");
        labelPadding(blank2);

        // button to access Long Stay Ward statistics
        JButton viewLS = new JButton("Click here to see Longstay Ward Information");
        viewLS.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        viewLS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                LongStay LSpage = new LongStay(methods);           // opens long stay ward overview
            }
        });

        longStayPanel.add(titleLS);                               // adds components to Long Stay Panel
        longStayPanel.add(lcap);
        longStayPanel.add(freeBed2);
        longStayPanel.add(blank2);
        longStayPanel.add(viewLS);

        // AMC Panel
        AMCPanel = new JPanel();
        AMCPanel.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 3, Color.BLACK));
        AMCPanel.setLayout(new GridLayout(8,1));

        JLabel titleAMC = new JLabel("AMC");
        labelPadding(titleAMC);
        titleAMC.setHorizontalAlignment(JLabel.CENTER);
        titleAMC.setFont (titleAMC.getFont ().deriveFont (24.0f));

        JLabel capAMC = new JLabel("AMC Ward Capacity: "+methods.getAmcCapacityPerc()+"%");
        labelPadding(capAMC);
        capAMC.setHorizontalAlignment(JLabel.CENTER);
        capAMC.setFont (capAMC.getFont ().deriveFont (18.0f));

        JLabel freeBedAMC = new JLabel("Free beds: "+methods.getFreeBeds());
        labelPadding(freeBedAMC);
        freeBedAMC.setHorizontalAlignment(JLabel.CENTER);
        freeBedAMC.setFont (capAMC.getFont ().deriveFont (18.0f));

        JLabel disAMC = new JLabel("Number of discharges today: "+methods.getDischargePatients());
        labelPadding(disAMC);
        disAMC.setHorizontalAlignment(JLabel.CENTER);
        disAMC.setFont (disAMC.getFont ().deriveFont (18.0f));

        JLabel tranAMC = new JLabel("Number of transfers today: "+methods.getTransferPatients());
        labelPadding(tranAMC);
        tranAMC.setHorizontalAlignment(JLabel.CENTER);
        tranAMC.setFont (tranAMC.getFont ().deriveFont (18.0f));

        JLabel blank3 = new JLabel("   ");
        labelPadding(blank3);

        // button to access AMC Ward statistics
        JButton AMCinfoBut = new JButton("Click here to see AMC ward Information");
        AMCinfoBut.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        AMCinfoBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                AMCInfo AMCPage = new AMCInfo(methods);                // opens AMC ward overview page (new JFrame)
            }
        });

        // Button to access discharge/transfer lists
        JButton tdBut = new JButton("Click here to see AMC transfer and discharge information");
        tdBut.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        tdBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                DisTransPage dtList = new DisTransPage(methods);           // opens transfer/discharge page
            }
        });

        AMCPanel.add(titleAMC);                 // adds components to AMC panel
        AMCPanel.add(capAMC);
        AMCPanel.add(freeBedAMC);
        AMCPanel.add(disAMC);
        AMCPanel.add(tranAMC);
        AMCPanel.add(blank3);
        AMCPanel.add(AMCinfoBut);
        AMCPanel.add(tdBut);

        // Main Panel arrangement
        mainPanel.setLayout(new BorderLayout());               // defines layout of MainPanel
        mainPanel.add(titlePanel , BorderLayout.NORTH);        // adds panels to MainPanel in correct position
        mainPanel.add(incomingPanel , BorderLayout.WEST);
        mainPanel.add(longStayPanel , BorderLayout.EAST);
        mainPanel.add(AMCPanel , BorderLayout.CENTER);

        // Frame setup
        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);

    }

    // adds padding to JLabels for better spacing
    public void labelPadding(JLabel label){
        label.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
    }

    // adds padding to JLabels for better spacing
    public void panelPadding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }

    // adds outline to JPanels
    public void outline (JPanel panel){
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));
    }

}
