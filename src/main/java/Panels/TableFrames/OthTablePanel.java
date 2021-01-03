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

public class OthTablePanel extends JPanel implements TableModelListener {

    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private OthTableModel tableModel;


    //Columnames in our table
    private String[] columnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "ETT",
            "Destination",
            "Delete Button"
    };

    private Object[][] dbData;

    public OthTablePanel(GeneralWard methods){
        try {
            dbData = methods.getOtherData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //Instantiating table with appropriate data and tablemodel

        tableModel = new OthTableModel(columnName, dbData);        //Instance of OthtableModel
        table = new JTable(tableModel);         //Creating a table of model tablemodel (instance of MyTableModel)
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)


        //Editing table
        setupTable(table);

        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel, methods);
            }
        };


        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 7);


        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (OthTableModel)e.getSource();   //Tablemodel used

        //Name of the column and data introduced
        String columnName = tableModel.getColumnName(column);
        Object data = tableModel.getValueAt(row, column);
        //Bednumber of row selected
        Object bedNum = tableModel.getValueAt(row, 0);

        //Printing out what has been edited
        System.out.println("Patient bed: " + bedNum + "     Edited '" + columnName+ "': " +data);

    }

    //Transforming a LocalDateTime object into a string displaying hours and minutes in the form "HH:mm"
    public String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    public void setupTable(JTable table) {
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);
    }
}
