package Panels;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BedButton extends JButton{
    String BedId;   // kept as string in case e decide to name them with characters too
    char status;    // free (f), if it is occupied (o) or closed (c)
    Integer age;    // when empty, age = 0
    char gender;    // when empty, gender = x
    String dia = "";    // diagnosis, not necessary to instantiate when creating bed
    Boolean sideroom;
    LocalDateTime ETD = LocalDateTime.now(); // right now if bed is empty or ETD is not set, ETD is current time

    //constructor; when instantiating a bed its location must be specified with x and y.
    public BedButton(String BedId, char status, Boolean sideroom, Integer x, Integer y, Integer age, char gender) {
        this.BedId = BedId;
        this.status = status; //f = full, e = empty, c = closed
        this.gender = gender;
        this.sideroom = sideroom;
        this.age = age;

        this.setText(BedId);
        this.setFont(new Font("Verdana", Font.PLAIN, 30));
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);
        if(status == 'F'){this.setBackground(Color.decode("#E74C3C")); }
        if(status == 'E'){this.setBackground(Color.decode("#2ECC71"));}
        if(status == 'C'){this.setBackground(Color.decode("#F39C12"));}
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
    public LocalDateTime getETD(){ return ETD; }

    public void makeFull(){
        this.status = 'F';
        this.setBackground(Color.decode("#E74C3C"));
        this.repaint();

    }
    public void makeEmpty(){
        this.status = 'E';
        this.setBackground(Color.decode("#2ECC71"));
        this.setAge(0);
        this.setGender('x');
        this.setDia("");
        this.setETDinMins(0);

    }

    public void setAge(Integer age){
        this.age = age;
    }
    public void setGender(char gender){ this.gender = gender;}
    public void setDia(String dia){
        this.dia = dia;
    }
    public void setSR(Boolean sr){ this.sideroom = sr; }
    public void setETDinMins(Integer Minutes){ this.ETD = LocalDateTime.now().plusMinutes(Minutes); }
    public void setETDinHrs(Integer Hours){ this.ETD = LocalDateTime.now().plusHours(Hours); }

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

        // formating ETD time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String ETDTime = this.getETD().format(formatter);
        JLabel ETDLabel = new JLabel("ETD: "+ETDTime, SwingConstants.CENTER);

        //jbutton for editing bed
        JButton editButton = new JButton("Edit Patient");
        // button to edit information when it is clicked
        editButton.addActionListener(evt -> {
            inputNewInfo();
            infoFrame.dispose();
        });

        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(evt -> {
            makeEmpty();
            infoFrame.dispose();
        });

        // add the labels to the frame
        infoFrame.setLayout(new GridLayout(8,1));
        infoFrame.add(bedIdLabel);
        infoFrame.add(ageLabel);
        infoFrame.add(genderLabel);
        infoFrame.add(srLabel);
        infoFrame.add(diaLabel);
        infoFrame.add(ETDLabel);

        infoFrame.add(editButton);
        infoFrame.add(deleteButton);
    }

    // prints text fields, gets info from user and updates gender and age
    public void inputNewInfo(){
        // new popup over which everything is put
        JFrame editorFrame = new JFrame();
        editorFrame.setSize(300,300);
        editorFrame.setBackground(Color.WHITE);
        editorFrame.setVisible(true);
        editorFrame.setLocation(300,300);
        editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // panel on editorFrame
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(5,1));
        editorPanel.setBackground(Color.WHITE);

        // add input fields to editorPanel
        JTextField ageTextField = new JTextField("New age");
        JTextField genderTextField = new JTextField("New gender");
        JTextField diaTextField = new JTextField(this.getDia());
        JTextField SRTextField = new JTextField("Sideroom");

        // confirm button on editorPanel. when clicked, this button must (1) assign new values to all fields, (2) change color from green to red if bed was empty
        JButton confirmButton = new JButton("Confirm Edits");
        confirmButton.addActionListener(evt -> {

            // change bed color
            if(this.getStatus() == 'E') {
                this.makeFull();
            }

            // asign new values
            setAge(Integer.parseInt(ageTextField.getText()));
            setGender(genderTextField.getText().charAt(0));
            setDia(diaTextField.getText());
            // there are many ways to indicate yes or no
            if(SRTextField.getText().equals("False") || SRTextField.getText().equals("false")  || SRTextField.getText().equals("FALSE") || SRTextField.getText().charAt(0) == 'F' || SRTextField.getText().charAt(0) == 'N' || SRTextField.getText().equals("No") || SRTextField.getText().equals("NO") || SRTextField.getText().equals("no")){
                setSR(false);
            }
            else if (SRTextField.getText().equals("True") || SRTextField.getText().equals("true")  || SRTextField.getText().equals("TRUE") || SRTextField.getText().charAt(0) == 'T' || SRTextField.getText().charAt(0) == 'Y' || SRTextField.getText().equals("Yes") || SRTextField.getText().equals("YES") || SRTextField.getText().equals("yes")){
                setSR(true);
            }

            // get rid of editor popup after confirming edits
            editorFrame.dispose();
        });

        editorPanel.add(ageTextField);
        editorPanel.add(genderTextField);
        editorPanel.add(diaTextField);
        editorPanel.add(SRTextField);
        editorPanel.add(confirmButton);
        editorPanel.setVisible(true);
        editorFrame.add(editorPanel);
    }


}
