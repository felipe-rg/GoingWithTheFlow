
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;

public class UserPage {

    static GraphicsConfiguration gc;

    public UserPage() {


        JFrame frame = new JFrame(gc);                                                  // create a JFrame

        JPanel mainPanel = new JPanel();                                                // creates mainPanel for JFrame

        JPanel titlePanel = new JPanel();                                               // creates panel for title
        JLabel titleLabel = new JLabel("Welcome to the AMC Bed Manager");          // adds Title text
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 80));             // sets text size
        titlePanel.add(titleLabel);                                                    // adds title to panel

        JPanel userPanel = new JPanel();                                               // creates panel to select user

        JLabel userLabel = new JLabel("Please select a user:" , SwingConstants.CENTER);
        userLabel.setFont(new Font("Verdana", Font.PLAIN, 30));

        JButton wardButton = new JButton("Click here to view AMC Ward");           // creates button to access AMC GUI
        wardButton.setFont(new Font("Verdana", Font.PLAIN, 20));

        wardButton.addActionListener(new ActionListener() {                             // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();                                                        // closes current JFrame
                AMCGUI AMC = new AMCGUI();                                              // constructs AMC GUI (a new JFrame)
            }
        });

        JButton controlButton = new JButton("Click here to view Control Unit");     // creates button to access Control Unit
        controlButton.setFont(new Font("Verdana", Font.PLAIN, 20));

        controlButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ControlUnit control = new ControlUnit();                        // constructs control page (new Jframe)
            }
        });

        JButton formButton = new JButton("Click here to fill out Patient Form");     // creates button to access Patient Form
        formButton.setFont(new Font("Verdana", Font.PLAIN, 20));
        formButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                PatientForm form = new PatientForm();                        // constructs control page (new Jframe)
            }
        });

        userPanel.setLayout(new GridLayout(4,1));                     // sets user panel layout
        padding(userPanel);                                                    // adds padding to user panel
        userPanel.add(userLabel);                                              // adds components to user panel
        userPanel.add(wardButton);
        userPanel.add(controlButton);
        userPanel.add(formButton);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);                         // adds title to the top of panel
        mainPanel.add(userPanel , BorderLayout.CENTER);                         // adds user panel below

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