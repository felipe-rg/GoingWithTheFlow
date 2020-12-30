package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class InTable {

    public InTable(){
        //We create a new frame
        JFrame frame = new JFrame("Incoming Patients");
        //We create the tablepanel with the table
        InTablePanel intablePanel = new InTablePanel();
        //We create the mainPanel where everything will be
        MainPanel mainPanel = new MainPanel(true, "Incoming Patients");
        //We add the table to the mainPanel
        mainPanel.add(intablePanel, BorderLayout.CENTER);

        //Adding mainPanel to frame
        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }
}
