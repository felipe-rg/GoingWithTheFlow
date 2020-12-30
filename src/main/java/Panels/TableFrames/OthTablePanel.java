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

public class OthTablePanel extends JPanel implements TableModelListener {

    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private OthTableModel tableModel;


    //Columnames in our table
    private String[] columnName = {"Bed Num",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "ETT",
            "Destination",
            "Delete Button"
    };

    //This is how we will receive the date from the database
    //We need to input it into the table as doing dateFormatter first like i did below
    LocalDateTime localDateTime1 = LocalDateTime.of(0, Month.JULY, 1, 16, 00, 0);
    LocalDateTime localDateTime2 = LocalDateTime.of(0, Month.JULY, 1, 19, 30, 0);
    LocalDateTime localDateTime3 = LocalDateTime.of(0, Month.JULY, 1, 20, 20, 0);

    //Data in each of the rows of our table
    private Object[][] data = {
            {1, 166128, "M", "Asthma", false, dateFormatter(localDateTime1), "Intensive Care", "Delete Patient"},
            {2, 234134, "F", "Internal Bleeding", true, dateFormatter(localDateTime2), "TBD", "Delete Patient"} ,
            {3, 356456, "M", "Fracture", true, dateFormatter(localDateTime3), "Intensive Care", "Delete Patient"}
    };


    public OthTablePanel(){
        //Instantiating table with appropriate data and tablemodel

        tableModel = new OthTableModel(columnName, data);        //Instance of OthtableModel
        table = new JTable(tableModel);         //Creating a table of model tablemodel (instance of MyTableModel)
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)


        //Editing table
        table.setRowHeight(35);                                     //Setting rowheight
        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Verdana", Font.PLAIN, 15));   //Setting tableheader font

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);

        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel);
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
}
