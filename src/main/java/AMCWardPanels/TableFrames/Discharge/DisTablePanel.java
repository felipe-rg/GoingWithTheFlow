package AMCWardPanels.TableFrames.Discharge;

import AMCWardPanels.TableFrames.ButtonColumn;
import AMCWardPanels.TableFrames.DeletePopUp;
import AMCWardPanels.TableFrames.IndexRenderer;
import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import Methods.GeneralWard;
import Methods.tableInfo.WardTableInfo.DischargeTableData;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;

public class DisTablePanel extends JPanel implements TableModelListener {

    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;

    //Model of the table
    private DisTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Index",
            "Bed ID",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "TTA Done?",
            "Discharge Lounge",
            "ETD",
            "Delete Button"};


    //2D Object that will contain the data to insert in the table
    private Object[][] dbData;

    //Methods that will be called
    private GeneralWard methods;

    //Constructor
    public DisTablePanel(GeneralWard methods){
        this.methods = methods;

        //Class that gets info from database and puts it into an object
        DischargeTableData dischargeTableData = new DischargeTableData(methods.getClient(), methods.getWardId());
        //Filling dbData with data from database
        dbData =dischargeTableData.getData();

        //Instantiating table with appropriate data and tablemodel
        tableModel = new DisTableModel(columnName, dbData);       //Instance of DisTableModel (extending from MyTableModel)
        table = new JTable(tableModel);         //Creating a table of model tablemodel
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
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 9);

        //Rendering index column so it displays it as a stirng (aligned to the left) for viewing purposes
        table.getColumnModel().getColumn(0).setCellRenderer(new IndexRenderer());


        //Setting layout and adding scrollpane with table
        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }

    //Class that edits the patient's side room, tta done or discharge lounge in the database
    private void editPatient(int patientId, String column, boolean value){
        try {
            methods.editPatient(patientId, column, String.valueOf(value));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Function that is called when the table is changed, it prints out what has been edited and calls
    //editPatient function to edit database
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
        System.out.println("Index: " + patientId + "     Edited '" + columnName+ "': " +data);

        //Editing database
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


    //Function editing table in general
    public void setupTable(JTable table){
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);

    }
}
