package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class ColorCodePanel extends JPanel {

    public ColorCodePanel(){


        JPanel greenPanel = new JPanel();
        JLabel greenLabel = new JLabel("<2 hours ago");
        JButton greenButton = new JButton(" ");
        greenButton.setBackground(Color.decode("#8ABB59"));
        greenButton.setOpaque(true);
        greenButton.setEnabled(false);


        JPanel ambarPanel = new JPanel();
        JLabel ambarLabel = new JLabel("2-3 hours ago");
        JButton ambarButton = new JButton(" ");
        ambarButton.setBackground(Color.decode("#F9D88C"));
        ambarButton.setOpaque(true);
        ambarButton.setEnabled(false);

        JPanel redPanel = new JPanel();
        JLabel redLabel = new JLabel(">3 hours ago");
        JButton redButton = new JButton(" ");
        redButton.setBackground(Color.decode("#F76262"));
        redButton.setOpaque(true);
        redButton.setEnabled(false);



        setLabel(greenLabel, ambarLabel, redLabel);

        greenPanel.add(greenButton);
        greenPanel.add(greenLabel);

        ambarPanel.add(ambarButton);
        ambarPanel.add(ambarLabel);

        redPanel.add(redButton);
        redPanel.add(redLabel);


        this. setLayout(new GridLayout(1,3));
        this.add(greenPanel);
        this.add(ambarPanel);
        this.add(redPanel);

    }

    public void setLabel(JLabel ... a){
        for (JLabel i:a){
            i.setOpaque(true);
            i.setHorizontalAlignment(JLabel.CENTER);
            i.setVerticalAlignment(JLabel.CENTER);
            i.setFont(new Font("Verdana", Font.PLAIN, 20));
        }
    }
}

