package AMCWardPanels;

import Client.*;
import Methods.GeneralWard;

import javax.security.auth.callback.ConfirmationCallback;
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
    private int BedId;   // kept as string in case e decide to name them with characters too
    private String status;    // free (f), if it is occupied (o) or closed (c)
    private String sex;    // when empty, gender = x
    private Boolean sideroom;
    private GeneralWard methods;
    private LocalDateTime ETD;
    Topography top;
    //constructor; when instantiating a bed its location must be specified with x and y.
    public BedButton(Topography top, GeneralWard methods, int BedId, String status, String sex, Boolean sideroom, Integer x, Integer y) {
        this.BedId = BedId;
        this.status = status; //f = free, o = occupied, c = closed
        this.sideroom = sideroom;
        this.sex = sex;
        this.methods = methods;

        this.top = top;

        this.setText(String.valueOf(BedId));
        this.setFont(new Font("Verdana", Font.PLAIN, 30));
        this.setBounds(x, y, 70, 140);
        this.setOpaque(true);
        if(status.equals("O")){
            this.setBackground(Color.decode("#E74C3C"));
            try {
                Patient p = methods.getPatient(BedId);
                this.ETD = p.getEstimatedTimeOfNext();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if(status.equals("F")){this.setBackground(Color.decode("#2ECC71")); }
        if(status.equals("C")){this.setBackground(Color.BLACK); }
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
        //TODO setBed
        this.status = "O";
        this.setBackground(Color.decode("#E74C3C"));
        this.repaint();
        refreshTopography();
    }
    public void makeEmpty(Patient p){
        try {
            methods.removePatient(p.getId(), BedId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.setStatus("F");
        this.setBackground(Color.decode("#2ECC71"));
        refreshTopography();
    }
    public void makeClosed(){
        try {
            methods.editBed(BedId, "status", "'C'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.setStatus("C");
        this.setBackground(Color.BLACK);
        refreshTopography();
    }

    public void makeOpen(){
        try {
            methods.editBed(BedId, "status", "'F'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        this.setStatus("F");
        this.setBackground(Color.decode("#2ECC71"));
        refreshTopography();
    }

    public void refreshTopography(){
        top.refresh();
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
        infoFrame.setSize(450,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        // labels with the bed information using methods from class Bed
        JLabel bedIdLabel = new JLabel("Bed ID: "+this.getID(),SwingConstants.CENTER);
        JLabel ageLabel = new JLabel("Date of Birth: "+p.getDateOfBirth(),SwingConstants.CENTER);
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
        deleteButton.setBounds(400, 520, 100, 20);
        deleteButton.addActionListener(evt -> {
            makeEmpty(finalP);
            infoFrame.dispose();
        });

        JButton selectWardButton = new JButton("Make Transfer Request");
        selectWardButton.setBounds(400, 500, 100, 20);
        selectWardButton.addActionListener(evt -> {
            selectWard(finalP);
            infoFrame.dispose();
        });

        JButton editAge = new JButton("Edit Age");
        editAge.addActionListener(evt -> {
            editAge();
        });

        JButton editGender = new JButton("Edit Gender");
        editGender.addActionListener(evt -> {
            editParameter("Gender", this.getID());
            infoFrame.dispose();
        });

        JButton editSR = new JButton("Edit Sideroom Need");
        editSR.addActionListener(evt -> {
            editParameter("Sideroom", this.getID());
            infoFrame.dispose();
        });

        JButton editDia = new JButton("Edit Diagnosis");
        editDia.addActionListener(evt -> {
            editParameter("Diagnosis", this.getID());
            infoFrame.dispose();
        });

        JLabel editETD = new JLabel("<html>  To edit please use<br>'Make Transfer Request'</html>", SwingConstants.CENTER);

        // add the labels to the frame
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        infoPanel.add(bedIdLabel, c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        infoPanel.add(ageLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        infoPanel.add(editAge, c);

        c.gridx = 0;
        c.gridy = 2;
        infoPanel.add(genderLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        infoPanel.add(editGender, c);

        c.gridx = 0;
        c.gridy = 3;
        infoPanel.add(srLabel, c);
        c.gridx = 1;
        c.gridy = 3;
        infoPanel.add(editSR, c);

        c.gridx = 0;
        c.gridy = 4;
        infoPanel.add(diaLabel, c);
        c.gridx = 1;
        c.gridy = 4;
        infoPanel.add(editDia, c);

        c.gridx = 0;
        c.gridy = 5;
        infoPanel.add(ETDLabel, c);
        c.gridx = 1;
        c.gridy = 5;
        infoPanel.add(editETD, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 6;
        infoPanel.add(selectWardButton, c);

        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 7;
        infoPanel.add(deleteButton, c);


        infoFrame.add(infoPanel);
    }

    private void editAge() {
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
                methods.editPatient(this.getID(), "dateofbirth", "'"+DOB+"'");

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

    private void editParameter( String p , Integer bedId){
        JFrame editFrame = new JFrame("Edit "+p);
        editFrame.setSize(300,200);
        editFrame.setBackground(Color.WHITE);
        editFrame.setVisible(true);
        editFrame.setLocationRelativeTo(null);
        JPanel editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());

        JTextField newp = new JTextField("New "+p);

        JButton ConfirmButton = new JButton("Confirm");
        ConfirmButton.addActionListener(evt -> {
            editFrame.dispose();
            if(p == "Gender"){
                setSex(newp.getText());
            }
            if(p == "Sideroom"){
                setSR(false);
            }
            if(p == "Diagnosis") {
                try {

                    methods.editPatient(bedId, "initialdiagnosis", "'" + newp.getText() + "'");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        editPanel.add(newp);
        c.gridy = 1;
        editPanel.add(ConfirmButton);
        editFrame.add(editPanel);

    }

    private void selectWard(Patient p){
        Client client = new Client();
        JFrame infoFrame = new JFrame();

        //edit info Frame
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocationRelativeTo(null);

        ArrayList<Ward> wards = new ArrayList<Ward>();
        //FIXME get all wards more efficiently
        for(int i=3; i<8; i++) {
            try {
                ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+i);
                if(json.size()!=0){
                    Ward ward = client.wardsFromJson(json).get(0);
                    wards.add(ward);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
            setETDFrame(p);
        });
        infoFrame.add(setETDButton);
        JButton submitWard = new JButton("Submit");
        infoFrame.add(submitWard);
        submitWard.addActionListener(evt -> {
            String selected = longstayWards.getSelection().getActionCommand();
            try {
                ArrayList<String> json = client.makeGetRequest("*", "wards", "wardname='"+selected+"'");
                Ward ward = client.wardsFromJson(json).get(0);
                client.makePutRequest("patients", "nextdestination="+ward.getWardId(), "id="+p.getId());
                client.makePutRequest("patients", "transferrequeststatus='P'", "id="+p.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            infoFrame.dispose();
        });

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

        JButton addPatient = new JButton("Add a Patient");
        // button to edit information when it is clicked
        editButton.addActionListener(evt -> {
            //inputNewPatientInfo();
            infoFrame.dispose();
        });

        // add the labels to the frame
        infoFrame.setLayout(new GridLayout(4,1));
        infoFrame.add(bedIdLabel);
        infoFrame.add(srLabel);
        infoFrame.add(sexLabel);

        infoFrame.add(editButton);
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
        JLabel infoSex = new JLabel("Gender:     " , SwingConstants.RIGHT);
        String[] sex = { "Uni","Male","Female"};
        final JComboBox<String> pSex = new JComboBox<String>(sex);

        JLabel infoSR = new JLabel("Has a side room?     " , SwingConstants.RIGHT);
        String[] SR = {"false", "true"};
        final JComboBox<String> pSR = new JComboBox<String>(SR);

        JLabel infoStat = new JLabel("Bed Status     " , SwingConstants.RIGHT);
        String[] Stat = { "Open","Closed"};
        final JComboBox<String> pStat = new JComboBox<String>(Stat);


        // confirm button on editorPanel. when clicked, this button must (1) assign new values to all fields, (2) change color from green to red if bed was empty
        JButton confirmButton = new JButton("Confirm Edits");
        confirmButton.addActionListener(evt -> {

            try {
                //set side room
                methods.editBed(BedId, "hassideroom", (String)pSR.getSelectedItem());
                if(pSR.getSelectedItem() == "true"){
                    this.sideroom = true;
                } else {
                    this.sideroom = false;
                }
                //set Sex
                methods.editBed(BedId, "forsex", "'"+pSex.getSelectedItem()+"'");
                this.sex = (String)pSex.getSelectedItem();

                if((String)pStat.getSelectedItem() == "Open"){
                    makeOpen();
                } else { makeClosed();}

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            // get rid of editor popup after confirming edits
            editorFrame.dispose();
        });
        editorPanel.add(infoSex);
        editorPanel.add(pSex);
        editorPanel.add(infoSR);
        editorPanel.add(pSR);
        editorPanel.add(infoStat);
        editorPanel.add(pStat);
        editorPanel.add(confirmButton);
        editorPanel.setVisible(true);
        editorFrame.add(editorPanel);
    }


    private void inputNewPatientInfo(Patient p){
        // new popup over which everything is put
        JFrame editorFrame = new JFrame();
        editorFrame.setSize(700,600);
        editorFrame.setBackground(Color.WHITE);
        editorFrame.setVisible(true);
        editorFrame.setLocationRelativeTo(null);
        editorFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        // panel on editorFrame
        JPanel editorPanel = new JPanel();
        editorPanel.setLayout(new GridLayout(5,1));
        editorPanel.setBackground(Color.WHITE);

        JLabel infoDOB = new JLabel("Date of Birth     " , SwingConstants.RIGHT);
        JPanel panelDOB = new JPanel();
        panelDOB.setLayout(new GridLayout(2,3));

        JLabel dayLabel = new JLabel("Day");
        JLabel monthLabel = new JLabel("Month");
        JLabel yearLabel = new JLabel("Year");
        panelDOB.add(dayLabel);
        panelDOB.add(monthLabel);
        panelDOB.add(yearLabel);

        Integer[] days = new Integer[31];
        int inc=1;
        for(int i=0;i<31;i++){
            days[i]= inc;
            inc++;
        }
        JComboBox<Integer> day = new JComboBox<>(days);
        panelDOB.add(day);

        Integer[] months = new Integer[12];
        inc=1;
        for(int i=0;i<12;i++){
            months[i]= inc;
            inc++;
        }
        JComboBox<Integer> month = new JComboBox<>(months);
        panelDOB.add(month);

        int currentYear = 2021;
        Integer[] years = new Integer[currentYear];
        inc=currentYear;
        for(int i=0;i<currentYear;i++){
            years[i]= inc;
            inc--;
        }
        JComboBox<Integer> year = new JComboBox<>(years);
        panelDOB.add(year);

        // add input fields to editorPanel
        JLabel infoSex = new JLabel("Gender:     " , SwingConstants.RIGHT);
        String[] sex = {"Male","Female"};
        final JComboBox<String> pSex = new JComboBox<String>(sex);

        JLabel infoDia = new JLabel("Diagnosis:     " , SwingConstants.RIGHT);
        JTextField diaTextField = new JTextField("PleaseTypeOneWordOnly");



        // confirm button on editorPanel. when clicked, this button must (1) assign new values to all fields, (2) change color from green to red if bed was empty
        JLabel infoConf = new JLabel("Confirm:     " , SwingConstants.RIGHT);
        JButton confirmButton = new JButton("Submit");
        confirmButton.addActionListener(evt -> {

            // change bed color if bed goes from empty to full
            if (this.getStatus().equals("F")) {
                this.makeFull();
            }

            // asign new values for gender, age and diagnosis
            try {
                //Change date of birth
                LocalDate DOB = LocalDate.of(  (Integer) year.getSelectedItem(),  (Integer) month.getSelectedItem(),  (Integer) day.getSelectedItem() );
                methods.editPatient(p.getId(), "dateofbirth", "'"+DOB+"'");
                //Todo initial diagnosis multiple words!
                methods.editPatient(p.getId(), "initialdiagnosis", "'"+diaTextField.getText()+"'");

                //change sex
                methods.editPatient(p.getId(), "sex", "'"+pSex.getSelectedItem()+"'");
                this.sex = (String)pSex.getSelectedItem();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            // get rid of editor popup after confirming edits
            editorFrame.dispose();
        });

        editorPanel.add(infoSex);
        editorPanel.add(pSex);
        editorPanel.add(infoDOB);
        editorPanel.add(panelDOB);
        editorPanel.add(infoDia);
        editorPanel.add(diaTextField);
        editorPanel.add(infoConf);
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
                this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours));
                ETDFrame.dispose(); //close popup
            }
            // if only hours are inputted, change to the new hour o'clock
            if (!InputHrs.getText().equals("ETD Hours") && InputMins.getText().equals("ETD Minutes")) {
                int Minutes = 0;
                int Hours = Integer.parseInt(InputHrs.getText());
                this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours));
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

                else{ this.setETD(LocalDateTime.now().plusMinutes(Minutes).plusHours(Hours)); }
            }
            try {
                System.out.println(ETD);
                methods.editPatientETON(p.getId(), ETD);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        ETDPanel.add(InputHrs);
        ETDPanel.add(InputMins);
        ETDFrame.add(ETDPanel);
        ETDFrame.add(confirmButton);

    }

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
}