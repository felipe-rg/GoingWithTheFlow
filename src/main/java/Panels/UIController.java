package Panels;

import Methods.AMCWard;
import Methods.GeneralWard;
import Methods.LongstayWard;
import jdk.incubator.jpackage.main.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UIController {
    //Declaring Panels
    JPanel mainPanel;
    JPanel rhsPanel;
    Topography topography;
    Title title;
    BedStatus bedStatus;
    WardInfo wardInfo;
    AMCWardInfo amcWardInfo;


    //Constructor
    public UIController(JButton backButton, LongstayWard methods){
        //Instantiating Panels
        mainPanel = new JPanel();
        rhsPanel = new JPanel();
        bedStatus = new BedStatus(methods);
        topography = new Topography(bedStatus, methods);
        title = new Title("Longstay GUI", backButton);
        wardInfo = new WardInfo(methods);

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


        //window.setInterval(UpdateBedStatus(),10000);
    }
    public UIController(JButton backButton, AMCWard methods){
        //Instantiating Panels
        mainPanel = new JPanel();
        rhsPanel = new JPanel();
        bedStatus = new BedStatus(methods);
        topography = new Topography(bedStatus, methods);
        title = new Title("AMC GUI", backButton);
        amcWardInfo = new AMCWardInfo(methods);

        //Giving the panels a black border
        outline(topography,title, rhsPanel);

        //Adding Panels to mainPanel
        mainPanel.setLayout(new BorderLayout(0,0));
        mainPanel.add(topography);
        mainPanel.add(title, BorderLayout.NORTH);

        rhsPanel.setLayout(new BorderLayout(0,0));
        rhsPanel.add(bedStatus, BorderLayout.NORTH);
        rhsPanel.add(amcWardInfo, BorderLayout.CENTER);

        mainPanel.add(rhsPanel, BorderLayout.EAST);


        //window.setInterval(UpdateBedStatus(),10000);
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

//    public void UpdateBedStatus(){
//        topography.CountBeds();
//        bedStatus.updateStatuses(topography.getECount(), topography.getCCount(), topography.getFCount());
//    }

}
