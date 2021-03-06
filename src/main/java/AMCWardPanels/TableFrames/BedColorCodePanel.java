package AMCWardPanels.TableFrames;

import javax.swing.*;
import java.awt.*;


/*
    This class creates a colorcode Panel for the topography to explain bed colors. It uses the function
    createColorPanels that creates little panels made out of a button of the desired color and a label.
    Then it's just a matter of adding them to the general ColorCodePanel (this)

 */

public class BedColorCodePanel extends JPanel {

    public BedColorCodePanel(){

        //Creating individual color panels
        JPanel greenPanel = createColorPanels("Free Bed", Color.decode("#2ECC71"));
        JPanel amberPanel = createColorPanels("Bed will be free", Color.decode("#F39C12"));
        JPanel redPanel = createColorPanels("Occupied Bed", Color.decode("#E74C3C"));
        JPanel bluePanel = createColorPanels("Bed should be free", Color.decode("#1531e8"));
        JPanel blackPanel = createColorPanels("Closed Bed", Color.black);


        //Adding them
        this.setLayout(new GridLayout(1,5));
        addPanels(greenPanel, amberPanel, redPanel, bluePanel, blackPanel);

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
        colorPanel.setBackground(Color.white);


        return colorPanel;
    }

    //Editing Label function
    public void setLabel(JLabel ... a){
        for (JLabel i:a){
            i.setOpaque(true);
            i.setBackground(Color.white);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 12));
        }
    }

    //Function to add several panels in a line
    public void addPanels(JPanel ... a){
        for (JPanel i:a){
            add(i);
        }
    }
}


