import Methods.AandE;
import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//todo get input into variables

public class PatientForm {

    public PatientForm() {
        AandE methods = new AandE(1);
        JFrame f = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK , 3));

        JButton backButton = new JButton("Cancel & go back");
        Title titlePanel = new Title("A&E Incoming Patient Form" , backButton);
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, Color.BLACK));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                f.dispose();
                UserPage user = new UserPage();
            }
        });

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(7,2));

        JLabel instructLabel = new JLabel("Please fill out this patient form for the AMC" , SwingConstants.RIGHT);
        JLabel blank = new JLabel(" ");

        JLabel infoID = new JLabel("Patient ID:     ", SwingConstants.RIGHT);
        JTextField pID = new JTextField(10);
        //pID.setPreferredSize(new Dimension(30,25));

        JLabel infoGend = new JLabel("Gender:     " , SwingConstants.RIGHT);
        String[] gender = { "Male","Female"};
        final JComboBox<String> pGend = new JComboBox<String>(gender);
        JLabel infoIll = new JLabel("Initial Diagnosis     " , SwingConstants.RIGHT);
        JTextField pIll = new JTextField();

        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        String[] sideroom = { "No","Yes"};
        final JComboBox<String> pSR = new JComboBox<String>(sideroom);

        JLabel blank1 = new JLabel("  ");
        JButton submit = new JButton ("Submit Form");
        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //methods.createPatient(infoID, sex, infoIll, );
                f.dispose();
                UserPage user = new UserPage();
            }
        });

        padding(infoPanel);
        infoPanel.add(instructLabel);
        infoPanel.add(blank);
        infoPanel.add(infoID);
        infoPanel.add(pID);
        infoPanel.add(infoGend);
        infoPanel.add(pGend);
        infoPanel.add(infoIll);
        infoPanel.add(pIll);
        infoPanel.add(infoSR);
        infoPanel.add(pSR);
        infoPanel.add(blank1);
        infoPanel.add(submit);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(titlePanel , BorderLayout.NORTH);
        mainPanel.add(infoPanel , BorderLayout.CENTER);

        f.getContentPane().add(mainPanel);
        f.setVisible(true);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setExtendedState(Frame.MAXIMIZED_BOTH);
    }
    public void padding(JPanel panel){
        panel.setBorder(BorderFactory.createEmptyBorder(30, 500, 30, 500));
    }
}