package Panels;

import javax.swing.*;
import java.awt.*;

public class UIController {
    //Declaring Panels
    JPanel mainPanel;
    JPanel rhsPanel;
    Topography topography;
    Title title;
    BedStatus bedStatus;
    WardInfo wardInfo;


    //Constructor
    public UIController(JButton backButton){

        //Instantiating Panels
        mainPanel = new JPanel();
        rhsPanel = new JPanel();
        topography = new Topography();
        title = new Title("AMC GUI", backButton);
        bedStatus = new BedStatus();
        wardInfo = new WardInfo();

        //Giving the panels a black border
        outline(topography,title, rhsPanel);


        //Adding Panels to mainPanel
        mainPanel.setLayout(new BorderLayout(0,0));
        mainPanel.add(topography);
        mainPanel.add(title, BorderLayout.NORTH);

        rhsPanel.setLayout(new BorderLayout(0,0));
        rhsPanel.add(bedStatus, BorderLayout.NORTH);
        rhsPanel.add(wardInfo, BorderLayout.CENTER);

        mainPanel.add(rhsPanel, BorderLayout.EAST);


    }

    //Function returning the mainPanel
    public JPanel getMainPanel(){
        return mainPanel;
    }

    //Function that creates borders around panel entered
    public void outline(JPanel ... a){
        for (JPanel i:a){
            i.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

}
