package AMCWardPanels.TableFrames.Transfer;

import AMCWardPanels.TableFrames.ButtonColumn;
import AMCWardPanels.TableFrames.DeletePopUp;
import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import Methods.AMCWard;
import Methods.tableInfo.TransTableData;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TransTablePanel extends JPanel implements TableModelListener {
    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;

    //Model of the table
    private TransTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Index",
            "Bed ID",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "ETT",
            "New Ward",
            "Delete Button"
            };

    //2D Object that will contain the data to insert in the table
    private Object[][] dbData;

    //Methods that will be called
    private AMCWard methods;

    //Constructor
    public TransTablePanel(AMCWard methods){
        this.methods = methods;

        //Class that gets info from database and puts it into an object
        TransTableData transTableData = new TransTableData(methods.getClient(), methods.getWardId());
        //Filling dbData with data from database
        dbData = transTableData.getData();

        //Instantiating table with appropriate data and tablemodel
        tableModel = new TransTableModel(columnName, dbData);        //Instance of TransTableModel (extending from MyTableModel)
        table = new JTable(tableModel);         //Creating a table of model tablemodel
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)


        //Editing table
        setupTable(table);

        //Actions for delet button and request ward button respectively
        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel, methods);
            }
        };
        Action requestWard = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("New Ward selected");
            }
        };

        //Assigning the column that will have the delete buttons
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 8);

        //Setting layout and adding scrollpane with table
        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }



    //Function that is called when the table is changed, it prints out what has been edited and calls
    //editPatient function to edit database
    @Override
    public void tableChanged(TableModelEvent e) {
        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (TransTableModel)e.getSource();   //Tablemodel used

        //Name of the column and data introduced
        String columnName = tableModel.getColumnName(column);
        Object data = tableModel.getValueAt(row, column);
        //Bednumber of row selected
        Object bedNum = tableModel.getValueAt(row, 0);

        //Printing out what has been edited
        System.out.println("Patient bed: " + bedNum + "     Edited '" + columnName+ "': " +data);
        int patientId = tableModel.getPatientID(table.getSelectedRow());

        if(columnName == "Side Room"){
            editPatient(patientId, "needssideroom", (boolean)data);
        }
    }

    //Class that edits the patient's attributes in the database
    private void editPatient(int patientId, String column, boolean value){
        try {
            methods.editPatient(patientId, column, String.valueOf(value));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Editing table
    public void setupTable(JTable table) {
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);
    }
}
