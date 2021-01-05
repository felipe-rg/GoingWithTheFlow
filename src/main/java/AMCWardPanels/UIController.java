package AMCWardPanels;

import Methods.AMCWard;
import Methods.LongstayWard;

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
    LongstayWard methods;
    AMCWard method;


    //Constructor
    public UIController(JButton backButton, JButton refreshButton,  LongstayWard methods){
        this.methods = methods;
        setupOne();
        bedStatus = new BedStatus(methods);
        topography = new Topography(bedStatus, methods);
        title = new Title("AMC GUI", backButton, refreshButton, 420, 420);
        wardInfo = new WardInfo(topography, methods);

        setupTwo();
    }

    public UIController(JButton backButton, JButton refreshButton, AMCWard methods){
        this.method = methods;
        setupOne();

        bedStatus = new BedStatus(method);
        topography = new Topography(bedStatus, method);
        title = new Title("AMC GUI", backButton, refreshButton, 420, 420);
        wardInfo = new WardInfo(topography, method);

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
