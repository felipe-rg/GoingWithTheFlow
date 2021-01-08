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

public class GUI {
    JButton backButton;
    JButton refreshButton;
    LongstayWard methods;
    AMCWard method;
    int wardId;

    public GUI(Ward ward) {
        Client c = new Client();
        this.wardId = ward.getWardId();
        ArrayList<String> json = null;

        JFrame frame = new JFrame();
        frame.setSize(1200, 800);
        frame.setTitle("Longstay GUI");
        //frame.setResizable(false);

        //Creating button to go back to the User Page (it will be passed to UIController
        // and then to the Title panel to be added there
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
        if(ward.getWardType().equals("AMU")) {
            try {
                method = new AMCWard(wardId);
                UIc = new UIController(backButton, refreshButton, method);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else {
            try {
                methods = new LongstayWard(wardId);
                UIc = new UIController(backButton, refreshButton, methods);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

        frame.add(UIc.getMainPanel());

        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}