package AMCWardPanels;

import javax.swing.*;
import java.awt.*;

public class BedColorCodePanel extends JPanel {

    public BedColorCodePanel(){

        JPanel greenPanel = createColorPanels("Free Bed", Color.green);
        JPanel amberPanel = createColorPanels("Bed will become free", Color.decode("#F39C12"));
        JPanel redPanel = createColorPanels("Occupied Bed", Color.red);
        JPanel blackPanel = createColorPanels("Closed Bed", Color.black);
        JPanel bluePanel = createColorPanels("ETD Passed", Color.blue);

        this.setLayout(new GridLayout(1,5));
        addPanels(greenPanel, amberPanel, redPanel, blackPanel, bluePanel);

    }


    //Function that creates little panels each for each color and label
    public JPanel createColorPanels(String text, Color color){

        JPanel colorPanel = new JPanel();
        JLabel colorLabel = new JLabel(text);
        JButton colorButton = new JButton(" ");


        colorButton.setBackground(color);
        colorButton.setOpaque(true);
        colorButton.setSize(5, 10);
        colorButton.setEnabled(false);


        setLabel(colorLabel);

        colorPanel.add(colorButton);
        colorPanel.add(colorLabel);
        colorPanel.setBackground(Color.white);


        return colorPanel;
    }

    public void setLabel(JLabel ... a){
        for (JLabel i:a){
            i.setOpaque(true);
            i.setBackground(Color.white);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 12));
        }
    }

    public void addPanels(JPanel ... a){
        for (JPanel i:a){
            add(i);
        }
    }
}


