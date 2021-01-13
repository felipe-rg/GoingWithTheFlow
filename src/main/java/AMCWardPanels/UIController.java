package AMCWardPanels;

import Methods.AMCWard;
import Methods.LongstayWard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
/*
    This class has a mainPanel that contains all of the other panels that make up the GUI for the
    AMC and LongStay wards.
 */
public class UIController {
    //Declaring Panels
    JPanel mainPanel;
    JPanel rhsPanel;
    Topography topography;
    Title title;
    BedStatus bedStatus;
    WardInfo wardInfo;
    LongstayWard methods;
    AMCWard method;


    //Constructor for LongStay Ward (notice we input LongStayWard methods)
    public UIController(JButton backButton, JButton refreshButton,  LongstayWard methods){
        //Instantiating panels
        this.methods = methods;
        setupOne();
        wardInfo = new WardInfo(methods);
        methods.setWardInfo(wardInfo);
        bedStatus = new BedStatus(methods);
        methods.setBedStat(bedStatus);
        topography = new Topography(bedStatus, methods);
        methods.setTopography(topography);
        try {
            title = new Title(methods.getWardName(methods.getWardId()), backButton, refreshButton, 420, 420);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setupTwo();
    }

    //Constructor for AMC Ward (notice we input AMCWard methods)
    public UIController(JButton backButton, JButton refreshButton, AMCWard method){
        //Instantiating panels
        this.method = method;
        setupOne();
        wardInfo = new WardInfo(method);
        method.setWardInfo(wardInfo);
        bedStatus = new BedStatus(method);
        method.setBedStat(bedStatus);
        topography = new Topography(bedStatus, method);
        method.setTopography(topography);
        try {
            title = new Title(method.getWardName(method.getWardId()), backButton, refreshButton, 420, 420);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Adding them to the mainPanel
        setupTwo();

    }

    private void setupOne(){
        //Instantiating Panels
        mainPanel = new JPanel();
        rhsPanel = new JPanel();
    }

    //Editing panels and adding them to the mainPanel
    private void setupTwo(){
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
