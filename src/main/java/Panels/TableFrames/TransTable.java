package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class TransTable {

    public TransTable(){
        JFrame frame = new JFrame("Incoming Patients");


        TransTablePanel transtablePanel = new TransTablePanel();


        JPanel mainPanel = new JPanel();

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel topPanel = new JPanel();
        JPanel colorCodePanel = new ColorCodePanel();

        leftPanel.setPreferredSize(new Dimension(100,100));
        rightPanel.setPreferredSize(new Dimension(100,100));
        topPanel.setPreferredSize(new Dimension(100,100));
        colorCodePanel.setPreferredSize(new Dimension(100,100));


        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(colorCodePanel, BorderLayout.SOUTH);
        mainPanel.add(transtablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}