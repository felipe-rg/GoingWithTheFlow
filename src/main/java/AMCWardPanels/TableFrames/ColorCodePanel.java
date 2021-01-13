package AMCWardPanels.TableFrames;

import javax.swing.*;
import java.awt.*;

/*
    This class creates a colorcode Panel for the incoming lists. It uses the function createColorPanels
    that creates little panels made out of a button of the desired color and a label. Then it's just a matter
    of adding them to the general ColorCodePanel (this)

 */


public class ColorCodePanel extends JPanel {
    JPanel greenPanel;
    JPanel amberPanel;
    JPanel redPanel;

    //Constructor
    public ColorCodePanel(String ward){
    //Depending on if we are in AMC or in LongStay then we will visualize a different color code
        if (ward.equals("AMC")){
            //Creating individual colorpanels
            greenPanel = createColorPanels("<2 hours ago", Color.decode("#8ABB59"));
            amberPanel = createColorPanels("2-3 hours ago", Color.decode("#F9D88C"));
            redPanel = createColorPanels(">3 hours ago", Color.decode("#F76262"));
        }

        if (ward.equals("LS")){
            //Creating individual colorpanels
            greenPanel = createColorPanels(">5 hours until arrival", Color.decode("#8ABB59"));
            amberPanel = createColorPanels("<3 hours until arrival", Color.decode("#F9D88C"));
            redPanel = createColorPanels("<1 hours until arrival", Color.decode("#F76262"));
        }


        //Adding them
        this. setLayout(new GridLayout(1,3));
        addPanels(greenPanel, amberPanel, redPanel);

    }

    //Function that creates little panels each for each color button and label
    public JPanel createColorPanels(String text, Color color){

        //Instantiating panel, label and button
        JPanel colorPanel = new JPanel();
        JLabel colorLabel = new JLabel(text);
        JButton colorButton = new JButton(" ");

        //Editing button
        colorButton.setBackground(color);
        colorButton.setOpaque(true);
        colorButton.setSize(5, 10);
        colorButton.setEnabled(false);

        //Editing label
        setLabel(colorLabel);

        //Adding button and label
        colorPanel.add(colorButton);
        colorPanel.add(colorLabel);


        return colorPanel;
    }

    //Editing Label function
    public void setLabel(JLabel ... a){
        for (JLabel i:a){
            i.setOpaque(true);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 15));
        }
    }

    //Function to add several panels in a line
    public void addPanels(JPanel ... a){
        for (JPanel i:a){
            add(i);
        }
    }

}

