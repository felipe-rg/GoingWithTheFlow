//Main Class
import Panels.UIController;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main (String[] args){

        JFrame frame = new JFrame();
        frame.setSize(1200,800);
        frame.setResizable(false);

        UIController UIc = new UIController();
        frame.add(UIc.getMainPanel());

        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);






    }

}
