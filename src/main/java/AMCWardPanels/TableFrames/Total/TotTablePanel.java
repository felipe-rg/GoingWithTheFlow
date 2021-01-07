package AMCWardPanels.TableFrames.Total;

import AMCWardPanels.TableFrames.ButtonColumn;
import AMCWardPanels.TableFrames.DeletePopUp;
import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import Methods.GeneralWard;
import Methods.tableInfo.TotalTableData;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TotTablePanel extends JPanel implements TableModelListener {
    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private TotTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Index",
            "Bed Id",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "Time of stay (h)",        //This one can be obtained from current time and arrival time (not too difficult)
            "Next Destination",
            "Delete Button"};

    private Object[][] dbData;
    private GeneralWard methods;
    public TotTablePanel(GeneralWard methods, TotalTableData totalTableData){

        this.methods = methods;
        dbData = totalTableData.getData();

        //Instantiating table with appropriate data and tablemodel

        tableModel = new TotTableModel(columnName, dbData);        //Instance of MytableModel
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

        //
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 8);

        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }


    @Override
    public void tableChanged(TableModelEvent e) {

        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (TotTableModel)e.getSource();   //Tablemodel used

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

    private void editPatient(int patientId, String column, boolean value){
        try {
            methods.editPatient(patientId, column, String.valueOf(value));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void setupTable(JTable table) {
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);
    }
}
