package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class InTable {

    public InTable(){
        JFrame frame = new JFrame("Incoming Patients");


        InTablePanel intablePanel = new InTablePanel();


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
        mainPanel.add(intablePanel, BorderLayout.CENTER);


        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}
