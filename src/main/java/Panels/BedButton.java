package Panels;

import Client.Patient;
import Methods.GeneralWard;
import com.sun.tools.javac.tree.JCTree;
import jdk.vm.ci.meta.Local;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class BedButton extends JButton{
    private int BedId;   // kept as string in case e decide to name them with characters too
    private String status;    // free (f), if it is occupied (o) or closed (c)
    private String sex;    // when empty, gender = x
    private Boolean sideroom;
    private GeneralWard methods;
    private LocalDateTime ETD;
    //constructor; when instantiating a bed its location must be specified with x and y.
    public BedButton(GeneralWard methods, int BedId, String status, String sex, Boolean sideroom, Integer x, Integer y) {
        this.BedId = BedId;
        this.status = status; //f = full, e = empty, c = closed
        this.sideroom = sideroom;
        this.sex = sex;
        this.methods = methods;


        this.setText(String.valueOf(BedId));
        this.setFont(new Font("Verdana", Font.PLAIN, 30));
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);
        if(status == "F"){this.setBackground(Color.decode("#E74C3C")); }
        if(status == "E"){this.setBackground(Color.decode("#2ECC71")); }
        if(status == "C"){this.setBackground(Color.BLACK); }
    }

    // functions that return bed information
    public int getID(){
        return this.BedId;
    }
    public String getStatus(){ return this.status; }
    public Boolean getSR(){ return this.sideroom; }
    public String getSex(){ return this.sex; }
    public LocalDateTime getETD(){return this.ETD;}
    public void setETD(LocalDateTime time){this.ETD = time;}


    public void makeFull(){
        this.status = "F";
        this.setBackground(Color.decode("#E74C3C"));
        this.repaint();
    }
    public void makeEmpty(Patient p){
        try {
            methods.removePatient(p.getId(), BedId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.setStatus("E");
        this.setBackground(Color.decode("#2ECC71"));
    }
    public void makeClosed(){
        this.setStatus("C");
        this.setBackground(Color.BLACK);
    }

    public void setSex(String sex){this.sex = sex;}
    public void setSR(Boolean sr){ this.sideroom = sr; }
    public void setStatus(String status){ this.status = status; }

    // creates a frame with a panel inside, then 3 labels with the patient information and an 'edit' button. Once clicked, the edit button calls the 'inputNewInfo' function
    public void printInfoFull(){
        Patient p = new Patient();
        try {
            p = methods.getPatient(BedId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocation(300,300);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("Bed ID: "+this.getID(),SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("Age: "+p.getAge(),SwingConstants.CENTER);
        JLabel genderLabel = new JLabel("Gender: "+p.getSex(),SwingConstants.CENTER);
        JLabel srLabel = new JLabel("Sideroom: "+this.getSR(), SwingConstants.CENTER);
        JLabel diaLabel = new JLabel("Diagnosis: "+p.getInitialDiagnosis(), SwingConstants.CENTER);

        // formating ETD time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String ETDTime = p.getEstimatedTimeOfNext().format(formatter);
        JLabel ETDLabel = new JLabel("ETD: "+ETDTime, SwingConstants.CENTER);

        //jbutton for editing bed
        JButton editButton = new JButton("Edit Patient Information");
        // button to edit information when it is clicked
        Patient finalP = p;
        editButton.addActionListener(evt -> {
            inputNewPatientInfo(finalP);
            infoFrame.dispose();
        });

        // delete patient, making the bed empty
        JButton deleteButton = new JButton("Delete Patient");
        deleteButton.addActionListener(evt -> {
            makeEmpty(finalP);
            infoFrame.dispose();
        });


        JButton setETDButton = new JButton("Add ETD");
        setETDButton.addActionListener(evt -> {
            setETDFrame(finalP);
            infoFrame.dispose();
        });

        // add the labels to the frame
        infoFrame.setLayout(new GridLayout(10,1));
        infoFrame.add(bedIdLabel);
        infoFrame.add(ageLabel);
        infoFrame.add(genderLabel);
        infoFrame.add(srLabel);
        infoFrame.add(diaLabel);
        infoFrame.add(ETDLabel);

        infoFrame.add(setETDButton);
        infoFrame.add(editButton);
        infoFrame.add(deleteButton);
    }

    public void printInfoEmpty(){
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocation(300,300);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("Bed ID: "+this.getID(),SwingConstants.CENTER);
        JLabel srLabel = new JLabel("Sideroom: "+this.getSR(), SwingConstants.CENTER);
        JLabel sexLabel = new JLabel("Gender: "+this.getSex(), SwingConstants.CENTER);

        //jbutton for editing bed
        JButton editButton = new JButton("Edit Bed Info");
        // button to edit information when it is clicked
        editButton.addActionListener(evt -> {
            inputNewBedInfo();
            infoFrame.dispose();
        });

        // make bed closed
        JButton closeButton = new JButton("Close Bed");
        closeButton.addActionListener(evt -> {
            makeClosed();
            infoFrame.dispose();
        });

        // add the labels to the frame
        infoFrame.setLayout(new GridLayout(4,1));
        infoFrame.add(bedIdLabel);
        infoFrame.add(srLabel);
        infoFrame.add(sexLabel);

        infoFrame.add(editButton);
        infoFrame.add(closeButton);
    }

    // prints text fields, gets info from user and updates gender and age
    public void inputNewBedInfo() {
        // new popup over which everything is put
        JFrame editorFrame = new JFrame();
        editorFrame.setSize(300, 300);
        editorFrame.setBackground(Color.WHITE);
        editorFrame.setVisible(true);
        editorFrame.setLocation(300, 300);
        editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // panel on editorFrame
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(5, 1));
        editorPanel.setBackground(Color.WHITE);

        // add input fields to editorPanel
        JTextField genderTextField = new JTextField("New gender");
        JTextField SRTextField = new JTextField("Sideroom");

        // confirm button on editorPanel. when clicked, this button must (1) assign new values to all fields, (2) change color from green to red if bed was empty
        JButton confirmButton = new JButton("Confirm Edits");
        confirmButton.addActionListener(evt -> {

            // change bed color if bed goes from empty to full
            if (this.getStatus() == "E") {
                this.makeFull();
            }

            // asign new values for gender, age and diagnosis
            setSex(String.valueOf(genderTextField.getText().charAt(0)));

            // there are many ways to indicate yes or no
            // if sideroom is no
            if (SRTextField.getText().equals("False") || SRTextField.getText().equals("false") || SRTextField.getText().equals("FALSE") || SRTextField.getText().charAt(0) == 'F' || SRTextField.getText().charAt(0) == 'N' || SRTextField.getText().equals("No") || SRTextField.getText().equals("NO") || SRTextField.getText().equals("no")) {
                setSR(false);
            }
            // if sideroom is yes
            else if (SRTextField.getText().equals("True") || SRTextField.getText().equals("true") || SRTextField.getText().equals("TRUE") || SRTextField.getText().charAt(0) == 'T' || SRTextField.getText().charAt(0) == 'Y' || SRTextField.getText().equals("Yes") || SRTextField.getText().equals("YES") || SRTextField.getText().equals("yes")) {
                setSR(true);
            }

            // get rid of editor popup after confirming edits
            editorFrame.dispose();
        });
    }

        private void inputNewPatientInfo(Patient p){
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
            JTextField diaTextField = new JTextField("Diagnosis");

            // confirm button on editorPanel. when clicked, this button must (1) assign new values to all fields, (2) change color from green to red if bed was empty
            JButton confirmButton = new JButton("Confirm Edits");
            confirmButton.addActionListener(evt -> {

                // asign new values for gender, age and diagnosis
                try {
                    methods.editPatient(p.getId(), "initialdiagnosis", diaTextField.getText());
                    methods.editPatient(p.getId(), "age", String.valueOf(Integer.parseInt(ageTextField.getText())));
                    methods.editPatient(p.getId(), "sex", String.valueOf(genderTextField.getText().charAt(0)));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

                // get rid of editor popup after confirming edits
                editorFrame.dispose();
            });

        editorPanel.add(ageTextField);
        editorPanel.add(genderTextField);
        editorPanel.add(diaTextField);
        editorPanel.add(confirmButton);
        editorPanel.setVisible(true);
        editorFrame.add(editorPanel);
    }

    public void setETDFrame(Patient p){
        // frame popup where the panel, textfields and confirm button are
        JFrame ETDFrame = new JFrame();
        ETDFrame.setSize(300,300);
        ETDFrame.setBackground(Color.WHITE);
        ETDFrame.setVisible(true);
        ETDFrame.setLocation(300,300);
        ETDFrame.setLayout(new GridLayout(2,1));

        // pannel where textfields and confirm button are
        JPanel ETDPanel = new JPanel();
        ETDPanel.setBackground(Color.WHITE);
        ETDPanel.setLayout(new GridLayout(2,2));

        JTextField InputHrs = new JTextField("ETD Hours");
        JTextField InputMins = new JTextField("ETD Minutes");
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(evt -> {

            //check that minutes is <60 and
            if (Integer.parseInt(InputMins.getText()) > 59) { Warning("Minutes must be an integer less than 59"); }
            //check that hours is between 00 and 23
            if (Integer.parseInt(InputHrs.getText()) > 23) { Warning("Hours must be an integer less than 24"); }

            // if both hours and minutes have been inputted, then set the new time on the same day but the new time
            if (!InputHrs.getText().equals("ETD Hours") && !InputMins.getText().equals("ETD Minutes")) {
                int Minutes = Integer.parseInt(InputMins.getText());
                int Hours = Integer.parseInt(InputHrs.getText());
                this.setETD(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth(), Hours, Minutes));
                ETDFrame.dispose(); //close popup
            }
            // if only hours are inputted, change to the new hour o'clock
            if (!InputHrs.getText().equals("ETD Hours") && InputMins.getText().equals("ETD Minutes")) {
                int Minutes = 00;
                int Hours = Integer.parseInt(InputHrs.getText());
                this.setETD(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth(), Hours, Minutes));
                ETDFrame.dispose(); //close popup
            }
            // if only minutes are inputted, change ETD to the new minutes in the current hour
            if (InputHrs.getText().equals("ETD Hours") && !InputMins.getText().equals("ETD Minutes")) {
                int Minutes = Integer.parseInt(InputMins.getText());
                int Hours = LocalDateTime.now().getHour();

                // if the new ETD is in the past
                if(Minutes < LocalDateTime.now().getMinute()) {
                    this.Warning("Time is in the past");
                }

                else{ this.setETD(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(),LocalDateTime.now().getDayOfMonth(), Hours, Minutes)); }
            }

        });

        System.out.println(this.getETD());
        ETDPanel.add(InputHrs);
        ETDPanel.add(InputMins);
        ETDFrame.add(ETDPanel);
        ETDFrame.add(confirmButton);

        try {
            methods.editPatientETON(p.getId(), ETD);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void Warning(String Problem){
        JFrame WarningFrame = new JFrame();
        WarningFrame.setSize(300,75);
        WarningFrame.setBackground(Color.WHITE);
        WarningFrame.setVisible(true);
        WarningFrame.setLocation(300,400);

        JLabel ReasonLabel = new JLabel("Error: "+Problem, SwingConstants.CENTER);
        JLabel WarningLabel = new JLabel("Please try again", SwingConstants.CENTER);

        JPanel WarningPanel = new JPanel();
        WarningPanel.add(ReasonLabel);
        WarningPanel.add(WarningLabel);

        WarningFrame.add(WarningPanel);
    }
}
