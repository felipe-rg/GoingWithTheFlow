import Client.Patient;
import AMCWardPanels.TableFrames.ColorCodePanel;
import AMCWardPanels.Title;
import Methods.ControlCentre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Incoming {

    //private GeneralWard methods;

    public Incoming(ArrayList<Patient> incomingPatients, ControlCentre methods) {

        JFrame f = new JFrame();                   //creates JFrame
        JPanel mainPanel = new JPanel();           // creates MainPanel
        mainPanel.setBorder(BorderFactory.createMatteBorder(3, 3, 3, 3, Color.BLACK));

        JButton backButton = new JButton("Go Back");    // creates back button
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("Incoming Patients from A&E" , backButton, refreshButton, 240, 210);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    // closes incoming page
                ControlUnit control = new ControlUnit();        // opens control unit page (new JFrame
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                try {
                    Incoming page = new Incoming(methods.seeIncomingList() , methods);    // class will be called again
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        JPanel infoPanel = new JPanel();                        // creates info panel

        //JPanel inTablePanel = new InTablePanel(methods);

        //JLabel AMU1info = new JLabel("A&E Patient table goes here");
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel colorCodePanel = new ColorCodePanel();

        leftPanel.setPreferredSize(new Dimension(100,100));
        rightPanel.setPreferredSize(new Dimension(100,100));
        colorCodePanel.setPreferredSize(new Dimension(100,100));

        infoPanel.setLayout(new BorderLayout());
        infoPanel.add(leftPanel, BorderLayout.WEST);
        infoPanel.add(rightPanel, BorderLayout.EAST);
        infoPanel.add(colorCodePanel, BorderLayout.SOUTH);
        //infoPanel.add(inTablePanel, BorderLayout.CENTER);


        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(infoPanel, BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
}