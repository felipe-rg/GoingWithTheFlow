import Methods.AMCWard;
import Panels.UIController;

//AMU GUI
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class AMCGUI {
    JButton backButton;
    AMCWard methods;

    public AMCGUI() {
        try {
            AMCWard methods = new AMCWard(2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JFrame frame = new JFrame();
        frame.setSize(1200, 800);
        frame.setTitle("AMC GUI");
        //frame.setResizable(false);

        //Creating button to go back to the User Page (it will be passed to UIController
        // and then to the Title panel to be added there
        backButton = new JButton("Back to UI Selector");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                UserPage user = new UserPage();
            }
        });

        //Creating UIController than generates all panels
        UIController UIc = new UIController(backButton, methods);
        frame.add(UIc.getMainPanel());

        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }
}