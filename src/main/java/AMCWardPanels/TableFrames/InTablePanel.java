package AMCWardPanels.TableFrames;

import Client.*;
import Methods.GeneralWard;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class InTablePanel extends JPanel implements TableModelListener {
    //Table and scrollpane where table sits
    private JTable table;
    private JScrollPane scrollPane;
    private InTableModel tableModel;

    //Columnames in our table
    private String[] amcColumnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "Arrival at A&E",
            "Accepted by Medicine",
            "Bed",
            "Delete Button"};

    //Long stay ward table is slightly different
    private String[] lsColumnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "Estimated Time Arrival", //TODO remove Colour in longstay
            "Transfer Request Status",
            "Bed",
            "Delete Button"};

    private Object[][] dbData;

    private GeneralWard methods;

    //Constructor
    public InTablePanel(GeneralWard methods) {
        //Methods hold communication with database
        this.methods = methods;

        //If the ward is amc then we use amc data
        if(methods.wardId==2){
            try {
                dbData = this.methods.getIncomingData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tableModel = new InTableModel(amcColumnName, dbData);
        }
        //if the ward is not amc we use long stay data
        else {
            try {
                dbData = this.methods.getLSIncomingData();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            tableModel = new InTableModel(lsColumnName, dbData);
        }

        //Instantiating table with appropriate data and tablemodel

        table = new JTable(tableModel);         //Creating a table of model tablemodel
        scrollPane = new JScrollPane(table);    //Creating scrollpane where table is located (for viewing purposes)



        //Editing table
        setupTable(table);


        //Action to occur when the delete button is clicked
        Action deletePopUp = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DeletePopUp(table, tableModel, methods);
            }
        };


        //Action for the select bed button
        Action selectBedAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int patientID = tableModel.getPatientID(table.getSelectedRow());
                System.out.println("'Select Bed' button clicked.  PatientID: " + patientID);
                selectBed(patientID);
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

    //Used to change the patient information on the database
    private void editPatient(int patientId, String column, String value){
        try {
            methods.editPatient(patientId, column, value);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Function to assign a patient to a bed
    private void selectBed(int patientId){
        //Create array of acceptable beds (correct gender, not occupied etc)
        ArrayList<Bed> acceptableBeds = new ArrayList<Bed>();
        try {
            acceptableBeds = methods.getAcceptableBeds(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a button group, on which all beds will be assigned
        ButtonGroup beds = new ButtonGroup();

        //Create for information to be displayed on
        JFrame infoFrame = new JFrame();
        infoFrame.setSize(300,300);
        infoFrame.setBackground(Color.WHITE);
        infoFrame.setVisible(true);
        infoFrame.setLocation(300,300);
        //We need a row for each bed and a row for the submit button
        infoFrame.setLayout(new GridLayout(acceptableBeds.size()+1,1));

        //Loop all beds
        for(Bed w:acceptableBeds){
            //Create a button per bed
            JRadioButton lsWard = new JRadioButton(String.valueOf(w.getBedId()));
            //This is what is returned by ButtonGroup.getSelected()
            lsWard.setActionCommand(String.valueOf(w.getBedId()));
            //Make it look pretty
            lsWard.setFont(new Font("Verdana", Font.PLAIN, 20));
            //Add to button group
            beds.add(lsWard);
            //Add to frame
            infoFrame.add(lsWard);
        }

        // Create a submit button and add it to the frame

        JButton submitWard = new JButton("Submit");
        infoFrame.add(submitWard);

        //When submit button is pressed, we assign the bed
        submitWard.addActionListener(evt -> {
            //Retrieve our selected button
            String selected = beds.getSelection().getActionCommand();
            try {
                //Find bed
                Bed bed = methods.getBed(Integer.parseInt(selected));
                //Assign patient to bed
                methods.setBed(patientId, bed.getBedId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (SQLException throwables) {
            throwables.printStackTrace();
            }
            infoFrame.dispose();
        });

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

        int patientId = tableModel.getPatientID(table.getSelectedRow());

        //Printing out what has been edited
        System.out.println("Patient bed: " + patientId + "     Edited '" + columnName+ "': " +data);

        if(columnName == "Accepted by Medicine"){
            editPatient(patientId, "acceptedbymedicine", String.valueOf(data));
        }
        if(columnName == "Side Room"){
            editPatient(patientId, "needssideroom", String.valueOf(data));
        }
        if(columnName == "Transfer Request Status"){
            editPatient(patientId, "transferrequeststatus", "'"+data+"'");
        }

        /*
            The button to assign a bed will only be clickable if the patient has been accepted by medicine.
            So every time a cell is edited we call the method isCellEditable that cheks whether the patient
            has been accepted by medicine or not. If they have been accepted by medicine, then the button
            can be clicked, if not then it does not work.
         */
        tableModel.isCellEditable(row, column);
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


