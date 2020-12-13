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
    public UIController(){

        //Instantiating Panels
        mainPanel = new JPanel();
        rhsPanel = new JPanel();
        topography = new Topography();
        title = new Title();
        bedStatus = new BedStatus();
        wardInfo = new WardInfo();


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

}

