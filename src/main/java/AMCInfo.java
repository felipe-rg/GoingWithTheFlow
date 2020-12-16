import javax.swing.*;
import java.awt.*;

public class AMCInfo {

    JFrame f;                                                           // field: JFrame for the Homepage
    JPanel mainPanel;                                                   // field: Panel in frame

    JPanel title;                                                       // field: Panels for frame

    public AMCInfo() {

        f = new JFrame();                                               // creates JFrame for Homepage
        mainPanel = new JPanel();

        title = new JPanel();                                                                       // creates Title JPanel
        title.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));      // adds padding
        outline(title);

        JLabel Title = new JLabel("AMC Ward Information");                                           // text for Title JPanel
        Title.setFont(Title.getFont().deriveFont(32.0f));                                        // font size of text
        padding(Title);

        JButton back = new JButton("Click here for Homepage");


        title.add(Title);
        title.add(back);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);


        mainPanel.setLayout(new GridLayout(3,1));
        mainPanel.add(title);


        f.getContentPane().add(mainPanel);                                          // adds MainPanel to JFrame
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setSize(1000, 600);
    }

    public void outline(JPanel panel) {                                             // adds outline to JPanels
        panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    }

    public void padding(JLabel label) {                                              // adds padding to JLabels for better spacing
        label.setBorder(BorderFactory.createEmptyBorder(30, 10, 20, 10));

    }
}

