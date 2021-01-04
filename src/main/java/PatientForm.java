import Client.Patient;
import Panels.Title;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class PatientForm {

    public PatientForm() {

        JFrame f = new JFrame();                                                       //creates main JFrame
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));  // adds border

        // Sets up title panel with back option
        JButton backButton = new JButton("Cancel & go back");
        JButton refreshButton = new JButton("Refresh Page");
        Title titlePanel = new Title("A&E Incoming Patient Form" , backButton , refreshButton);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when back button is selected
                f.dispose();                                // patient form will close
                UserPage user = new UserPage();             // user page frame will open
            }
        });

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {    // when refresh button is selected
                f.dispose();                                // current frame will close
                PatientForm form = new PatientForm();       // class will be called again
            }
        });

        JPanel infoPanel = new JPanel();                    // this is the panel where patient info is added
        infoPanel.setLayout(new GridLayout(8,2));

        // Form instructions
        JLabel instructLabel = new JLabel("Please this form:     " , SwingConstants.RIGHT);
        JLabel blank = new JLabel(" ");
        instructLabel.setFont(new Font("Verdana", Font.PLAIN, 30));

        // Patient ID information (must be 10 characters)
        JLabel infoID = new JLabel("Patient ID     ", SwingConstants.RIGHT);
        JTextField pID = new JTextField(10);                           // info added to a text field
        JPanel panelID = new JPanel();
        panelID.add(pID);
        padding(panelID);

        // Patient's sex information
        JLabel infoSex = new JLabel("Sex     " , SwingConstants.RIGHT);
        String[] sex = { "Male","Female"};                                     // drop-down options
        final JComboBox<String> pSex = new JComboBox<>(sex);                   // info added via drop-down menu
        JPanel panelSex = new JPanel();
        panelSex.add(pSex);
        padding(panelSex);

        // Patient DOB information - not used yet
        JLabel infoDOB = new JLabel("Date of Birth     " , SwingConstants.RIGHT);
        JPanel panelDOB = new JPanel();
        panelDOB.setLayout(new GridLayout(2,3));        // another panel for layout purposes

        JLabel dayLabel = new JLabel("Day");
        JLabel monthLabel = new JLabel("Month");
        JLabel yearLabel = new JLabel("Year");
        panelDOB.add(dayLabel);
        panelDOB.add(monthLabel);
        panelDOB.add(yearLabel);

        Integer[] days = new Integer[31];                   // sets number of days in a month
        int counter = 1;
        for(int i = 0 ; i < 31 ; i++){
            days[i] = counter;
            counter++;
        }
        JComboBox<Integer> day = new JComboBox<>(days);     // creates drop-down option
        panelDOB.add(day);

        Integer[] months = new Integer[12];                // sets number of months in a year
        counter = 1;
        for(int i = 0; i < 12 ; i++){
            months[i] = counter;
            counter++;
        }
        JComboBox<Integer> month = new JComboBox<>(months);     // creates drop-down option
        panelDOB.add(month);

        int currentYear = 2020;                            // CAN WE MAKE THIS DYNAMIC??
        Integer[] years = new Integer[currentYear];
        counter = currentYear;
        for(int i = 0 ; i < currentYear ; i++){
            years[i] = counter;
            counter--;
        }
        JComboBox<Integer> year = new JComboBox<>(years);
        panelDOB.add(year);

        // saves DOB as a LocalDate variable for the database
        LocalDate DOB = LocalDate.of(  (Integer) year.getSelectedItem(),  (Integer) month.getSelectedItem(),  (Integer) day.getSelectedItem() );

        // Patient initial diagnosis information
        JLabel infoIll = new JLabel("Initial Diagnosis     " , SwingConstants.RIGHT);
        JTextField pIll = new JTextField( 10 );     // info added via a text field
        JPanel panelIll = new JPanel();
        panelIll.add(pIll);
        padding(panelIll);

        // Patient sideroom information
        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        String[] sideRoom = { "No","Yes"};                          // drop-down options
        final JComboBox<String> pSR = new JComboBox<>(sideRoom);    // drop-down options
        JPanel panelSR = new JPanel();
        panelSR.add(pSR);
        padding(panelSR);
        // these lines convert the drop-down options to a boolean for the database
        boolean sRoom = false;
        if (pSR.getSelectedItem() == "Yes") { sRoom = true;}
        final boolean SR = sRoom;

        JLabel blank1 = new JLabel("  ");               // for spacing purposes

        // Form submit button
        JButton submit = new JButton ("Submit Form");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // if statement checks whether patient ID is in the correct form (for length and numbers)
                if (pID.getText().length() != 10 || isNumeric(pID.getText()) == false){
                    // if ID is incorrect
                    JOptionPane.showMessageDialog(null, "Invalid Patient ID. Please note that the ID must be exactly 10 characters and all numbers.", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    // if ID is correct a new patient will be created in the database
                    Patient p = new Patient( pID.getText() , (String) pSex.getSelectedItem() ,pIll.getText(), SR);
                    // user will be notified that the patient has been added to the database
                    JOptionPane.showMessageDialog(null, "Patient has been added to database", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                    f.dispose();                        //form will close
                    UserPage user = new UserPage();     // user will be brought back to the user page
                    }
                }
        });
        JPanel panelSub = new JPanel();
        panelSub.add(submit);
        padding(panelSub);

        padding(infoPanel);                 // adds all the components to the info panel
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

        mainPanel.setLayout(new BorderLayout());        // sets up mainPanel layout and adds the sub panels
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(infoPanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }

    // this adds padding to panels for asthetics
    public void padding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(30, 200, 30, 500));
    }

    // checks that string is full of numbers (checks patient ID)
    /* Reference 1 - taken from https://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java/32313369 */
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);                // if correct, true will be returned
            return true;
        } catch(NumberFormatException e){
            return false;
        }
        /* end of reference 1 */
    }
}
