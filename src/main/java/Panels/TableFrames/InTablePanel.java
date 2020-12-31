package Panels.TableFrames;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;


public class InTablePanel extends JPanel implements TableModelListener {
    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private InTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "Arrival at A&E",
            "Accepted by Medicine",
            "Bed",
            "Delete Button"};


    //This is how we will receive the time from the database (LocalDateTime)
    LocalDateTime localDateTime1 = LocalDateTime.of(0, Month.JULY, 1, 17, 00, 0);
    LocalDateTime localDateTime2 = LocalDateTime.of(0, Month.JULY, 1, 19, 00, 0);
    LocalDateTime localDateTime3 = LocalDateTime.of(0, Month.JULY, 1, 14, 20, 0);
    //We will input the time into the table as a string so we do dateFormatter first like i did below


    //Data in each of the rows of our table
    private Object[][] data = {
            {1, 166128, "M", "Asthma", false, dateFormatter(localDateTime1), true, "Select Bed", "Delete Patient"},
            {2, 234134, "F", "Internal Bleeding", true, dateFormatter(localDateTime2), false, "Select Bed", "Delete Patient"},
            {3, 356456, "M", "Fracture", false, dateFormatter(localDateTime3), true, "Select Bed", "Delete Patient"}
    };




    //Constructor
    public InTablePanel() {

        //Instantiating table with appropriate data and tablemodel

        tableModel = new InTableModel(columnName, data);        //Instance of IntableModel extending from MyTableModel
        table = new JTable(tableModel);         //Creating a table of model tablemodel
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)



        //Editing table
        setupTable(table);


        //Action to occur when the delete button is clicked
        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel);
            }
        };


        //Action for the select bed button
        Action selectBedAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("'Select Bed' button clicked");
            }
        };


        //Creating button column instancess and assigning them to column 7 and 8
        ButtonColumn selectBed = new ButtonColumn(table, selectBedAction, 7);
        ButtonColumn deletePatient = new ButtonColumn(table, deletePopUp, 8);

        //Making the renderer of the Arrival at A&E column our custom TimeRenderer (in charge of changing
        //The background color
        table.getColumnModel().getColumn(5).setCellRenderer(new TimeRenderer());


        this.setLayout(new GridLayout());
        this.add(scrollPane);
    }


    //Method Noticing table changed and printing what changed
    @Override
    public void tableChanged(TableModelEvent e) {
        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (InTableModel)e.getSource();   //Tablemodel used

        //Name of the column and data introduced
        String columnName = tableModel.getColumnName(column);
        Object data = tableModel.getValueAt(row, column);
        //Bednumber of row selected
        Object bedNum = tableModel.getValueAt(row, 0);

        //Printing out what has been edited
        System.out.println("Patient bed: " + bedNum + "     Edited '" + columnName+ "': " +data);

        /*
            The button to assign a bed will only be clickable if the patient has been accepted by medicine.
            So every time a cell is edited we call the method isCellEditable that cheks whether the patient
            has been accepted by medicine or not. If they have been accepted by medicine, then the button
            can be clicked, if not then it does not work.
         */
        tableModel.isCellEditable(row, column);
    }

    //Transforming a LocalDateTime object into a string displaying hours and minutes in the form "HH:mm"
    public String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }


    public void setupTable(JTable table) {
        //Setting rowheight
        table.setRowHeight(35);
        //Setting header renderer with our clas MultiLineTableHeaderRenderer() (it basically edits header a lilbit)
        table.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());

        //Adding a listener to see user edits
        table.getModel().addTableModelListener(this);
    }
}


