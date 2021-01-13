package AMCWardPanels.TableFrames.Others;

import AMCWardPanels.TableFrames.MainPanel;
import AMCWardPanels.Title;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OthTable {

    public OthTable(GeneralWard methods){

        //Create frame and mainPanel containing everything
        JFrame frame = new JFrame("Others");
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Creating panel containing the table
        OthTablePanel othTablePanel = new OthTablePanel(methods);

        //Buttons that will be displayed in the title panel at the top
        JButton backButton = new JButton("Back");
        JButton refreshButton = new JButton("Refresh Page");

        //Refresh button action (reopen frame)
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                new OthTable(methods);
            }
        });

        //Back button action (close frame)
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        //Title Panel
        Title titlePanel = new Title("Other Patients", backButton, refreshButton, 350, 360);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));

        //We create the mainTablePanel where othTablePanel is introduced together with sidepanels
        MainPanel mainTablePanel = new MainPanel(false,  1, "AMC");
        mainTablePanel.add(othTablePanel, BorderLayout.CENTER);

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