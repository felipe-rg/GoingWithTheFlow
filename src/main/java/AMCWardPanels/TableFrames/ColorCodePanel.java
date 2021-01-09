package AMCWardPanels.TableFrames;

import javax.swing.*;
import java.awt.*;

/*
    This class creates a colorcode Panel for the incoming lists. It uses the function createColorPanels
    that creates little panels made out of a button of the desired color and a label. Then it's just a matter
    of adding them to the general ColorCodePanel (this)

 */


public class ColorCodePanel extends JPanel {

    //Constructor
    public ColorCodePanel(){

        //Creating individual colorpanels
        JPanel greenPanel = createColorPanels("Free Bed", Color.decode("#8ABB59"));
        JPanel amberPanel = createColorPanels("Bed will become free", Color.decode("#F9D88C"));
        JPanel redPanel = createColorPanels("Occupied Bed", Color.decode("#F76262"));

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

