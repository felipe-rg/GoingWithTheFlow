package Panels.TableFrames;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;

public class DisTablePanel extends JPanel implements TableModelListener {

    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private DisTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Bed num",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "TTA Done?",
            "Discharge Lounge",
            "ETD",
            "Delete Button"};


    //This is how we will receive the date from the database
    //We need to input it into the table as doing dateFormatter first like i did below
    LocalDateTime localDateTime1 = LocalDateTime.of(0, Month.JULY, 1, 9, 00, 0);
    LocalDateTime localDateTime2 = LocalDateTime.of(0, Month.JULY, 1, 10, 07, 0);
    LocalDateTime localDateTime3 = LocalDateTime.of(0, Month.JULY, 1, 12, 20, 0);



    //Data in each of the rows of our table
    private Object[][] data = {
            {1, 166128, "M", "Asthma", false, true, true, dateFormatter(localDateTime1), "Delete Patient"},
            {2, 234134, "F", "Internal Bleeding", true, false, false, dateFormatter(localDateTime2), "Delete Patient"},
            {3, 356456, "M", "Fracture", false, false, true, dateFormatter(localDateTime3), "Delete Patient"}
    };


    public DisTablePanel(){
        //Instantiating table with appropriate data and tablemodel

        tableModel = new DisTableModel(columnName, data);        //Instance of MytableModel
        table = new JTable(tableModel);         //Creating a table of model tablemodel (instance of MyTableModel)
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)

        //Editing table
        setupTable(table);

        //Action happening when we press delete button
        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel);
            }
        };
        //Assigning the column that will have the delete buttons
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 8);

        this.setLayout(new GridLayout());
        this.add(scrollPane);
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
        //Bednumber of row selected
        Object bedNum = tableModel.getValueAt(row, 0);

        //Printing out what has been edited
        System.out.println("Patient bed: " + bedNum + "     Edited '" + columnName+ "': " +data);
    }

    private Object dateFormatter(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    public void setupTable(JTable table){
        table.setRowHeight(35);                                     //Setting rowheight
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());  //Setting header renderer

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);

    }
}