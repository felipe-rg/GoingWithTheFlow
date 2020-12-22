import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    static GraphicsConfiguration gc;

    public static void main(String[] args) {

        JFrame frame = new JFrame(gc);                                          // Create a new JFrame
        JPanel mainPanel = new JPanel();

        JButton bt1 = new JButton("Click here to view AMC Ward");// creates button to access AMC GUI


        bt1.addActionListener(new ActionListener() {                       // waits for mouse to click button
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();                                           // closes Main JFrame
                AMCGUI AMC = new AMCGUI();                                 // constructs AMCGUI class
            }
        });
        //mainPanel.setLayout(new GridLayout(1, 0));
        mainPanel.add(bt1);                                                 // adds button to main panel

        /*
        JButton bt2 = new JButton("Click here to view Control Unit");      // creates button to access Control Unit
        bt2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                ControlUnit control = new ControlUnit();                                  // constructs Homepage class
            }
        });
        mainPanel.add(bt2);
        */
        frame.getContentPane().add(mainPanel);                              // adds MainPanel to frame

        frame.setSize(500, 600);                                // sets frame size
        frame.setVisible(true);                                             // makes JFrame visible
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);      // This closes the program when the frame is closed
    }
}
