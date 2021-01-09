import Client.Patient;
import Methods.AandE;
import AMCWardPanels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PatientForm {

    private static final Logger log= Logger.getLogger(PatientForm.class.getName());

    public PatientForm() {

        log.info("PatientForm Started");

        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

        JButton backButton = new JButton("Cancel & go back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("A&E Incoming Patient Form" , backButton, refreshButton, 230, 200);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                try {
                    UserPage user = new UserPage();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                PatientForm form = new PatientForm();       // class will be called again
            }
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(8,2));

        JLabel instructLabel = new JLabel("Please fill out this patient form for the AMC" , SwingConstants.RIGHT);
        JLabel blank = new JLabel(" ");

        JLabel infoID = new JLabel("Patient ID:     ", SwingConstants.RIGHT);
        JTextField pID = new JTextField(10);
        JPanel panelID = new JPanel();
        panelID.add(pID);
        padding(panelID);


        JLabel infoSex = new JLabel("Gender:     " , SwingConstants.RIGHT);
        String[] sex = { "Male","Female"};
        final JComboBox<String> pSex = new JComboBox<String>(sex);
        JPanel panelSex = new JPanel();
        panelSex.add(pSex);
        padding(panelSex);

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

        JLabel infoIll = new JLabel("Initial Diagnosis     " , SwingConstants.RIGHT);
        JTextField pIll = new JTextField( 10 );
        JPanel panelIll = new JPanel();
        panelIll.add(pIll);
        padding(panelIll);

        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        String[] sideRoom = { "No","Yes"};
        final JComboBox<String> pSR = new JComboBox<String>(sideRoom);
        JPanel panelSR = new JPanel();
        panelSR.add(pSR);
        padding(panelSR);
        boolean sRoom = false;
        if (pSR.getSelectedItem() == "Yes") { sRoom = true;}
        final boolean SR = sRoom;

        JLabel blank1 = new JLabel("  ");
        JButton submit = new JButton ("Submit Form");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AandE aneUser = new AandE(1);
                Patient p = null;
                try {
                    if (pID.getText().length() != 10){
                        JOptionPane.showMessageDialog(null, "Invalid Patient ID! It must contain 10 digits.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else if (aneUser.checkExistingId(pID.getText())) {
                        JOptionPane.showMessageDialog(null, "Already existing Patient ID! Try again.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        LocalDate DOB = LocalDate.of((Integer) year.getSelectedItem(), (Integer) month.getSelectedItem(), (Integer) day.getSelectedItem());
                        p = new Patient(pID.getText(), (String) pSex.getSelectedItem(), DOB, pIll.getText(), SR);
                        aneUser.createPatient(p);

                        if(aneUser.checkAddedPatient(p)) {
                            JOptionPane.showMessageDialog(null, "Patient has been added to database.", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                            log.info("Patient has been added to database");
                            f.dispose();
                            UserPage user = new UserPage();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "Patient has NOT been added to database! Try again", "Warning", JOptionPane.INFORMATION_MESSAGE);
                            log.warning("Patient not added to database");
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    //log.severe("UserPage Started");
                }
            }
        });

        JPanel panelSub = new JPanel();
        panelSub.add(submit);
        padding(panelSub);

        padding(infoPanel);
        infoPanel.add(instructLabel);
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

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(infoPanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);

    }
    public void padding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 500));
    }
}
