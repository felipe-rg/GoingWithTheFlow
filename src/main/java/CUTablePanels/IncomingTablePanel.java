package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import AMCWardPanels.TableFrames.TimeRenderer;
import Methods.AMCWard;
import Methods.ControlCentre;
import Methods.GeneralWard;
import Methods.tableInfo.IncomingInfoData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class IncomingTablePanel extends JPanel{

    private JTable table;
    private JScrollPane scrollPane;
    private MyTableModel tableModel;

    //Columnames in our tables
    private String[] columnName = {"Index",
            "Patient ID",
            "Sex",
            "Initial Diagnosis",
            "Side Room",
            "Arrival at A&E",
            "Accepted by Medicine",
    };


    //Data that will be inputed into table
    private Object data[][];

    private ControlCentre methods;




    public IncomingTablePanel(ControlCentre methods){

        this.methods = methods;
        IncomingInfoData incomingInfoData = new IncomingInfoData(methods.getClient());
        data = incomingInfoData.getData();
        //Filling the data object with incoming patients data from database

        tableModel = new MyTableModel(columnName, data);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        //Making the renderer of the Arrival at A&E column our custom TimeRenderer (in charge of changing
        //The background color
        table.getColumnModel().getColumn(5).setCellRenderer(new TimeRenderer());

        setupTable(table);

        this.setLayout(new GridLayout());
        add(scrollPane);

    }

    //Sets rowheight and edits the tableheader
    public void setupTable (JTable...tables){
        //Editing table
        for (JTable t : tables) {
            t.setRowHeight(35);                                     //Setting row height
            t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
        }
    }
}
