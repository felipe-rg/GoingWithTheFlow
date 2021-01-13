package CUTablePanels;

import AMCWardPanels.TableFrames.IndexRenderer;
import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import AMCWardPanels.TableFrames.TimeRenderer;
import Methods.ControlCentre;
import Methods.tableInfo.CCTableInfo.IncomingInfoData;

import javax.swing.*;
import java.awt.*;

public class IncomingTablePanel extends JPanel{

    //Table, scrollpane and tablemodel
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


    //2D Object that will contain all data to be introduced in the table
    private Object data[][];

    //Methods that we will call
    private ControlCentre methods;


    public IncomingTablePanel(ControlCentre methods){
        this.methods = methods;

        //Filling up the data objects with data from database
        IncomingInfoData incomingInfoData = new IncomingInfoData(methods.getClient());
        data = incomingInfoData.getData();

        //Instantiating tablemode, table and scrollpane
        tableModel = new MyTableModel(columnName, data);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        //Making the renderer of the Arrival at A&E column our custom TimeRenderer (in charge of changing
        //The background color
        table.getColumnModel().getColumn(5).setCellRenderer(new TimeRenderer());

        //Rendering index column so it displays it as a string (aligned to the left) for viewing purposes
        table.getColumnModel().getColumn(0).setCellRenderer(new IndexRenderer());

        //Editing table
        setupTable(table);

        //Setting layout and adding scrollpane to this tablePanel
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
