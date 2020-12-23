package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Bed extends JButton{
    String BedId;
    char status;    //Character saying if bed is free (f), if it is occupied (o) or closed (c)
    Integer age;
    char gender;
    private JFrame infoFrame = new JFrame();


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
    public void setGender(char gender){ this.gender = gender;}

    public void printInfo(){
        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocation(300,300);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("Bed ID: "+this.getID(),SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("Age: "+this.getAge(),SwingConstants.CENTER);
        JLabel genderLabel = new JLabel("Gender: "+this.getGender(),SwingConstants.CENTER);

        //jbutton for editing bed
        JButton editButton = new JButton("Edit Patient Info");
        // when button to edit information is clicked
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                inputNewInfo();
            }
        });

        // add the labels to a panel that will be added to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(4,1));
        infoPanel.add(bedIdLabel);
        infoPanel.add(ageLabel);
        infoPanel.add(genderLabel);
        infoPanel.add(editButton);


        infoFrame.add(infoPanel);
    }

    // prints text fields, gets info from user and updates gender and age
    public void inputNewInfo(){
        JFrame editorFrame = new JFrame();
        editorFrame.setSize(300,300);
        editorFrame.setBackground(Color.WHITE);
        editorFrame.setVisible(true);
        editorFrame.setLocation(300,300);

        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(3,1));
        editorPanel.setBackground(Color.WHITE);

        JTextField ageTextField = new JTextField("New age");
        JTextField genderTextField = new JTextField("New gender");

        JButton confirmButton = new JButton("Confirm Edits");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                setAge(Integer.parseInt(ageTextField.getText()));
                setGender(genderTextField.getText().charAt(0));
                infoFrame.dispose();
                editorFrame.dispose();

                System.out.print(getAge());
            }
        });

        editorPanel.add(ageTextField);
        editorPanel.add(genderTextField);
        editorPanel.add(confirmButton);
        editorPanel.setVisible(true);
        editorFrame.add(editorPanel);
    }

}
