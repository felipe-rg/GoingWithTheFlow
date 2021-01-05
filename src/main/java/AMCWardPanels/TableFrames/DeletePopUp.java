package AMCWardPanels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


public class DeletePopUp {

    public DeletePopUp(JTable table, MyTableModel tableModel, GeneralWard methods){
        //We create new frame
        JFrame deleteRowConfirmation = new JFrame("Delete Confirmation");
        deleteRowConfirmation.setSize(400,300);

        //Introduce buttons and labels
        JLabel confirmation = new JLabel("Are you sure you want to delete this patient?");
        JButton yesButton = new JButton("Yes");
        JButton noButton = new JButton("No");

        //Editing label
        confirmation.setHorizontalAlignment(SwingConstants.CENTER);
        confirmation.setFont(new Font("Verdana",Font.PLAIN, 15));

        //Action to happen when 'no' button is clicked
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRowConfirmation.dispose();
            }
        });
        //Action to happen when 'yes' button is clicked
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int patientID = tableModel.getPatientID(table.getSelectedRow());
                try {
                    methods.deletePatient(patientID);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                tableModel.removeRow(table.getSelectedRow());
                deleteRowConfirmation.dispose();
            }
        });

        //Editing and adding the components to the frame
        deleteRowConfirmation.setLayout(new GridLayout(3,1));
        deleteRowConfirmation.add(confirmation);
        deleteRowConfirmation.add(yesButton);
        deleteRowConfirmation.add(noButton);

        deleteRowConfirmation.setLocationRelativeTo(null);
        deleteRowConfirmation.setVisible(true);
        deleteRowConfirmation.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }



}