package Panels;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BedButton extends JButton{
    String BedId;   // kept as string in case e decide to name them with characters too
    char status;    // free (f), if it is occupied (o) or closed (c)
    Integer age;    // when empty, age = 0
    char gender;    // when empty, gender = x
    String dia = "";    // diagnosis, not necessary to instantiate when creating bed
    Boolean sideroom;

    //constructor; when instantiating a bed its location must be specified with x and y.
    public BedButton(String BedId, char status, Boolean sideroom, Integer x, Integer y, Integer age, char gender) {
        this.BedId = BedId;
        this.status = status; //f = full, e = empty, c = closed
        this.gender = gender;
        this.sideroom = sideroom;
        this.age = age;

        this.setText(BedId);
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);
        if(status == 'F'){this.setBackground(Color.RED); }
        if(status == 'E'){this.setBackground(Color.GREEN);}
        if(status == 'C'){this.setBackground(Color.YELLOW);}
    }

    // functions that return bed information
    public String getID(){
        return this.BedId;
    }
    public Integer getAge(){
        return this.age;
    }
    public char getGender(){
        return this.gender;
    }
    public char getStatus(){ return this.status; }
    public String getDia(){ return this.dia; }
    public Boolean getSR(){ return this.sideroom; }

    public void makeFull(){
        if(this.status == 'e'){
            this.status = 'f';
            this.setBackground(Color.GREEN);
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
    public void setDia(String dia){
        this.dia = dia;
    }
    public void setSR(Boolean sr){ this.sideroom = sr; }

    // creates a frame with a panel inside, then 3 labels with the patient information and an 'edit' button. Once clicked, the edit button calls the 'inputNewInfo' function
    public void printInfo(){
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocation(300,300);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("Bed ID: "+this.getID(),SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("Age: "+this.getAge(),SwingConstants.CENTER);
        JLabel genderLabel = new JLabel("Gender: "+this.getGender(),SwingConstants.CENTER);
        JLabel srLabel = new JLabel("Sideroom: "+this.getSR(), SwingConstants.CENTER);
        JLabel diaLabel = new JLabel("Diagnosis: "+this.getDia(), SwingConstants.CENTER);

        //jbutton for editing bed
        JButton editButton = new JButton("Edit Patient");
        // button to edit information when it is clicked
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                inputNewInfo();
                infoFrame.dispose();
            }
        });

        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                deleteInfo();
                infoFrame.dispose();
            }
        });

        // add the labels to a panel that will be added to the frame
        infoFrame.setLayout(new GridLayout(7,1));
        infoFrame.add(bedIdLabel);
        infoFrame.add(ageLabel);
        infoFrame.add(genderLabel);
        infoFrame.add(srLabel);
        infoFrame.add(diaLabel);

        infoFrame.add(editButton);
        infoFrame.add(deleteButton);
    }

    // prints text fields, gets info from user and updates gender and age
    public void inputNewInfo(){
        JFrame editorFrame = new JFrame();
        editorFrame.setSize(300,300);
        editorFrame.setBackground(Color.WHITE);
        editorFrame.setVisible(true);
        editorFrame.setLocation(300,300);
        editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(5,1));
        editorPanel.setBackground(Color.WHITE);

        JTextField ageTextField = new JTextField("New age");
        JTextField genderTextField = new JTextField("New gender");
        JTextField diaTextField = new JTextField(this.getDia());
        JTextField SRTextField = new JTextField("Sideroom");

        JButton confirmButton = new JButton("Confirm Edits");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if(getStatus() == 'e') {
                    makeFull();
                } // repaint bed in green
                setAge(Integer.parseInt(ageTextField.getText()));
                setGender(genderTextField.getText().charAt(0));
                setDia(diaTextField.getText());

                if(SRTextField.getText() == "False" || SRTextField.getText() == "false" || SRTextField.getText().charAt(0) == 'F'){
                    setSR(false);
                }
                else if (SRTextField.getText() == "True" || SRTextField.getText() == "true" || SRTextField.getText().charAt(0) == 'T'){
                    setSR(true);
                }

                editorFrame.dispose();
            }
        });

        editorPanel.add(ageTextField);
        editorPanel.add(genderTextField);
        editorPanel.add(diaTextField);
        editorPanel.add(SRTextField);
        editorPanel.add(confirmButton);
        editorPanel.setVisible(true);
        editorFrame.add(editorPanel);
    }

    public void deleteInfo(){
        this.setAge(0);
        this.setGender('x');
        this.makeEmpty();
    }
}
