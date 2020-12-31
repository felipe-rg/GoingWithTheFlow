package Panels.TableFrames;
import javax.swing.*;
import java.awt.*;

/*
    This class is the MainPanel where the table will be inputed, it contains the top, right and left side
    white panels to make things look good. It colorCode=true then it will also display the labels indicating
    the color code below the table

 */
public class MainPanel extends JPanel {
    public MainPanel(Boolean colorCode, String title){

        //Creating panels
        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();
        JPanel bottomPanel = new JPanel();
        JPanel colorCodePanel = new ColorCodePanel();

        //Creating titlepanel and adding title label
        JPanel topPanel = new JPanel();
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        topPanel.add(titleLabel);

        //Setting preferredsize (not extremely important because it is a BorderLayout)
        leftPanel.setPreferredSize(new Dimension(100,100));
        rightPanel.setPreferredSize(new Dimension(100,100));
        topPanel.setPreferredSize(new Dimension(100,100));
        bottomPanel.setPreferredSize(new Dimension(100,100));
        colorCodePanel.setPreferredSize(new Dimension(100,100));

        //SettingLayout and adding Panels
        this.setLayout(new BorderLayout());
        this.add(leftPanel, BorderLayout.WEST);
        this.add(rightPanel, BorderLayout.EAST);
        this.add(topPanel, BorderLayout.NORTH);

        //Depending on the value of the boolean colorCode we will have the bottom panel displaying the colors
        //or not (only true for the 'inTable' atm).
        if (colorCode == true){
            this.add(colorCodePanel, BorderLayout.SOUTH);
        }
        else{
            this.add(bottomPanel, BorderLayout.SOUTH);
        }

    }

}