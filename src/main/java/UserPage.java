import Client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class UserPage {

    static GraphicsConfiguration gc;
    private static final Logger log= Logger.getLogger(UserPage.class.getName());

    public UserPage() throws IOException {

        log.info("UserPage Started");

        Client client = new Client();
        ArrayList<Ward> amuWards = new ArrayList<Ward>();
        ArrayList<Ward> lsWards = new ArrayList<Ward>();
        ArrayList<String> json = new ArrayList<String>();
        ArrayList<JButton> amuButtons = new ArrayList<>();


        JFrame frame = new JFrame(gc);                                                  // create a JFrame

        JPanel mainPanel = new JPanel();                                                // creates mainPanel for JFrame

        JPanel titlePanel = new JPanel();                                               // creates panel for title
        JLabel titleLabel = new JLabel("<html><div style='text-align:center'>Welcome to the<br>AMC Bed Manager</html>");          // adds Title text
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 80));             // sets text size
        titlePanel.add(titleLabel);                                                    // adds title to panel

        JPanel userPanel = new JPanel();                                               // creates panel to select user
        GridBagLayout threeColumns = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        userPanel.setLayout(threeColumns);

        JPanel amuPanel = new JPanel();
        JPanel lsPanel = new JPanel();
        JPanel othPanel = new JPanel();


        JLabel userLabel = new JLabel("Please select a user:" , SwingConstants.CENTER);
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        userPanel.add(userLabel, c);

        JButton controlButton = new JButton("Control Unit");     // creates button to access Control Unit
        controlButton.setFont(new Font("Verdana", Font.PLAIN, 20));

        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ControlUnit control = new ControlUnit();                        // constructs control page (new Jframe)
            }
        });

        JButton formButton = new JButton("Patient Form");     // creates button to access Patient Form
        formButton.setFont(new Font("Verdana", Font.PLAIN, 20));
        formButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                PatientForm form = new PatientForm();                        // constructs patient form
            }
        });
        othPanel.setLayout(new GridLayout(3,1));
        JLabel othLabel = new JLabel("Others" , SwingConstants.CENTER);
        othLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        othPanel.add(othLabel);
        othPanel.add(formButton);
        othPanel.add(controlButton);

        try {
            json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        amuWards = client.wardsFromJson(json);
        amuPanel.setLayout(new GridLayout(amuWards.size()+1, 1));

        JLabel amuLabel = new JLabel("AMU" , SwingConstants.CENTER);
        amuLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        amuPanel.add(amuLabel);

        for(Ward w:amuWards){
            JButton amuWard = new JButton(w.getWardName());     // creates button to access amu ward
            amuWard.setFont(new Font("Verdana", Font.PLAIN, 20));
            amuWard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    GUI gui = new GUI(w);                        // contructs amu ward
                }
            });
            amuPanel.add(amuWard);
        }

        try {
            json = client.makeGetRequest("*", "wards", "wardtype='LS'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        lsWards = client.wardsFromJson(json);
        lsPanel.setLayout(new GridLayout(lsWards.size()+1, 1));
        JLabel lsLabel = new JLabel("Long Stay" , SwingConstants.CENTER);
        lsLabel.setFont(new Font("Verdana", Font.PLAIN, 30));
        lsPanel.add(lsLabel);
        for(Ward w:lsWards){
            JButton lsWard = new JButton(w.getWardName());     // creates button to access longstay ward
            lsWard.setFont(new Font("Verdana", Font.PLAIN, 20));
            lsWard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                    GUI gui = new GUI(w);                        // constructs control page (new JFrame)
                }
            });
            lsPanel.add(lsWard);
        }

        c.gridy = 1;
        c.gridwidth = 1;
        userPanel.add(othPanel, c);
        c.gridx = 2;
        userPanel.add(amuPanel, c);
        c.gridx = 3;
        userPanel.add(lsPanel, c);

        // adding title and user option buttons to main Panel
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridheight = 3;
        c2.gridwidth = 7;
        c2.gridx = 0;
        c2.gridy = 0;
        mainPanel.add(titlePanel, c2); // adds title to the top of panel
        c2.gridheight = 2;
        c2.gridwidth = 4;
        c2.gridx = 3;
        c2.gridy = 5;
        mainPanel.add(userPanel , c2);                         // adds user panel below
        /*
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);                         // adds title to the top of panel
        mainPanel.add(userPanel , BorderLayout.CENTER);
                           // adds user panel below
         */

        frame.getContentPane().add(mainPanel);                                  // adds MainPanel to frame

        frame.setExtendedState(Frame.MAXIMIZED_BOTH);                           // frame will occupy the whole screen
        frame.setVisible(true);                                                 // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);          // This closes the program when the frame is closed
    }

    // padding method improves visual layout
    public void padding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(30, 500, 30, 500));
    }
}