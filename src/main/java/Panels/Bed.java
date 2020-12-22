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

    public void setAge(Integer age){
        this.age = age;
    }


    public void printInfo(){
        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocation(300,300);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("Bed ID: "+this.getID(),SwingConstants.CENTER);
        JLabel genderLabel = new JLabel("\n Gender: "+this.getGender(),SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("\n Age: "+this.getAge(),SwingConstants.CENTER);


        //jbutton for editing bed
        JButton editButton = new JButton("Edit Patient Info");
        editButton.addActionListener(new ActionListener() {
            JTextField ageTextField;
            JTextField genderTextField;
            JButton confirmButton;

            @Override
            public void actionPerformed(ActionEvent evt) {
                JFrame editorFrame = new JFrame();
                editorFrame.setSize(300,300);
                editorFrame.setBackground(Color.WHITE);
                editorFrame.setVisible(true);
                editorFrame.setLocation(300,300);

                JPanel editorPanel = new JPanel();
                editorPanel.setLayout(new GridLayout(3,1));
                editorPanel.setBackground(Color.WHITE);

                ageTextField = new JTextField("new age");
                genderTextField = new JTextField("new gender");

                confirmButton = new JButton("Confirm Edits");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        age = Integer.parseInt(ageTextField.getText());
                        gender = genderTextField.getText().charAt(0);
                        printInfo();
                    }

                });

                editorPanel.add(ageTextField);
                editorPanel.add(genderTextField);
                editorPanel.add(confirmButton);
                editorPanel.setVisible(true);
                editorFrame.add(editorPanel);

            }
        });

        //this.setAge(newAge);

        // add the labels to a panel that will be added to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4,1));
        infoPanel.add(bedIdLabel);
        infoPanel.add(genderLabel);
        infoPanel.add(ageLabel);
        infoPanel.add(editButton);
        infoPanel.setVisible(true);


        infoFrame.add(infoPanel);

    }






}
