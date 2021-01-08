package AMCWardPanels.TableFrames.Incoming;

import AMCWardPanels.TableFrames.*;
import AMCWardPanels.WardInfo;
import Client.*;
import Methods.GeneralWard;
import Methods.tableInfo.IncomingInfoData;
import Methods.tableInfo.IncomingTableData;

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
    private MyTableModel tableModel;

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

    private String[] lsColumnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "Estimated Time Arrival", //Remove Colour
            "Transfer Request Status", //String, P = pending, C = confirmed, R = rejected
            "Bed",
            "Delete Button"};

    private Object[][] dbData;

    private GeneralWard methods;

    //Constructor
    public InTablePanel(GeneralWard methods) {
        this.methods = methods;

        //
        IncomingTableData incomingTableData = new IncomingTableData(methods.getClient(), methods.getWardId());
        dbData = incomingTableData.getData();
        try {
            if (methods.getWardType(methods.getWardId()).equals("AMU")){
                tableModel = new InTableModel(amcColumnName, dbData);        //Instance of IntableModel extending from MyTableModel
            }
            else {
                tableModel = new LongInTableModel(lsColumnName, dbData);
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    private void editPatient(int patientId, String column, String value){
        try {
            methods.editPatient(patientId, column, value);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    private void selectBed(int patientId){
        Client client = new Client();
        JFrame infoFrame = new JFrame();
        ArrayList<Bed> acceptableBeds = new ArrayList<Bed>();
        try {
            acceptableBeds = methods.getAcceptableBeds(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(acceptableBeds.size()!=0){
            infoFrame.setSize(300,300);
            infoFrame.setBackground(Color.WHITE);
            infoFrame.setVisible(true);
            infoFrame.setLocation(300,300);
            infoFrame.setLayout(new GridLayout(acceptableBeds.size() + 1, 2));
            ButtonGroup beds = new ButtonGroup();
            for (Bed w : acceptableBeds) {
                JRadioButton lsWard = new JRadioButton(String.valueOf(w.getBedId()));
                lsWard.setActionCommand(String.valueOf(w.getBedId()));
                lsWard.setFont(new Font("Verdana", Font.PLAIN, 20));
                beds.add(lsWard);
                infoFrame.add(lsWard);
            }
            JButton submitWard = new JButton("Submit");
            infoFrame.add(submitWard);
            submitWard.addActionListener(evt -> {
                String selected = beds.getSelection().getActionCommand();
                try {
                    Bed bed = methods.getBed(selected);
                    methods.setBed(patientId, bed.getBedId());
                    //update specific bed
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                tableModel.removeRow(table.getSelectedRow());
                infoFrame.dispose();
            });
        } else {
            infoFrame.setSize(300,100);
            infoFrame.setBackground(Color.WHITE);
            infoFrame.setVisible(true);
            infoFrame.setLocation(300,300);
            JLabel noBeds = new JLabel("Currently No Acceptable Free Beds");
            noBeds.setHorizontalAlignment(JLabel.CENTER);
            infoFrame.add(noBeds);
        }

    }


    //Method Noticing table changed and printing what changed
    @Override
    public void tableChanged(TableModelEvent e) {
        //Row and column being edited
        int row = e.getFirstRow();
        int column = e.getColumn();
        tableModel = (MyTableModel)e.getSource();


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
            editPatient(patientId, "transferrequeststatus", "'"+ data+"'");
        }

        /*
            In the AMC button to assign a bed will only be clickable if the patient has been accepted by medicine.
            So every time a cell is edited we call the method isCellEditable that cheks whether the patient
            has been accepted by medicine or not. If they have been accepted by medicine, then the button
            can be clicked, if not then it does not work.

            Similarly for Longstay Wards, if the request status is C (Confirmed) the cell select bed  will be
            editable, if it is P (Pending) or R (Rejected) then the select bed button cannot be clicked.
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



