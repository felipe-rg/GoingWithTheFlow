import Panels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlUnit {

    JFrame f;                                                           // field: JFrame for the Homepage
    JPanel mainPanel;                                                   // field: Panel in frame

    JPanel incoming;
    JPanel longStay;
    JPanel AMC;

    public ControlUnit() {                                                 // constructor of Homepage

        f = new JFrame();                                               // creates JFrame for Homepage
        mainPanel = new JPanel();
        outline(mainPanel);


        Title titlePanel = new Title("AMC Status");                                                                       // creates Title JPanel// adds padding

        incoming = new JPanel();
        outline(incoming);
        incoming.setLayout(new BoxLayout(incoming, BoxLayout.Y_AXIS));                              // sets layout type of incoming panel

        JLabel head1 = new JLabel("Incoming from A&E");
        head1.setFont (head1.getFont ().deriveFont (24.0f));
        padding(head1);

        JLabel body1 = new JLabel("Number of Patients coming from A&E:");                       // add info
        body1.setFont (body1.getFont ().deriveFont (14.0f));
        padding(body1);

        JButton r = new JButton("2");
        r.setBackground(Color.RED);
        JButton y = new JButton("4");
        y.setBackground(Color.YELLOW);
        JButton g = new JButton("6");
        g.setBackground(Color.GREEN);

        JLabel blank1 = new JLabel("   ");
        padding(blank1);

        JButton ipDetails = new JButton("Click here to see Incoming Patients Details");
        ipDetails.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        ipDetails.addActionListener(new ActionListener() {                            // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                Incoming patientList = new Incoming();
            }
        });

        incoming.add(head1);
        incoming.add(body1);
        incoming.add(r);
        incoming.add(y);
        incoming.add(g);
        incoming.add(blank1);
        incoming.add(ipDetails);


        longStay = new JPanel();
        outline(longStay);
        longStay.setLayout(new BoxLayout(longStay, BoxLayout.Y_AXIS));

        JLabel head2 = new JLabel("Long Stay Wards");
        head2.setFont (head2.getFont ().deriveFont (24.0f));
        padding(head2);

        JLabel lcap = new JLabel("Longstay Ward Capacity: 95%");                       // add info
        lcap.setFont (lcap.getFont ().deriveFont (14.0f));
        padding(lcap);

        JLabel freeBed2 = new JLabel("Number of Free Beds: 13");                       // add info
        freeBed2.setFont (freeBed2.getFont ().deriveFont (14.0f));
        padding(freeBed2);

        JLabel blank2 = new JLabel("   ");
        padding(blank2);

        JButton lsinfo = new JButton("Click here to see Longstay Ward Information");
        lsinfo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        longStay.add(head2);
        longStay.add(lcap);
        longStay.add(freeBed2);
        longStay.add(blank2);
        longStay.add(lsinfo);


        AMC = new JPanel();
        outline(AMC);
        AMC.setLayout(new BoxLayout(AMC, BoxLayout.Y_AXIS));

        JLabel head3 = new JLabel("AMC");
        padding(head3);
        head3.setFont (head3.getFont ().deriveFont (24.0f));


        JLabel cap = new JLabel("AMC Ward Capacity: 95%");          // make dynamic
        padding(cap);
        cap.setFont (cap.getFont ().deriveFont (18.0f));

        JLabel freeBed1 = new JLabel("Free beds: 5");          // make dynamic
        padding(freeBed1);
        freeBed1.setFont (cap.getFont ().deriveFont (18.0f));

        JLabel disch = new JLabel("Number of discharges today: 4");          // make dynamic
        padding(disch);
        disch.setFont (disch.getFont ().deriveFont (18.0f));

        JLabel trans = new JLabel("Number of transfers today: 3");          // make dynamic
        padding(trans);
        trans.setFont (trans.getFont ().deriveFont (18.0f));

        JLabel blank3 = new JLabel("   ");
        padding(blank3);

        JButton AMCinfo = new JButton("Click here to see AMC ward Information");
        AMCinfo.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        AMCinfo.addActionListener(new ActionListener() {                            // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                AMCInfo AMCPage = new AMCInfo();
            }
        });

        JButton td = new JButton("Click here to see AMC transfer and discharge information");
        td.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        AMC.add(head3);
        AMC.add(cap);
        AMC.add(freeBed1);
        AMC.add(disch);
        AMC.add(trans);
        AMC.add(blank3);
        AMC.add(AMCinfo);
        AMC.add(td);

        mainPanel.setLayout(new BorderLayout());                                    // defines layout of MainPanel

        mainPanel.add(titlePanel , BorderLayout.NORTH);                                  // adds panels to MainPanel in correct position
        mainPanel.add(incoming , BorderLayout.WEST);
        mainPanel.add(longStay , BorderLayout.EAST);
        mainPanel.add(AMC , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000, 600);
    }

    public void padding(JLabel label){                                              // adds padding to JLabels for better spacing
        label.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));
    }

    public void outline (JPanel panel){                                             // adds outline to JPanels
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

}
