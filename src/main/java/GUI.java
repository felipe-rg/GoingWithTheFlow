import Client.*;
import Methods.AMCWard;
import Methods.LongstayWard;
import AMCWardPanels.UIController;

//AMU GUI
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GUI {
    JButton backButton;
    JButton refreshButton;
    LongstayWard methods;
    AMCWard method;
    int wardId;

    private static final Logger log= Logger.getLogger(GUI.class.getName());

    public GUI(Ward ward) {

        log.info("Ward GUI Started");

        Client c = new Client();
        this.wardId = ward.getWardId();
        ArrayList<String> json = null;

        //Creating frame with the appropriate title
        JFrame frame = new JFrame();
        frame.setSize(1200, 800);
        if(ward.getWardId() == 3 || ward.getWardId() == 4 || ward.getWardId() == 5){
            frame.setTitle("Longstay Ward");
        }
        else frame.setTitle("AMC Ward");


        //Creating button to go back to the User Page (it will be passed to UIController
        //and then to the Title panel to be added there
        backButton = new JButton("Back to UI Selector");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                try {
                    UserPage user = new UserPage();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        //Refresh button
        refreshButton = new JButton("Refresh Page");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                GUI gui  = new GUI(ward);

            }
        });

        //Creating UIController than generates all panels
        UIController UIc = null;
        //Creating UIController for AMC Wards and tracking status in Log
        if(ward.getWardType().equals("AMU")) {
            try {
                method = new AMCWard(wardId);
                UIc = new UIController(backButton, refreshButton, method);
                log.info("AMC Ward Started");
            } catch (IOException e) {
                e.printStackTrace();
                log.severe("AMC Ward cannot setup");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                log.severe("AMC Ward cannot setup");
            }
        }
        //Creating UIController for LongStay Wards and tracking status in Log
        else {
            try {
                methods = new LongstayWard(wardId);
                UIc = new UIController(backButton, refreshButton, methods);
                log.info("Longstay Ward setup");
            } catch (IOException e) {
                e.printStackTrace();
                log.severe("Longstay Ward cannot setup");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                log.severe("Longstay Ward cannot setup");
            }
        }

        //Adding mainPanel of the UI Controller to the frame
        frame.add(UIc.getMainPanel());

        //Basic frame editing
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}