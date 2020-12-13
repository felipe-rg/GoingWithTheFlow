package Panels;

import javax.swing.*;
import java.awt.*;

public class UIController {
    //Declaring Panels
    JPanel mainPanel;
    Topography topography;
    Title title;
    BedStatus bedStatus;
    WardInfo wardInfo;


    //Constructor
    public UIController(){

        //Instantiating Panels
        mainPanel = new JPanel();
        topography = new Topography();
        title = new Title();
        bedStatus = new BedStatus();
        wardInfo = new WardInfo();

        //Adding Panels to mainPanel
        mainPanel.setLayout(null);
        mainPanel.add(topography);
        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(bedStatus, BorderLayout.EAST);
        mainPanel.add(wardInfo, BorderLayout.EAST);

    }

    //Function returning the mainPanel
    public JPanel getMainPanel(){
        return mainPanel;
    }

}
