import Panels.Title;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PatientForm {

    public PatientForm() {

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
        JPanel panelID = new JPanel();
        panelID.add(pID);
        padding(panelID);


        JLabel infoGend = new JLabel("Gender:     " , SwingConstants.RIGHT);
        String[] gender = { "Male","Female"};
        final JComboBox<String> pGend = new JComboBox<String>(gender);
        JPanel panelGend = new JPanel();
        panelGend.add(pGend);
        padding(panelGend);

        JLabel infoIll = new JLabel("Initial Diagnosis     " , SwingConstants.RIGHT);
        JTextField pIll = new JTextField( 10 );
        JPanel panelIll = new JPanel();
        panelIll.add(pIll);
        padding(panelIll);

        JLabel infoSR = new JLabel("Is a sideroom required?     ", SwingConstants.RIGHT);
        String[] sideroom = { "No","Yes"};
        final JComboBox<String> pSR = new JComboBox<String>(sideroom);
        JPanel panelSR = new JPanel();
        panelSR.add(pSR);
        padding(panelSR);

        JLabel blank1 = new JLabel("  ");
        JButton submit = new JButton ("Submit Form");

        submit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (pID.getText().length() != 10){
                    JOptionPane.showMessageDialog(null, "Invalid Patient ID", "Warning", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    f.dispose();
                    UserPage user = new UserPage();
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
        infoPanel.add(infoGend);
        infoPanel.add(panelGend);
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
