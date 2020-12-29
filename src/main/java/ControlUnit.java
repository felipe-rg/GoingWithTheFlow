import Panels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlUnit {

    JFrame f;                                                           // creates relevant fields
    JPanel mainPanel;

    JPanel incomingPanel;
    JPanel longStayPanel;
    JPanel AMCPanel;

    public ControlUnit() {                                              // constructor for control page

        f = new JFrame();
        mainPanel = new JPanel();
        outline(mainPanel);                                            // adds a border to panel

        // Title Panel
        JButton backButton = new JButton("Go Back To User Page");  // creates back button
        Title titlePanel = new Title("AMC Status" , backButton);   // calls title class to create panel for title
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        backButton.addActionListener(new ActionListener() {             // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                            // closes control page
                UserPage user = new UserPage();                         // creates user page (new JFrame)
            }
        });

        // Incoming Panel
        incomingPanel = new JPanel();
        outline(incomingPanel);
        panelPadding(incomingPanel);                                    // adds padding to the JPanel

        incomingPanel.setLayout(new BoxLayout(incomingPanel, BoxLayout.Y_AXIS));  // sets layout type of panel to be vertical

        JLabel incomingTitle = new JLabel("Incoming from A&E");
        incomingTitle.setFont (incomingTitle.getFont ().deriveFont (24.0f));
        labelPadding(incomingTitle);

        // incoming from A&E info
        JLabel text1 = new JLabel("Number of Patients coming from A&E:");
        text1.setFont (text1.getFont ().deriveFont (14.0f));
        labelPadding(text1);

        JButton r = new JButton("2");           // incoming patients with traffic light to represent time of arrival
        r.setBackground(Color.RED);
        r.setHorizontalAlignment(SwingConstants.CENTER);
        JButton y = new JButton("4");
        y.setBackground(Color.YELLOW);
        y.setHorizontalAlignment(SwingConstants.CENTER);
        JButton g = new JButton("6");
        g.setBackground(Color.GREEN);
        g.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel blank1 = new JLabel("   ");      // here for spacing
        labelPadding(blank1);

        JButton ipDetails = new JButton("Click here to see Incoming Patients Details");
        ipDetails.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        ipDetails.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                Incoming ipList = new Incoming();           // opens incoming patient list (new JFrame)
            }
        });

        incomingPanel.add(incomingTitle);                   // adds components to incoming panel
        incomingPanel.add(text1);
        incomingPanel.add(r);
        incomingPanel.add(y);
        incomingPanel.add(g);
        incomingPanel.add(blank1);
        incomingPanel.add(ipDetails);

        // Long-stay Panel
        longStayPanel = new JPanel();
        outline(longStayPanel);
        panelPadding(longStayPanel);
        longStayPanel.setLayout(new BoxLayout(longStayPanel, BoxLayout.Y_AXIS));

        JLabel titleLS = new JLabel("Long Stay Wards");
        titleLS.setFont (titleLS.getFont ().deriveFont (24.0f));
        labelPadding(titleLS);

        JLabel lcap = new JLabel("Longstay Ward Capacity: 95%");                       // add info
        lcap.setFont (lcap.getFont ().deriveFont (14.0f));
        labelPadding(lcap);

        JLabel freeBed2 = new JLabel("Number of Free Beds: 13");                       // add info
        freeBed2.setFont (freeBed2.getFont ().deriveFont (14.0f));
        labelPadding(freeBed2);

        JLabel blank2 = new JLabel("   ");
        labelPadding(blank2);

        JButton viewLS = new JButton("Click here to see Longstay Ward Information");
        viewLS.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        viewLS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                LongStay LSpage = new LongStay();           // opens long stay ward overview
            }
        });

        longStayPanel.add(titleLS);
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

        JLabel capAMC = new JLabel("AMC Ward Capacity: 95%");          // make dynamic
        labelPadding(capAMC);
        capAMC.setHorizontalAlignment(JLabel.CENTER);
        capAMC.setFont (capAMC.getFont ().deriveFont (18.0f));

        JLabel freeBedAMC = new JLabel("Free beds: 5");          // make dynamic
        labelPadding(freeBedAMC);
        freeBedAMC.setHorizontalAlignment(JLabel.CENTER);
        freeBedAMC.setFont (capAMC.getFont ().deriveFont (18.0f));

        JLabel disAMC = new JLabel("Number of discharges today: 4");          // make dynamic
        labelPadding(disAMC);
        disAMC.setHorizontalAlignment(JLabel.CENTER);
        disAMC.setFont (disAMC.getFont ().deriveFont (18.0f));

        JLabel tranAMC = new JLabel("Number of transfers today: 3");          // make dynamic
        labelPadding(tranAMC);
        tranAMC.setHorizontalAlignment(JLabel.CENTER);
        tranAMC.setFont (tranAMC.getFont ().deriveFont (18.0f));

        JLabel blank3 = new JLabel("   ");
        labelPadding(blank3);

        JButton AMCinfoBut = new JButton("Click here to see AMC ward Information");
        AMCinfoBut.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        AMCinfoBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                AMCInfo AMCPage = new AMCInfo();                // opens AMC ward overview page (new JFrame)
            }
        });

        JButton tdBut = new JButton("Click here to see AMC transfer and discharge information");
        tdBut.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        tdBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                DisTransPage dtList = new DisTransPage();           // opens transfer/discharge lists
            }
        });

        AMCPanel.add(titleAMC);
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

    public void labelPadding(JLabel label){                                              // adds padding to JLabels for better spacing
        label.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
    }

    public void panelPadding(JPanel panel){                                              // adds padding to JLabels for better spacing
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
    }

    public void outline (JPanel panel){                                             // adds outline to JPanels
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));
    }

}