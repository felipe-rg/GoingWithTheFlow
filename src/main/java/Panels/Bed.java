package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bed extends JButton{
    String BedId;
    char status;    //Character saying if bed is free (f), if it is occupied (o) or closed (c)
    Integer age;
    char gender;
    JFrame infoFrame = new JFrame();


    public Bed(String BedId, char status, Integer x, Integer y, Integer age, char gender) {
        this.BedId = BedId;
        this.status = status; //f = full, e = empty, c = closed
        this.gender = gender;
        this.age = age;

        this.setText(BedId);
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);
        if(status == 'f'){setBackground(Color.RED); }
        if(status == 'e'){setBackground(Color.GREEN);}
        if(status == 'c'){setBackground(Color.YELLOW);}
    }

    public String getID(){
        return this.BedId;
    }

    public Integer getAge(){
        return this.age;
    }

    public char getGender(){
        return this.gender;
    }

    public void makeFull(){
        if(this.status == 'e'){
            this.status = 'f';
        }
    }

    public void makeEmpty(){
        if(this.status == 'f' || this.status == 'c'){
            this.status = 'e';
        }
    }

    public void printInfo(){
        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        infoFrame.setLocation(300,300);

        // labels with the bed information using methods from class Bed
        JLabel bedId = new JLabel("Bed ID: "+this.getID());
        JLabel gender = new JLabel("\n Gender: "+this.getGender());
        JLabel age = new JLabel("\n Age: "+this.getAge());

        bedId.setVisible(true);
        gender.setVisible(true);
        age.setVisible(true);

        //jbutton for editing bed
        JButton editButton = new JButton("Edit Patient Info");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                
            }
        });

        // add the labels to a panel that will be added to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.add(bedId);
        infoPanel.add(gender);
        infoPanel.add(age);
        infoPanel.add(editButton);
        infoPanel.setVisible(true);


        infoFrame.add(infoPanel);

    }





}
