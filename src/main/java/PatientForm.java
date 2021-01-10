import Client.Patient;
import Methods.AandE;
import AMCWardPanels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.Logger;

/*
This page takes in patient information to add to the database
 */

public class PatientForm {

    private static final Logger log= Logger.getLogger(PatientForm.class.getName());

    public PatientForm() {

        log.info("PatientForm Started");

        // Creates mainPanel
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

        // creates back and refresh buttons
        JButton backButton = new JButton("Cancel & go back");
        JButton refreshButton = new JButton("Refresh Page");

        // adds buttons to title class that creates title
        Title titlePanel = new Title("A&E Incoming Patient Form" , backButton, refreshButton, 230, 200);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));

        // back button instructions
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();                                    // close frame
                try {
                    UserPage user = new UserPage();             // open user page
                } catch (IOException ioException) {
                    ioException.printStackTrace();

                }
            }
        });

        // refresh button instructions
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                PatientForm form = new PatientForm();       // class will be called again
            }
        });

        // this is the panel where patient info will be added
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(8,2));

        JLabel instructLabel = new JLabel("Please fill out this patient form for the AMC" , SwingConstants.RIGHT);
        JLabel blank = new JLabel(" ");

        // patient ID field
        JLabel infoID = new JLabel("Patient ID:", SwingConstants.RIGHT);
        JTextField pID = new JTextField(10);
        JPanel panelID = new JPanel();
        panelID.add(pID);
        padding(panelID);

        // patient sex field
        JLabel infoSex = new JLabel("Gender:" , SwingConstants.RIGHT);
        String[] sex = { "Male","Female"};
        final JComboBox<String> pSex = new JComboBox<String>(sex);
        JPanel panelSex = new JPanel();
        panelSex.add(pSex);
        padding(panelSex);

        // patient DOB field
        JLabel infoDOB = new JLabel("Date of Birth" , SwingConstants.RIGHT);
        JPanel panelDOB = new JPanel();
        panelDOB.setLayout(new GridLayout(2,3));

        JLabel dayLabel = new JLabel("Day");
        JLabel monthLabel = new JLabel("Month");
        JLabel yearLabel = new JLabel("Year");
        panelDOB.add(dayLabel);
        panelDOB.add(monthLabel);
        panelDOB.add(yearLabel);

        // Days option
        Integer[] days = new Integer[31];
        int counter=1;
        for(int i=0;i<31;i++){
            days[i]= counter;
            counter++;
        }
        JComboBox<Integer> day = new JComboBox<>(days);
        panelDOB.add(day);
        // Months option
        Integer[] months = new Integer[12];
        counter=1;
        for(int i=0;i<12;i++){
            months[i]= counter;
            counter++;
        }
        JComboBox<Integer> month = new JComboBox<>(months);
        panelDOB.add(month);
        // Years option
        int currentYear = 2021;
        Integer[] years = new Integer[currentYear];
        counter=currentYear;
        for(int i=0;i<currentYear;i++){
            years[i]= counter;
            counter--;
        }
        JComboBox<Integer> year = new JComboBox<>(years);
        panelDOB.add(year);

        // Initial diagnosis field
        JLabel infoIll = new JLabel("Initial Diagnosis     " , SwingConstants.RIGHT);
        JTextField pIll = new JTextField( 10 );
        JPanel panelIll = new JPanel();
        panelIll.add(pIll);
        padding(panelIll);

        // Sideroom field
        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        final JCheckBox pSR = new JCheckBox();
        JPanel panelSR = new JPanel();
        panelSR.add(pSR);
        padding(panelSR);


        JLabel blank1 = new JLabel("  ");
        JButton submit = new JButton ("Submit Form");

        //Submit button instructions
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AandE aneUser = new AandE(1);
                Patient p = null;
                try {
                    // checks ID is 10 characters long and all numbers
                    if (pID.getText().length() != 10 || isNumeric(pID.getText()) == false){
                        JOptionPane.showMessageDialog(null, "Invalid Patient ID! It must contain 10 digits.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if (aneUser.checkExistingId(pID.getText())) { // checks ID does not already exist
                        JOptionPane.showMessageDialog(null, "Already existing Patient ID! Try again.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        // sets DOB using LocalDate
                        LocalDate DOB = LocalDate.of((Integer) year.getSelectedItem(), (Integer) month.getSelectedItem(), (Integer) day.getSelectedItem());
                        //create a new patient
                        p = new Patient(pID.getText(), (String) pSex.getSelectedItem(), DOB, pIll.getText(), pSR.isSelected());
                        // sets location of patient
                        aneUser.createPatient(p);

                        if(aneUser.checkAddedPatient(p)) {      // checks patient has been added to database

                            // notifies user of successful database update
                            JOptionPane.showMessageDialog(null, "Patient has been added to database.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);

                            log.info("Patient has been added to database");

                            f.dispose();                        // closes frame
                            UserPage user = new UserPage();     // reopens user page
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Patient has NOT been added to database! Try again", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            log.warning("Patient not added to database");
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        // adds submit button to panel
        JPanel panelSub = new JPanel();
        panelSub.add(submit);
        padding(panelSub);

        padding(infoPanel);
        infoPanel.add(instructLabel);       // adds components to info panel
        infoPanel.add(blank);
        infoPanel.add(infoID);
        infoPanel.add(panelID);
        infoPanel.add(infoSex);
        infoPanel.add(panelSex);
        infoPanel.add(infoDOB);
        infoPanel.add(panelDOB);
        infoPanel.add(infoIll);
        infoPanel.add(panelIll);
        infoPanel.add(infoSR);
        infoPanel.add(panelSR);
        infoPanel.add(blank1);
        infoPanel.add(panelSub);

        // organises mainPanel
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(infoPanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);

    }

    // adds JPanel borders
    public void padding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 500));
    }

    // function checks if the string is full of numbers
    // reference code: https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
