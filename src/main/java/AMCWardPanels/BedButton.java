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
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.JRadioButton;

public class BedButton extends JButton{
    private Bed bed;                // Bed from the Client package
    private GeneralWard methods;    // GeneralWard from Methods package, for communication with database
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
        this.setBounds(x, y, 80, 140);
        this.setOpaque(false);

        String colour = null;
        try {
            colour = methods.getBedColour(bed.getBedId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBorder(BorderFactory.createLineBorder(Color.decode(colour),4));

    }


    public int getBedButtonBedId(){                                 // returns bed id
        return bed.getBedId();
    }
    public LocalDateTime getETD(){return this.ETD;}                 // returns expected time of discharge for patient in the bed
    public void setETD(LocalDateTime time){this.ETD = time;}        // sets the ETD

    public String getBedButtonStatus(){                             // returns status of the bed: closed, open, free
        return bed.getStatus();
    }

    // makeEmpty removes the patient from the bed and sets it to 'free'
    public void makeEmpty(Patient p){
        try {
            methods.removePatient(p.getId(), bed.getBedId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setBorder(BorderFactory.createLineBorder(Color.decode("#2ECC71"),4));
        for( ActionListener al : this.getActionListeners() ) {
            this.removeActionListener( al );
        }
        this.addActionListener(evt-> {
            printInfoEmpty();
        });
    }

    // makeClosed closes the bed: this means a patient can't be added to the bed
    public void makeClosed(){
        try {
            methods.editBed(bed.getBedId(), "status", "'C'");
            methods.changeGreenBeds(-1);
            methods.changeBlackBeds(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bed.setStatus("C");
        this.setBorder(BorderFactory.createLineBorder(Color.decode("#00000"),4));
        for( ActionListener al : this.getActionListeners() ) {
            this.removeActionListener( al );
        }
        this.addActionListener(evt-> {
            printInfoClosed();
        });
    }
    // makeOpen opens a bed that was previously closed, and leaves it free.
    public void makeOpen(){
        try {
            methods.editBed(bed.getBedId(), "status", "'F'");
            methods.changeGreenBeds(1);
            methods.changeBlackBeds(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bed.setStatus("F");
        this.setBorder(BorderFactory.createLineBorder(Color.decode("#2ECC71"),4));
        for( ActionListener al : this.getActionListeners() ) {
            this.removeActionListener( al );
        }
        this.addActionListener(evt-> {
            printInfoEmpty();
        });
    }

    // these functions print the information of the bed when clicked in the Topography depending on the type of bed.
    // Beds #1, #2 and #3 (see description above) are all printed by printInfoFull, #4 is printed by printInfoClosed and
    // #5 by printInfoEmpty
    public void printInfoFull(){
        // get the information from the patient in the bed
        Patient p = new Patient();
        try {
            p = methods.getPatient(bed.getBedId());
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(450,260);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("<html><b>Bed ID: </b>"+bed.getBedId()+"</html>",SwingConstants.CENTER);
        bedIdLabel.setFont(bedIdLabel.getFont().deriveFont(15.0f));
        JLabel patientIdLabel = new JLabel("<html><b>Patient ID: </b>"+p.getPatientId()+"</html>", SwingConstants.CENTER);
        patientIdLabel.setFont(patientIdLabel.getFont().deriveFont(15.0f));
        JLabel ageLabel = new JLabel("<html><b>Date of Birth: </b>"+p.getDateOfBirth()+"</html>",SwingConstants.CENTER);
        JLabel genderLabel = new JLabel("<html><b>Gender: </b>"+p.getSex()+"</html>",SwingConstants.CENTER);
        JLabel srLabel = new JLabel("<html><b>Sideroom: </b>"+bed.getHasSideRoom()+"</html>", SwingConstants.CENTER);
        JLabel diaLabel = new JLabel("<html><b>Diagnosis: </b>"+p.getInitialDiagnosis()+"</html>", SwingConstants.CENTER);
        JLabel nextDestLabel = new JLabel("<html><b>Next Destination:   </b> N/A </html>");
        // in case it can't reach the database, nextDestLabel will have N/A
        //filling the next destination with the database info
        try {
            nextDestLabel = new JLabel("<html><b>Next Destination:   </b>"+methods.getWardName(p.getNextDestination())+"</html>", SwingConstants.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // fill the ETD label also with info from the database
        JLabel ETDLabel;
        if(p.getEstimatedTimeOfNext().isEqual(p.getArrivalDateTime())){
            ETDLabel = new JLabel("<html><b>ETD: </b>N/A</html>", SwingConstants.CENTER);
        }
        else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String ETDTime = p.getEstimatedTimeOfNext().format(formatter);
            ETDLabel = new JLabel("<html><b>ETD: </b>"+ETDTime+"</html>", SwingConstants.CENTER);
        }

        Patient finalP = p;

        // button to delete patient and empty the bed
        JButton deleteButton = new JButton("Remove Patient");
        deleteButton.addActionListener(evt -> {
            makeEmpty(finalP);
            infoFrame.dispose();
        });

        // button to edit the date of birth of the patient
        JButton editAge = new JButton("Edit");
        Patient finalP1 = p;
        editAge.addActionListener(evt -> {
            editAge(finalP1); //calls a popup to enter the new date of birth - see line 548
            infoFrame.dispose();
        });

        //button to edit the gender of the patient
        JButton editGender = new JButton("Edit");
        editGender.addActionListener(evt -> {
            editParameter("Gender", finalP1); //calls a popup to edit the gender - see line 624
            infoFrame.dispose();
        });

        //button to edit the diagnosis
        JButton editDia = new JButton("Edit");
        editDia.addActionListener(evt -> {
            editDiagnosis(finalP1); // calls a popup to edit the diagnosis - see line 702
            infoFrame.dispose();
        });

        //button to edit the ETD in Hours & minutes from current time
        JButton editETD = new JButton("Edit");
        editETD.addActionListener(evt -> {
            editETD(finalP1); // calls a popup to edit ETD - see line 461
            infoFrame.dispose();
        });

        //button to eidt the next destination
        JButton changeNextDest = new JButton("Edit");
        changeNextDest.addActionListener(evt -> {
            selectWard(finalP); // calls a popup to select next ward - see line 405
            infoFrame.dispose();
        });

        // add the labels to the frame using gridbaglayout
        // having information on the LHS and edit buttons in RHS
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c); // add bed ID label
        c.gridx = 1;
        infoPanel.add(patientIdLabel, c); // add patient ID label

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(ageLabel, c); // add age label
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editAge, c); // add edit age button

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(genderLabel, c); // add gender label
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editGender, c); // add edit gender button

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(srLabel, c); // add sideroom label (can only be editted if bed is free, so not in this case)

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 4;
        infoPanel.add(diaLabel, c); // add diagnosis label
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editDia, c); // add diagnosis edit button

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 5;
        infoPanel.add(ETDLabel, c); // add ETD label
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(editETD, c); // add ETD edit button

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 6;
        infoPanel.add(nextDestLabel, c); // add next destination label
        c.gridwidth = 1;
        c.gridx = 2;
        infoPanel.add(changeNextDest, c); // add next destination edit button

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 8;
        infoPanel.add(deleteButton, c); // add delete button


        infoFrame.add(infoPanel); // add the panel to the frame

    }

    // in this case we can only edit the gender and of the bed, and the only parameters on display are the gender and
    // the sideroom. we can also close the bed
    public void printInfoEmpty(){
        JFrame infoFrame = new JFrame(); // set up frame

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("<html><b>Bed ID: </b>"+bed.getBedId()+"</html>",SwingConstants.CENTER);
        JLabel srLabel = new JLabel("<html><b>Sideroom: </b>"+bed.getHasSideRoom()+"</html>", SwingConstants.CENTER);
        JLabel sexLabel = new JLabel("<html><b>Gender: </b>"+bed.getForSex()+"</html>", SwingConstants.CENTER);

        JButton editGender = new JButton("Edit"); //button to edit bed gender
        editGender.addActionListener(evt -> {
            this.editBedGender(); //see line 747
            infoFrame.dispose();
        });

        JButton closeButton = new JButton("Close Bed"); // button to close bed
        closeButton.addActionListener(evt -> {
            this.makeClosed(); // see line 91
            infoFrame.dispose();
        });

        // add the labels to the panel, and then to the frame
        // once again we have the info on the LHS and the button to edit it on the RHS. The Close bed is at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c); // add bed ID label
        c.ipady = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(srLabel, c); // add sideroom label
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(sexLabel, c); // add bed gender label
        c.ipady = 1;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(editGender, c); // add edit gender button
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(closeButton, c); // add close button

        infoFrame.add(infoPanel);

    }

    // in this case the same information as in free beds is displayed, and instead of closing the bed, we can open it
    public void printInfoClosed(){
        // setup the frame
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
            this.editBedGender(); //see line 747
            infoFrame.dispose();
        });

        JButton openButton = new JButton("Open Bed");
        openButton.addActionListener(evt -> {
            this.makeOpen(); //see line 109
            infoFrame.dispose();
        });

        // add the labels to the panel, and then to the frame
        // same structure as with the empty beds: info on the LHS, edits on the RHS, open bed at the bottom
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.ipady = 10; // so there's some space between the bedID and the rest
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c); // bed ID info
        c.ipady = 1;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(srLabel, c); // Sideroom info
        c.ipady = 1;
        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(sexLabel, c); // bed gender info
        c.ipady = 1;
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(editGender, c); // edit gender button
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(openButton, c); // open bed button

        infoFrame.add(infoPanel);
    }
    // end of the print-information functions

    // selectWard is used to transfer or discharge patients from the current ward. It can be done by simply clicking on
    // the 'Make Transfer Request' button (after printing its information) or by expressly editing the 'Next Destination'
    private void selectWard(Patient p){
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        ArrayList<Ward> wards = methods.getAllTransWards(); // this gets all the possible wards a patient can transfer to

        infoFrame.setLayout(new GridLayout(wards.size()+2,1));
        ButtonGroup longStayWards = new ButtonGroup(); // basically an A-B-C-D kind of question where a ward is chosen
        for(Ward w:wards){
            JRadioButton lsWard = new JRadioButton(w.getWardName());
            lsWard.setActionCommand(w.getWardName());
            lsWard.setFont(new Font("Verdana", Font.PLAIN, 20));
            longStayWards.add(lsWard);
            infoFrame.add(lsWard);
        }
        JButton submitWard = new JButton("Submit"); // button to submit next ward
        infoFrame.add(submitWard);
        submitWard.addActionListener(evt -> {
            String selected = longStayWards.getSelection().getActionCommand(); // get the selected option from longStayWards
            methods.transferPatient(p.getId(), selected); // communicate the next ward for this patient to the database
            infoFrame.dispose();
        });
    }

    // this function prints different warnings, specified by the String Problem. This function is run when the user
    // inputs a value that is out of bounds (eg. an ETD with minutes>60), or other issues that may require the user to
    // input the information again
    public void Warning(String Problem){
        // set up the frame
        JFrame WarningFrame = new JFrame();
        WarningFrame.setSize(300,100);
        // set the location so it's just above the previous popup (where the wrong info was inputted)
        // in the middle horizontally but not vertically
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - WarningFrame.getWidth()) / 2); // get the middle of the screen
        WarningFrame.setLocation(x, 150); // set it up at 150 which we know will be above the other popup
        WarningFrame.setBackground(Color.WHITE);
        WarningFrame.setVisible(true);

        JLabel ReasonLabel = new JLabel("Warning: "+Problem, SwingConstants.CENTER); //this helps the user know what's wrong
        JLabel WarningLabel = new JLabel("Please try again", SwingConstants.CENTER);

        JPanel WarningPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        WarningPanel.add(ReasonLabel, c); // add what the problem is
        c.gridy = 1;
        WarningPanel.add(WarningLabel,c); // add warning label

        WarningFrame.add(WarningPanel);
    }

    // editETD gets user input to edit the ETD of a patient. This can be done by editing 'ETD' after printing patient
    // information, or within the 'selectWard' function
    private void editETD(Patient p){
        // set up the frame
        JFrame editFrame = new JFrame("Edit ETD");
        editFrame.setSize(500, 200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
        editFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel editLabel = new JLabel("Time from now:");
        JTextField hours = new JTextField("",5); // how many hours in the future
        JTextField minutes = new JTextField("",5); // how many minutes in the future

        JLabel one = new JLabel(":"); // this goes between hours : minutes

        JButton confirmButton = new JButton("Confirm"); // confirm button
        confirmButton.addActionListener(evt -> {
            System.out.println(minutes.getText());
            //check that minutes is <60
            if (Integer.parseInt(minutes.getText()) > 59) { Warning("Minutes must be less than 59"); }
            //check that hours is between 00 and 23
            if (Integer.parseInt(hours.getText()) > 23) { Warning("Hours must be less than 24"); }

            // update local Bed information
            int Minutes = Integer.parseInt(minutes.getText());
            int Hours = Integer.parseInt(hours.getText());
            this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours));
            editFrame.dispose(); //close popup

            // update information on database
            try {
                methods.editPatient(p.getId(), "estimatedatetimeofnext", "'"+ETD+"'");
                methods.changeRedBeds(-1);
                methods.changeOrangeBeds(1);
                this.setBorder(BorderFactory.createLineBorder(Color.decode("#F89820"),4));
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            editFrame.dispose();
            printInfoFull(); //reprint the updated information
        });

        // add labels, textfields and confimation button to a panel and then to the frame
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridy = 0;
        editPanel.add(editLabel); // info label
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        editPanel.add(hours, c); // hours textfield
        c.gridx = 1;
        c.gridy = 1;
        editPanel.add(one, c); // the : that separates hours : minutes
        c.gridx = 2;
        c.gridy = 1;
        editPanel.add(minutes, c); // minutes textfield
        c.gridwidth = 3;
        c.gridy = 2;
        editPanel.add(confirmButton, c); //confirmation button

        editFrame.add(editPanel);
    }

    // edits the age of the patient
    private void editAge(Patient p) {
        // setup the frame
        JFrame editFrame = new JFrame("Edit Age");
        editFrame.setSize(500, 200);
        editFrame.setLayout(new GridLayout(2,1));
        editFrame.setBackground(Color.WHITE);
        editFrame.setLocationRelativeTo(null);
        editFrame.setVisible(true);
        editFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel DateLabel = new JLabel("Date of Birth:"); // description of the information
        JTextField day = new JTextField("",5); // text field for day
        JTextField month = new JTextField("",5); // text field for month
        JTextField year = new JTextField("",5); // text field for year

        // these jlabels are so it looks like day / month / year
        // for example, 08 / 01 / 1942
        JLabel one = new JLabel("/"); // separates day and month
        JLabel two = new JLabel("/"); // separates month and year

        // this has the dateLabel in one line, the input fields and '/' in another, and the confirmation in the last one
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 3;
        c.gridy = 0;
        c.ipady = 20;
        editPanel.add(DateLabel); // add info label
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        editPanel.add(day); // add day text field
        c.gridx = 1;
        editPanel.add(one); // add the first '/'
        c.gridx = 2;
        editPanel.add(month); // add month text field
        c.gridx = 3;
        editPanel.add(two); // add the second '/'
        c.gridx = 4;
        editPanel.add(year); // add the year textfield

        JButton ConfirmButton = new JButton("Confirm");
        ConfirmButton.addActionListener(evt -> {

            // warning if year is in the future
            if(Integer.parseInt(year.getText()) > LocalDateTime.now().getYear()){Warning("Year is in the future");}
            // warning if month > 12
            if(Integer.parseInt(month.getText()) > 12){Warning("That is not a valid month");}
            // warning if day > 31
            if(Integer.parseInt(day.getText()) > 31){Warning("That is not a valid day");}

            editFrame.dispose();
            // update information of DoB
            try {
                LocalDate DOB = LocalDate.of(  Integer.parseInt(year.getText()),  Integer.parseInt(month.getText()),  Integer.parseInt(day.getText()) );
                methods.editPatient(p.getId(), "dateofbirth", "'"+DOB+"'");

            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }

            printInfoFull(); //reprint the updated information
        });

        // add confirm button to another panel
        editPanel.add(ConfirmButton, c);
        JPanel confirmPanel = new JPanel();
        confirmPanel.add(ConfirmButton);

        // add both panels to the frame
        editFrame.add(editPanel);
        editFrame.add(confirmPanel);

    }

    // editParameter can edit several different variables that have similar frame requirements. it  is used for sideroom
    // and gender. which of these must be changed is chosen with Sring par.
    // during the day to day running of the hospitals, the parameter 'sideroom' isn't editted very often, as it is quite
    // permanent. However, we have decided to keep the option to change it within the code in case at some point this
    // changes. For example, the sideroom is unusable for some reason.
    private void editParameter( String par , Patient p){
        // setup the frame
        JFrame editFrame = new JFrame("Edit");
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        // the confirmbutton
        JButton ConfirmButton = new JButton();

        // dropdown menu to select from options
        JComboBox<String> pSR = new JComboBox<>();

        // case where sideroom is editted: there are two options : sideroom or no sideroom
        if(par.equals("Sideroom")){
            // creating dropdown with options
            String[] sideRoom = { "No Sideroom","Sideroom"};
            pSR = new JComboBox<String>(sideRoom); // setup the menu to have the 2 options

            ConfirmButton = new JButton("Confirm"); //confirm button
            JComboBox<String> finalPSR1 = pSR;
            ConfirmButton.addActionListener(evt -> {
                //update info of the database
                try {
                    // by default its false
                    String sRoom = "false";
                    boolean sroo = false;

                    // if input is true, change to boolean true
                    if (Objects.equals(finalPSR1.getSelectedItem(), "Sideroom")) { // if we do need a sideroom
                        sRoom = "true";
                        sroo = true;}
                    final String SR = sRoom; // this is what has been chosen from menu
                    boolean finalSroo = sroo; // convert into boolean

                    //update database
                    methods.editPatient(p.getId(), "needssideroom", SR); // update patient
                    methods.editBed(bed.getBedId(), "hassideroom", SR);  // update bed
                    bed.setSR(finalSroo);
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                editFrame.dispose();
                printInfoFull(); //reprint updated information
            });
        }

        // case if gender is editted
        else if(par.equals("Gender")){
            // dropdown menu will now have male / female
            String[] genders = { "Male","Female"};
            pSR = new JComboBox<String>(genders);

            ConfirmButton = new JButton("Confirm"); //confirm button
            JComboBox<String> finalPSR = pSR;
            ConfirmButton.addActionListener(evt -> {
                //update database information
                try {
                    methods.editPatient(p.getId(), "sex", "'" + finalPSR.getSelectedItem() + "'");
                } catch (IOException | SQLException e) {
                    e.printStackTrace();
                }
                editFrame.dispose();
                printInfoFull(); //reprint with updated information
            });
        }

        // add everything to editPanel, which is added to editFrame
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        editPanel.add(pSR); // add dropdown menu
        c.gridx = 1;
        editPanel.add(ConfirmButton, c); // add confirm btton

        editFrame.add(editPanel);
    }

    // edits the diagnosis of the patient
    private void editDiagnosis(Patient p){
        // setup the frame
        JFrame editFrame = new JFrame("Edit Diagnosis");
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        // in the future we may decide to add more than one word for diagnosis, but for now:
        JLabel message = new JLabel("Please enter only one word");

        JTextField newp = new JTextField(p.getInitialDiagnosis()); // the field where the information is inputted.

        JButton ConfirmButton = new JButton("Confirm"); // confirm button
        ConfirmButton.addActionListener(evt -> {
            // update database
            try {
                methods.editPatient(p.getId(), "initialdiagnosis", "'" + newp.getText() + "'");
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            editFrame.dispose();
            printInfoFull(); //reprint updated information
        });


        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.gridx = 0;
        c.gridwidth = 2;
        editPanel.add(message, c); // add message to enter one word
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 1;
        editPanel.add(newp, c); // add input field
        c.gridx = 1;
        editPanel.add(ConfirmButton,c); // add confirm button
        editFrame.add(editPanel);

    }

    // editBed is used to change the parameters of empty beds: gender or sideroom. the bed parameters can only be
    // changed if it is empty or closed
    private void editBedGender(){
        // setup frame
        JFrame editFrame = new JFrame("Edit");
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        JButton ConfirmButton;
        JComboBox<String> pSR;

        // options for bed gender
        String[] genders = { "Male","Female", "Uni"}; // uni means it is unspecified, ie. it can be either
        pSR = new JComboBox<String>(genders); // add options to dropdown menu

        ConfirmButton = new JButton("Confirm"); // confirmation button
        JComboBox<String> finalPSR = pSR;
        ConfirmButton.addActionListener(evt -> {
            // update database information
            try {
                methods.editBed(bed.getBedId(), "forsex", "'" + finalPSR.getSelectedItem() + "'");
                bed.setSex((String) finalPSR.getSelectedItem());
            } catch (IOException e) {
                e.printStackTrace();
            }
            editFrame.dispose();
            printInfoEmpty(); //reprint updated information
        });

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 0;
        editPanel.add(pSR); //add dropdown menu
        c.gridx = 1;
        editPanel.add(ConfirmButton, c); // add confirmation button

        editFrame.add(editPanel);
    }

}
