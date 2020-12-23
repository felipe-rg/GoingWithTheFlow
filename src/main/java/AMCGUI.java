import Panels.UIController;

//AMU GUI
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AMCGUI {
    JButton backButton;

    public AMCGUI() {
        JFrame frame = new JFrame();
        frame.setSize(1200, 800);
        frame.setTitle("AMC GUI");
        //frame.setResizable(false);

        UIController UIc = new UIController();
        frame.add(UIc.getMainPanel());



        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}