package Panels.TableFrames;

import Methods.GeneralWard;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DisTablePanel extends JPanel implements TableModelListener {

    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private DisTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "TTA Done?",
            "Discharge Lounge",
            "ETD",
            "Delete Button"};

    private Object[][] dbData;

    private GeneralWard methods;

    public DisTablePanel(GeneralWard methods){

        this.methods = methods;

        try {
            dbData = methods.getDischargeData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Instantiating table with appropriate data and tablemodel

        tableModel = new DisTableModel(columnName, dbData);        //Instance of MytableModel
        table = new JTable(tableModel);         //Creating a table of model tablemodel (instance of MyTableModel)
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)

        //Editing table
        setupTable(table);

        //Action happening when we press delete button
        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel, methods);
            }
        };
        //Assigning the column that will have the delete buttons
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 8);

        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }

    //Used to change the patient information on the database
    private void editPatient(int patientId, String column, boolean value){
        try {
            methods.editPatient(patientId, column, String.valueOf(value));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (DisTableModel)e.getSource();   //Tablemodel used

        //Name of the column and data introduced
        String columnName = tableModel.getColumnName(column);
        Object data = tableModel.getValueAt(row, column);

        int patientId = tableModel.getPatientID(table.getSelectedRow());

        //Printing out what has been edited
        System.out.println("Patient bed: " + patientId + "     Edited '" + columnName+ "': " +data);
        if(columnName == "TTA Done?"){
            editPatient(patientId, "ttasignedoff", (boolean)data);
        }
        if(columnName == "Side Room"){
            editPatient(patientId, "needssideroom", (boolean)data);
        }
        if(columnName == "Discharge Lounge"){
            editPatient(patientId, "suitablefordischargelounge", (boolean)data);
        }

    }

    public void setupTable(JTable table){
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);

    }
}
