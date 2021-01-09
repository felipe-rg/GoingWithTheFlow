package AMCWardPanels;

import javax.swing.*;
import java.awt.*;

/*
        This is a panel that consists of a label (the title itself) and two buttons: the refresh and the
        backbutton. The parameters are the title text, the buttons and the insets (distance between title
        and buttons for things to look good).

 */


public class Title extends JPanel{

    public Title(String title , JButton back , JButton refresh,  int titleInsets, int backInsets){

        //Editing Panel itself
        this.setPreferredSize(new Dimension(1200,100));
        this.setBackground(Color.white);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();


        //Creating title label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Verdana", Font.PLAIN, 50));

        // Adding a refresh button
        c.insets = new Insets(0, 30,0,0);
        add(refresh , c);

        //Adding title label
        c.insets = new Insets(0,titleInsets,0,0);
        add(titleLabel , c);

        //Adding Back Button
        c.insets = new Insets(0,backInsets,0,0);
        add(back , c);

    }
}