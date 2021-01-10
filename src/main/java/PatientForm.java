import Client.Patient;
import Methods.AandE;
import AMCWardPanels.Title;

import javax.swing.*;
import javax.swing.border.Border;
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

        // Creates mainPanel
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));

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

        // this is the panel where patient info will be added
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(8, 2));

        JLabel instructLabel = new JLabel("Please fill out this patient form for the AMC", SwingConstants.RIGHT);
        instructLabel.setFont(instructLabel.getFont().deriveFont(20.0f));
        JLabel blank = new JLabel(" ");

        // patient ID field
        JLabel infoID = new JLabel("Patient ID:", SwingConstants.RIGHT);
        JTextField pID = new JTextField(10);

        // patient sex field
        JLabel infoSex = new JLabel("Gender:", SwingConstants.RIGHT);
        String[] sex = {"Male", "Female"};
        final JComboBox<String> pSex = new JComboBox<String>(sex);

        // patient DOB field
        JLabel infoDOB = new JLabel("Date of Birth:", SwingConstants.RIGHT);

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
        Integer[] months = new Integer[12];
        counter = 1;
        for (int i = 0; i < 12; i++) {
            months[i] = counter;
            counter++;
        }
        JComboBox<Integer> month = new JComboBox<>(months);
        int currentYear = 2021;
        Integer[] years = new Integer[currentYear];
        counter = currentYear;
        for (int i = 0; i < currentYear; i++) {
            years[i] = counter;
            counter--;
        }
        JComboBox<Integer> year = new JComboBox<>(years);

        // Initial diagnosis field
        JLabel infoIll = new JLabel("Initial Diagnosis:", SwingConstants.RIGHT);
        JTextField pIll = new JTextField(20);

        // Sideroom field
        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        final JCheckBox pSR = new JCheckBox();

//        JLabel infoSR = new JLabel("Sideroom:", SwingConstants.RIGHT);
//        String[] sideRoom = {"No", "Yes"};
//        final JComboBox<String> pSR = new JComboBox<String>(sideRoom);
//        boolean sRoom = false;
//        if (pSR.getSelectedItem() == "Yes") {
//            sRoom = true;
//        }
//        final boolean SR = sRoom;

        JLabel blank1 = new JLabel("  ");
        JButton submit = new JButton("Submit Form");

        //Submit button instructions
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AandE aneUser = new AandE(1);
                Patient p = null;
                try {
                    if (pID.getText().length() != 10) {      // checks ID is 10 characters long
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

        JLabel one = new JLabel("/");
        JLabel two = new JLabel("/");

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.CENTER;
        c.gridwidth = 7;
        c.gridx = 0;
        c.gridy = 0;
        c.ipady = 50;
        mainPanel.add(instructLabel, c);
        // patient ID
        c.anchor = GridBagConstraints.EAST;
        c.ipady = 20;
        c.gridy = 1;
        c.gridwidth = 1;
        mainPanel.add(infoID, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        c.ipady = 5;
        c.gridx = 1;
        mainPanel.add(pID, c);
        // gender
        c.ipady = 20;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 2;
        mainPanel.add(infoSex, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        c.ipady = 20;
        c.gridx = 1;
        mainPanel.add(pSex, c);
        // Date of Birth DoB
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.CENTER;
        c.ipady = 10;
        c.gridy = 3;
        c.gridx = 1;
        mainPanel.add(dayLabel, c);
        c.gridx = 3;
        mainPanel.add(monthLabel, c);
        c.gridx = 5;
        mainPanel.add(yearLabel, c);
        c.anchor = GridBagConstraints.EAST;
        c.gridy = 6;
        c.gridx = 0;
        mainPanel.add(infoDOB, c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        mainPanel.add(day, c);
        c.gridx = 2;
        mainPanel.add(one, c);
        c.gridx = 3;
        mainPanel.add(month, c);
        c.gridx = 4;
        mainPanel.add(two, c);
        c.gridx = 5;
        mainPanel.add(year, c);
        // initial diagnosis
        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        c.gridy = 8;
        mainPanel.add(infoIll, c);
        c.ipady = 5;
        c.gridwidth = 5;
        c.gridx = 1;
        c.anchor = GridBagConstraints.WEST;
        mainPanel.add(pIll, c);
        // sideroom
        c.ipady = 20;
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 10;
        mainPanel.add(infoSR, c);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        c.gridwidth = 2;
        mainPanel.add(pSR, c);
        // submit button
        c.gridx = 1;
        c.gridy = 12;
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        mainPanel.add(submit, c);


        f.add(mainPanel);
        f.add(titlePanel , BorderLayout.NORTH);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);


    }
}


