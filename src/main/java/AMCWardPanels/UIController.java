package AMCWardPanels;

import Methods.AMCWard;
import Methods.LongstayWard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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


    //Constructor
    public UIController(JButton backButton, JButton refreshButton,  LongstayWard methods){
        this.methods = methods;
        setupOne();
        bedStatus = new BedStatus(methods);
        methods.setBedStat(bedStatus);
        topography = new Topography(bedStatus, methods);
        methods.setTopography(topography);
        try {
            title = new Title(methods.getWardName(methods.getWardId()), backButton, refreshButton, 420, 420);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wardInfo = new WardInfo(topography, methods);
        methods.setWardInfo(wardInfo);

        setupTwo();
    }

    public UIController(JButton backButton, JButton refreshButton, AMCWard method){
        this.method = method;
        setupOne();

        bedStatus = new BedStatus(method);
        method.setBedStat(bedStatus);
        topography = new Topography(bedStatus, method);
        method.setTopography(topography);
        try {
            title = new Title(method.getWardName(method.getWardId()), backButton, refreshButton, 420, 420);
        } catch (IOException e) {
            e.printStackTrace();
        }
        wardInfo = new WardInfo(topography, method);
        method.setWardInfo(wardInfo);

        setupTwo();

    }

    private void setupOne(){
        //Instantiating Panels
        mainPanel = new JPanel();
        rhsPanel = new JPanel();
    }

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

//    public void UpdateBedStatus(){
//        topography.CountBeds();
//        bedStatus.updateStatuses(topography.getECount(), topography.getCCount(), topography.getFCount());
//    }

}
