package AMCWardPanels;

/*
This is the class that makes all the beds in the AMCs and the Long Stay Wards. There are 5 kinds of beds, with 5 distinct colours:
1. Occupied beds without an ETD: these are RED, and can edit all the information about the patient on it, as well as the bed
   characteristics (sideroom, gender).
2. Occupied beds with an ETD: these are AMBAR, and they have the same functionalities as Bed#1 with the difference that it
   is known when the patient will be transferred/discharged from the bed
3. Occupied beds with an ETD that has passed: these are BLUE, and they are exactly like Bed#2 except for the fact that their
   ETD has passed. ie, the bed show now be empty. The application awaits for staff confirmation to free the bed
4. Closed beds: these are BLACK, and a patient can't be assigned to them. This is done when there aren't enough nurses to
   cover all the beds, or there is something wrong with the bed itself. They can be opened, or the bed characteristics can
   be editted
5. Free Beds: these are GREEN, and no patient is on them but one can be assigned through the 'Incoming Patients' List. The
   bed characteristics can also be altered, or the bed can be closed.

Each of these beds are different examples of BedButtons, which are contained in Topography. This class comunicates with the
database to get and edit patient information.
 */

import Client.*;
import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.JRadioButton;

public class BedButton extends JButton{
    private Bed bed;
    private GeneralWard methods;
    private LocalDateTime ETD;
    Topography top;

    //constructor; when instantiating a bed its location must be specified with x and y.
    public BedButton(Topography top, GeneralWard methods, Bed bed, Integer x, Integer y) {
        this.bed = bed;
        this.methods = methods;
        this.top = top;

        //Makes bed pretty
        this.setText(String.valueOf(bed.getBedId()));
        this.setFont(new Font("Verdana", Font.PLAIN, 30));
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);

        String colour = null;
        try {
            colour = methods.getBedColour(bed.getBedId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBackground(Color.decode(colour));

    }

    public int getBedButtonBedId(){
        return bed.getBedId();
    }

    // functions that return bed information
    public LocalDateTime getETD(){return this.ETD;}
    public void setETD(LocalDateTime time){this.ETD = time;}

    public String getBedButtonStatus(){
        return bed.getStatus();
    }

    //Used when patients are moved into/from a bed, or a bed must be closed
    public void makeEmpty(Patient p){
        try {
            methods.removePatient(p.getId(), bed.getBedId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.setBackground(Color.decode("#2ECC71"));
    }
    public void makeClosed(){
        try {
            methods.editBed(bed.getBedId(), "status", "'C'");
            methods.changeGreenBeds(-1);
            methods.changeBlackBeds(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bed.setStatus("C");
        this.setBackground(Color.BLACK);
    }
    public void makeOpen(){
        try {
            methods.editBed(bed.getBedId(), "status", "'F'");
            methods.changeGreenBeds(1);
            methods.changeBlackBeds(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bed.setStatus("F");
        this.setBackground(Color.decode("#2ECC71"));
    }

    // these functions print the information of the bed when clicked in the Topography depending on the type of bed.
    // Beds #1, #2 and #3 are all printed by printInfoFull, #4 is printed by printInfoClosed and #5 by printInfoEmpty
    public void printInfoFull(){
        Patient p = new Patient();
        try {
            p = methods.getPatient(bed.getBedId());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(450,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("<html><b>Bed ID: </b>"+bed.getBedId()+"</html>",SwingConstants.CENTER);
        bedIdLabel.setFont(bedIdLabel.getFont().deriveFont(15.0f));
        JLabel patientIdLabel = new JLabel("<html><b>Patient ID: </b>"+p.getId()+"</html>", SwingConstants.CENTER);
        patientIdLabel.setFont(patientIdLabel.getFont().deriveFont(15.0f));
        JLabel ageLabel = new JLabel("<html><b>Date of Birth: </b>"+p.getDateOfBirth()+"</html>",SwingConstants.CENTER);
        JLabel genderLabel = new JLabel("<html><b>Gender: </b>"+p.getSex()+"</html>",SwingConstants.CENTER);
        JLabel srLabel = new JLabel("<html><b>Sideroom: </b>"+bed.getHasSideRoom()+"</html>", SwingConstants.CENTER);
        JLabel diaLabel = new JLabel("<html><b>Diagnosis: </b>"+p.getInitialDiagnosis()+"</html>", SwingConstants.CENTER);
        JLabel nextDestLabel = null;
        try {
            nextDestLabel = new JLabel("<html><b>Next Destination:   </b>"+methods.getWardName(p.getNextDestination())+"</html>", SwingConstants.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel ETDLabel;
        if(p.getEstimatedTimeOfNext().isEqual(p.getArrivalDateTime())){
            ETDLabel = new JLabel("ETD: N/A", SwingConstants.CENTER);
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String ETDTime = p.getEstimatedTimeOfNext().format(formatter);
            ETDLabel = new JLabel("ETD: "+ETDTime, SwingConstants.CENTER);
        }

        Patient finalP = p;

        // delete patient, making the bed empty
        JButton deleteButton = new JButton("Remove Patient");
        deleteButton.addActionListener(evt -> {
            makeEmpty(finalP);
            infoFrame.dispose();
        });

        JButton selectWardButton = new JButton("Make Transfer Request");
        selectWardButton.addActionListener(evt -> {
            selectWard(finalP);
            infoFrame.dispose();
        });

        JButton editAge = new JButton("Edit");
        Patient finalP1 = p;
        editAge.addActionListener(evt -> {
            editAge(finalP1);
            infoFrame.dispose();
        });

        JButton editGender = new JButton("Edit");
        editGender.addActionListener(evt -> {
            editParameter("Gender", finalP1);
            infoFrame.dispose();
        });

        JButton editSR = new JButton("Edit");
        editSR.addActionListener(evt -> {
            editParameter("Sideroom", finalP1);
            infoFrame.dispose();
        });

        JButton editDia = new JButton("Edit");
        editDia.addActionListener(evt -> {
            editDiagnosis(finalP1);
            infoFrame.dispose();
        });

        JButton editETD = new JButton("Edit");
        editETD.addActionListener(evt -> {
            editETD(finalP1);
            infoFrame.dispose();
        });

        JButton changeNextDest = new JButton("Edit");
        changeNextDest.addActionListener(evt -> {
            selectWard(finalP);
            infoFrame.dispose();
        });

        // add the labels to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c);
        c.gridx = 1;
        infoPanel.add(patientIdLabel, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(ageLabel, c);
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editAge, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(genderLabel, c);
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editGender, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(srLabel, c);
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editSR, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        infoPanel.add(diaLabel, c);
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editDia, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 5;
        infoPanel.add(ETDLabel, c);
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editETD, c);

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 6;
        infoPanel.add(nextDestLabel, c);
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(changeNextDest, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 7;
        infoPanel.add(selectWardButton, c);

        c.gridy = 8;
        infoPanel.add(deleteButton, c);


        infoFrame.add(infoPanel);

    }

    public void printInfoEmpty(){
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("<html><b>Bed ID: </b>"+bed.getBedId()+"</html>",SwingConstants.CENTER);
        JLabel srLabel = new JLabel("<html><b>Sideroom: </b>"+bed.getHasSideRoom()+"</html>", SwingConstants.CENTER);
        JLabel sexLabel = new JLabel("<html><b>Gender: </b>"+bed.getForSex()+"</html>", SwingConstants.CENTER);

        JButton editGender = new JButton("Edit");
        editGender.addActionListener(evt -> {
            this.editBed("Gender");
            infoFrame.dispose();
        });

        JButton editSR = new JButton("Edit");
        editSR.addActionListener(evt -> {
            this.editBed("Sideroom");

            infoFrame.dispose();
        });

        JButton closeButton = new JButton("Close Bed");
        closeButton.addActionListener(evt -> {
            this.makeClosed();
            infoFrame.dispose();
        });

        // add the labels to the panel, and then to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c);
        c.ipady = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(srLabel, c);
        c.ipady = 1;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(editSR, c);
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(sexLabel, c);
        c.ipady = 1;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(editGender, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(closeButton, c);

        infoFrame.add(infoPanel);

    }

    public void printInfoClosed(){
        JFrame infoFrame = new JFrame();
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("<html><b>Bed ID: </b>"+bed.getBedId()+"</html>",SwingConstants.CENTER);
        JLabel srLabel = new JLabel("<html><b>Sideroom: </b>"+bed.getHasSideRoom()+"</html>", SwingConstants.CENTER);
        JLabel sexLabel = new JLabel("<html><b>Gender: </b>"+bed.getForSex()+"</html>", SwingConstants.CENTER);

        JButton editGender = new JButton("Edit");
        editGender.addActionListener(evt -> {
            this.editBed("Gender");
            infoFrame.dispose();
        });

        JButton editSR = new JButton("Edit");
        editSR.addActionListener(evt -> {
            this.editBed("Sideroom");

            infoFrame.dispose();
        });

        JButton openButton = new JButton("Open Bed");
        openButton.addActionListener(evt -> {
            this.makeOpen();
            infoFrame.dispose();
        });

        // add the labels to the panel, and then to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c);
        c.ipady = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(srLabel, c);
        c.ipady = 1;
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(editSR, c);
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(sexLabel, c);
        c.ipady = 1;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(editGender, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(openButton, c);

        infoFrame.add(infoPanel);
    }

    // end of the print-information functions

    // selectWard is used to transfer or discharge patients from the current ward. It can be done by simply clicking on
    // the 'Make Transfer Request' button (after printing its information) or by expressly editing the 'Next Destination'
    private void selectWard(Patient p){
        Client client = new Client();
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        ArrayList<Ward> wards = methods.getAllTransWards();

        infoFrame.setLayout(new GridLayout(wards.size()+2,1));
        ButtonGroup longstayWards = new ButtonGroup();
        for(Ward w:wards){
            JRadioButton lsWard = new JRadioButton(w.getWardName());
            lsWard.setActionCommand(w.getWardName());
            lsWard.setFont(new Font("Verdana", Font.PLAIN, 20));
            longstayWards.add(lsWard);
            infoFrame.add(lsWard);
        }
        JButton setETDButton = new JButton("Select time from now");
        setETDButton.addActionListener(evt -> {
            editETD(p);
        });
        infoFrame.add(setETDButton);
        JButton submitWard = new JButton("Submit");
        infoFrame.add(submitWard);
        submitWard.addActionListener(evt -> {
            String selected = longstayWards.getSelection().getActionCommand();
            methods.transferPatient(p.getId(), selected);
            infoFrame.dispose();
        });
    }

    // these are different warnings, specified by the String Problem. This function is run when the user inputs a value that
    // is out of bounds (eg. an ETD with minutes>60), or other issues that may require the user to input the information again
    public void Warning(String Problem){
        JFrame WarningFrame = new JFrame();
        WarningFrame.setSize(300,75);
        WarningFrame.setBackground(Color.WHITE);
        WarningFrame.setVisible(true);
        WarningFrame.setLocationRelativeTo(null);

        JLabel ReasonLabel = new JLabel("Error: "+Problem, SwingConstants.CENTER);
        JLabel WarningLabel = new JLabel("Please try again", SwingConstants.CENTER);

        JPanel WarningPanel = new JPanel();
        WarningPanel.add(ReasonLabel);
        WarningPanel.add(WarningLabel);

        WarningFrame.add(WarningPanel);
    }

    // editETD gets user input to edit the ETD of a patient. This can be done by editing 'ETD' after printing patient
    // information, or within the 'selectWard' function
    private void editETD(Patient p){
        JFrame editFrame = new JFrame("Edit ETD");
        editFrame.setSize(500, 200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
        editFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel editLabel = new JLabel("Time from now:");
        JTextField hours = new JTextField("",5);
        JTextField minutes = new JTextField("",5);

        JLabel one = new JLabel(":");

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(evt -> {

            //check that minutes is <60 and
            if (Integer.parseInt(minutes.getText()) > 59) { Warning("Minutes must be an integer less than 59"); }
            //check that hours is between 00 and 23
            if (Integer.parseInt(hours.getText()) > 23) { Warning("Hours must be an integer less than 24"); }

            // if both hours and minutes have been inputted, then set the new time on the same day but the new time
            if (!hours.getText().equals("ETD Hours") && !minutes.getText().equals("ETD Minutes")) {
                int Minutes = Integer.parseInt(minutes.getText());
                int Hours = Integer.parseInt(hours.getText());
                this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours));
                editFrame.dispose(); //close popup
            }
            // if only hours are inputted, change to the new hour o'clock
            if (!hours.getText().equals("ETD Hours") && minutes.getText().equals("ETD Minutes")) {
                int Minutes = 0;
                int Hours = Integer.parseInt(hours.getText());
                this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours));
                editFrame.dispose(); //close popup
            }
            // if only minutes are inputted, change ETD to the new minutes in the current hour
            if (hours.getText().equals("ETD Hours") && !minutes.getText().equals("ETD Minutes")) {
                int Minutes = Integer.parseInt(minutes.getText());
                int Hours = LocalDateTime.now().getHour();

                // if the new ETD is in the past
                if(Minutes < LocalDateTime.now().getMinute()) {
                    this.Warning("Time is in the past");
                }

                else{ this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours)); }
            }
            try {
                System.out.println(ETD);
                methods.editPatient(p.getId(), "estimatedatetimeofnext", "'"+ETD+"'");
                methods.changeRedBeds(-1);
                methods.changeOrangeBeds(1);
                this.setBackground(Color.decode("#F89820"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridy = 0;
        editPanel.add(editLabel);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        editPanel.add(hours, c);
        c.gridx = 1;
        c.gridy = 1;
        editPanel.add(one, c);
        c.gridx = 2;
        c.gridy = 1;
        editPanel.add(minutes, c);
        c.gridwidth = 3;
        c.gridy = 2;
        editPanel.add(confirmButton, c);

        editFrame.add(editPanel);
    }

    // edits the age of the patient
    private void editAge(Patient p) {
        JFrame editFrame = new JFrame("Edit Age");
        editFrame.setSize(500, 200);
        editFrame.setLayout(new GridLayout(2,1));
        editFrame.setBackground(Color.WHITE);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
        editFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel DateLabel = new JLabel("Date of Birth:");
        JTextField day = new JTextField("",5);
        JTextField month = new JTextField("",5);
        JTextField year = new JTextField("",5);

        JLabel one = new JLabel("/");
        JLabel two = new JLabel("/");

        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridy = 0;
        editPanel.add(DateLabel);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        editPanel.add(day);
        c.gridx = 1;
        c.gridy = 1;
        editPanel.add(one);
        c.gridx = 2;
        c.gridy = 1;
        editPanel.add(month);
        c.gridx = 3;
        c.gridy = 1;
        editPanel.add(two);
        c.gridx = 4;
        c.gridy = 1;
        editPanel.add(year);

        JButton ConfirmButton = new JButton("Confirm");
        ConfirmButton.addActionListener(evt -> {
            editFrame.dispose();
            try {
                //Change date of birth
                LocalDate DOB = LocalDate.of(  Integer.parseInt(year.getText()),  Integer.parseInt(month.getText()),  Integer.parseInt(day.getText()) );
                methods.editPatient(p.getId(), "dateofbirth", "'"+DOB+"'");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        JPanel confirmPanel = new JPanel();
        confirmPanel.add(ConfirmButton);

        editFrame.add(editPanel);
        editFrame.add(confirmPanel);


    }

    // editParameter can edit several different variables that have similar frame requirements. it  is used for sideroom,
    // sex and diagnosis. which of these must be changed is chosen with Sring par.
    private void editParameter( String par , Patient p){
        JFrame editFrame = new JFrame("Edit");
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        JButton ConfirmButton = new JButton();

        JComboBox<String> pSR = new JComboBox<>();

        // case where sideroom is editted
        if(par.equals("Sideroom")){
            // creating dropdown with options
            String[] sideRoom = { "No Sideroom","Sideroom"};
            pSR = new JComboBox<String>(sideRoom);
            JPanel panelSR = new JPanel();
            panelSR.add(pSR);
            String sRoom = "false";
            boolean sroo = false;
            if (pSR.getSelectedItem() == "Sideroom") {
                sRoom = "true";
                sroo = true;}
            final String SR = sRoom;
            ConfirmButton = new JButton("Confirm"); //when confirmed, update information of database
            boolean finalSroo = sroo;
            ConfirmButton.addActionListener(evt -> {
                try {
                    methods.editPatient(p.getId(), "needssideroom", SR);
                    methods.editBed(bed.getBedId(), "hassideroom", SR);
                    bed.setSR(finalSroo);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                editFrame.dispose();
                printInfoFull();
            });
        }

        // case if gender is editted
        else if(par.equals("Gender")){
            String[] genders = { "Male","Female"};
            pSR = new JComboBox<String>(genders);
            JPanel panelSR = new JPanel();
            panelSR.add(pSR);

            ConfirmButton = new JButton("Confirm"); //when confirmed, update information of database
            JComboBox<String> finalPSR = pSR;
            ConfirmButton.addActionListener(evt -> {
                try {
                    methods.editPatient(p.getId(), "sex", "'" + finalPSR.getSelectedItem() + "'");
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                editFrame.dispose();
                printInfoFull();
            });
        }

        // add everything to editPanel, which is added to editFrame
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        editPanel.add(pSR);
        c.gridx = 1;
        editPanel.add(ConfirmButton, c);

        editFrame.add(editPanel);
    }

    // edits the diagnosis of the patient
    private void editDiagnosis(Patient p){
        JFrame editFrame = new JFrame("Edit Diagnosis");
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        JTextField newp = new JTextField(p.getInitialDiagnosis()); // the field where the information is inputted.

        JButton ConfirmButton = new JButton("Confirm"); // updates the database with the new information
        ConfirmButton.addActionListener(evt -> {
            try {
                methods.editPatient(p.getId(), "initialdiagnosis", "'" + newp.getText() + "'");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            editFrame.dispose();
            printInfoFull();
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        editPanel.add(newp);
        c.gridy = 1;
        editPanel.add(ConfirmButton);
        editFrame.add(editPanel);

    }
    // editBed is used to change the parameters of empty beds: gender or sideroom. the bed parameters can only be
    // changed if it is empty or closed
    private void editBed(String par){
        JFrame editFrame = new JFrame("Edit");
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        JButton ConfirmButton = new JButton();

        JComboBox<String> pSR = new JComboBox<>();
        // case where sideroom is the parameter to edit
        if(par.equals("Sideroom")){
            String[] sideRoom = { "No Sideroom","Sideroom"};
            pSR = new JComboBox<String>(sideRoom);
            JPanel panelSR = new JPanel();
            panelSR.add(pSR);
            String sRoom = "false";
            boolean sroo = false;
            if (pSR.getSelectedItem() == "Sideroom") {
                sRoom = "true";
            sroo = true;}
            final String SR = sRoom;
            ConfirmButton = new JButton("Confirm");
            boolean finalSroo = sroo;
            ConfirmButton.addActionListener(evt -> {
                try {
                    methods.editBed(bed.getBedId(), "hassideroom", SR);
                    bed.setSR(finalSroo);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editFrame.dispose();
                printInfoEmpty();
            });
        }

        // case where Gender is the parameter to edit
        else if(par.equals("Gender")){
            String[] genders = { "Male","Female", "Uni"}; // uni means it is unspecified, ie. it can be either
            pSR = new JComboBox<String>(genders);
            JPanel panelSR = new JPanel();
            panelSR.add(pSR);

            ConfirmButton = new JButton("Confirm");
            JComboBox<String> finalPSR = pSR;
            ConfirmButton.addActionListener(evt -> {
                try {
                    methods.editBed(bed.getBedId(), "forsex", "'" + finalPSR.getSelectedItem() + "'");
                    bed.setSex((String) finalPSR.getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                editFrame.dispose();
                printInfoEmpty();
            });
        }

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        editPanel.add(pSR);
        c.gridx = 1;
        editPanel.add(ConfirmButton, c);

        editFrame.add(editPanel);
    }

}
