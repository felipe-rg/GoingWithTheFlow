import Client.Patient;
import Methods.AandE;
import AMCWardPanels.Title;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
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

    private static final Logger log = Logger.getLogger(PatientForm.class.getName());

    public PatientForm() {

        log.info("PatientForm Started");

        // Creates mainPanel which has all the elements of the patient form
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();

        // creates back and refresh buttons
        JButton backButton = new JButton("Cancel & go back");
        JButton refreshButton = new JButton("Refresh Page");

        // adds buttons to title class that creates title
        Title titlePanel = new Title("A&E Incoming Patient Form", backButton, refreshButton, 230, 200);
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


        JLabel instructLabel = new JLabel("Please fill out this patient form for the AMC     ", SwingConstants.RIGHT);
        instructLabel.setFont(instructLabel.getFont().deriveFont(20.0f));

        // patient ID field
        JLabel infoID = new JLabel("Patient ID:     ", SwingConstants.RIGHT);
        infoID.setFont(instructLabel.getFont().deriveFont(15.0f));
        JTextField pID = new JTextField(10);

        // patient sex field
        JLabel infoSex = new JLabel("Gender:     ", SwingConstants.RIGHT);
        infoSex.setFont(instructLabel.getFont().deriveFont(15.0f));
        String[] sex = {"Male", "Female"};
        final JComboBox<String> pSex = new JComboBox<String>(sex);
        pSex.setPreferredSize(new Dimension(70,2));

        // patient DOB field
        JLabel infoDOB = new JLabel("Date of Birth:     ", SwingConstants.RIGHT);
        infoDOB.setFont(instructLabel.getFont().deriveFont(15.0f));

        JLabel dayLabel = new JLabel("Day");
        JLabel monthLabel = new JLabel("Month");
        JLabel yearLabel = new JLabel("Year");

        // Days option
        Integer[] days = new Integer[31];
        int counter = 1;
        for (int i = 0; i < 31; i++) {
            days[i] = counter;
            counter++;
        }
        JComboBox<Integer> day = new JComboBox<>(days);
        day.setPreferredSize(new Dimension(45,2));
        Integer[] months = new Integer[12];
        counter = 1;
        for (int i = 0; i < 12; i++) {
            months[i] = counter;
            counter++;
        }
        JComboBox<Integer> month = new JComboBox<>(months);
        month.setPreferredSize(new Dimension(45,2));
        int currentYear = 2021;
        Integer[] years = new Integer[currentYear];
        counter = currentYear;
        for (int i = 0; i < currentYear; i++) {
            years[i] = counter;
            counter--;
        }
        JComboBox<Integer> year = new JComboBox<>(years);
        year.setPreferredSize(new Dimension(70,2));

        // Initial diagnosis field
        JLabel infoIll = new JLabel("Initial Diagnosis:     ", SwingConstants.RIGHT);
        infoIll.setFont(instructLabel.getFont().deriveFont(15.0f));
        JTextField pIll = new JTextField(20);

        // Sideroom field
        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        infoSR.setFont(instructLabel.getFont().deriveFont(15.0f));
        final JCheckBox pSR = new JCheckBox();


        JButton submit = new JButton("Submit Form");

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
                    } else if (aneUser.checkExistingId(pID.getText())) { // checks ID does not already exist
                        JOptionPane.showMessageDialog(null, "Already existing Patient ID! Try again.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        // sets DOB using LocalDate
                        LocalDate DOB = LocalDate.of((Integer) year.getSelectedItem(), (Integer) month.getSelectedItem(), (Integer) day.getSelectedItem());
                        //create a new patient
                        p = new Patient(pID.getText(), (String) pSex.getSelectedItem(), DOB, pIll.getText(), pSR.isSelected());
                        // sets location of patient
                        aneUser.createPatient(p);

                        if (aneUser.checkAddedPatient(p)) {      // checks patient has been added to database

                            // notifies user of successful database update
                            JOptionPane.showMessageDialog(null, "Patient has been added to database.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);

                            log.info("Patient has been added to database");

                            f.dispose();                        // closes frame
                            UserPage user = new UserPage();     // reopens user page
                        } else {
                            JOptionPane.showMessageDialog(null, "Patient has NOT been added to database! Try again", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            log.warning("Patient not added to database");
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(600,500));
        panel.setMinimumSize(new Dimension(600,500));

        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // patient ID
        c.anchor = GridBagConstraints.EAST;
        c.ipady = 30;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(infoID, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        c.ipady = 2;
        c.gridx = 1;
        panel.add(pID, c);
        // gender
        c.ipady = 30;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 2;
        panel.add(infoSex, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        c.ipady = 30;
        c.gridx = 1;
        panel.add(pSex, c);
        // Date of Birth DoB
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.ipady = 10;
        c.gridy = 3;
        c.gridx = 1;
        panel.add(dayLabel, c);
        c.gridx = 3;
        panel.add(monthLabel, c);
        c.gridx = 5;
        panel.add(yearLabel, c);
        c.ipady = 30;
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 6;
        c.gridx = 0;
        panel.add(infoDOB, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        panel.add(day, c);
        c.gridx = 3;
        c.insets = new Insets(0,15,0,0);
        panel.add(month, c);
        c.insets = new Insets(0,15,0,0);
        c.gridx = 5;
        panel.add(year, c);
        // initial diagnosis
        c.insets = new Insets(0,0,0,0);
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 8;
        panel.add(infoIll, c);
        c.ipady = 5;
        c.gridwidth = 5;
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        panel.add(pIll, c);
        // sideroom
        c.ipady = 60;
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 10;
        panel.add(infoSR, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        c.gridwidth = 2;
        panel.add(pSR, c);
        // submit button
        c.ipady = 2;
        c.gridx = 1;
        c.gridy = 12;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        panel.add(submit, c);

        // adding a square around the form
        Border blackline = BorderFactory.createLineBorder(Color.black);
        TitledBorder title;
        title = BorderFactory.createTitledBorder(blackline,"<html><center>Please fill out this<br>patient form for the AMC</center></html>");
        title.setTitleJustification(TitledBorder.CENTER);
        title.setTitleFont(new Font("Verdana",Font.PLAIN,20));
        panel.setBorder(title);

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridheight = 50;
        c2.gridwidth = 40;
        // adding everything to mainPanel and then the frame.
        mainPanel.add(panel, c2);
        f.add(mainPanel);
        f.add(titlePanel , BorderLayout.NORTH);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);


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
