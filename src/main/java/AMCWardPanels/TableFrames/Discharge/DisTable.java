package AMCWardPanels.TableFrames.Discharge;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DisTable {

    public DisTable(GeneralWard methods){
        //Create frame and mainPanel containing everything
        JFrame frame = new JFrame("Discharge Patients");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Creating panel containing the table
        DisTablePanel disTablePanel = new DisTablePanel(methods);

        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Refresh button action (reopen frame)
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new DisTable(methods);
            }
        });

        //Backbutton action (close frame)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Discharge Patients", backButton, refreshButton, 350, 360);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //We create the mainTablePanel where disTablePanel is introduced together with sidepanels
        MainPanel mainTablePanel = new MainPanel(false,  1, "AMC");
        mainTablePanel.add(disTablePanel, BorderLayout.CENTER);

        //We add the titlePanel and mainTablePanel to the mainPanel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(mainTablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);


        //Standard Frame editing
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

    }
}