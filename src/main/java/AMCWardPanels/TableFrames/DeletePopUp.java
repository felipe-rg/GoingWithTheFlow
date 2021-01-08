package AMCWardPanels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/*
        This class will be called when the delete button is pressed. A new frame appears asking you if
        you really want to delete. If you click no then this frame dissapears and if you click yes,
        frame dissapears and patient is deleted from table and from database

 */

public class DeletePopUp {

    //Construnctor
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
                //Removing patient from database
                try {
                    methods.deletePatient(patientID);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                //Removing row from table
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