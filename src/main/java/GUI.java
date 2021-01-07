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
        //Todo once we have the wardType we can remove below
        ArrayList<String> json = null;
        try {
            json = c.makeGetRequest("*", "wards", "wardtype='AMU'");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Ward> amuWards = c.wardsFromJson(json);
        ArrayList<Integer> amuIds = new ArrayList<Integer>();
        for(Ward w:amuWards){
            amuIds.add(w.getWardId());
        }

        try {
            if(amuIds.contains(ward.getWardId())){
                method = new AMCWard(wardId);
            }
            else { methods = new LongstayWard(wardId);}
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
                UserPage user = new UserPage();
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
        //Todo once we have the wardTyoe we can use this loop and remove below
        /*if(ward.getWardType.equals("AMU")) {
            UIc = new UIController(backButton, refreshButton, method);
        }

         */

        if(amuIds.contains(ward.getWardId())){
            UIc = new UIController(backButton, refreshButton, method);
        }
        else { UIc = new UIController(backButton, refreshButton, methods);}
        frame.add(UIc.getMainPanel());

        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}