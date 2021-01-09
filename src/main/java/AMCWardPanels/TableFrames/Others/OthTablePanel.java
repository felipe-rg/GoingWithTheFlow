package AMCWardPanels.TableFrames.Others;

import AMCWardPanels.TableFrames.ButtonColumn;
import AMCWardPanels.TableFrames.DeletePopUp;
import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import Methods.GeneralWard;
import Methods.tableInfo.OtherTableData;

import javax.print.attribute.standard.MediaSize;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OthTablePanel extends JPanel implements TableModelListener {

    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private OthTableModel tableModel;


    //Columnames in our table
    private String[] columnName = {"Index",
            "Bed ID",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "ETT",
            "Destination",
            "Delete Button"
    };


    private Object[][] dbData;

    private GeneralWard methods;

    public OthTablePanel(GeneralWard methods){
        OtherTableData otherTableData = new OtherTableData(methods.getClient(), methods.getWardId());
        this.methods = methods;
        dbData = otherTableData.getData();


        //Instantiating table with appropriate data and tablemodel
        tableModel = new OthTableModel(columnName, dbData);        //Instance of OthtableModel
        table = new JTable(tableModel);         //Creating a table of model tablemodel (instance of MyTableModel)
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)


        //Editing table
        setupTable(table);

        //Delete action
        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel, methods);
            }
        };

        //Making last column a buttoncolumn (to delete patient)
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 8);

        //Setting layout and adding scrollpan with table
        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }

    //Function that
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
    //Function prints
    public void tableChanged(TableModelEvent e) {
        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (OthTableModel)e.getSource();   //Tablemodel used

        int patientId = tableModel.getPatientID(table.getSelectedRow());

        //Name of the column and data introduced
        String columnName = tableModel.getColumnName(column);
        Object data = tableModel.getValueAt(row, column);
        //Bednumber of row selected
        Object bedNum = tableModel.getValueAt(row, 0);

        //Printing out what has been edited
        System.out.println("Patient bed: " + bedNum + "     Edited '" + columnName+ "': " +data);


        //Editing the sideroom database value
        if(columnName == "Side Room"){
            editPatient(patientId, "needssideroom", (boolean)data);
        }
    }

    public void setupTable(JTable table) {
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);
    }
}
