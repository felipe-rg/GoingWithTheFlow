package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Methods.ControlCentre;
import Methods.tableInfo.LongStayInfoData;

import javax.swing.*;
import java.awt.*;

public class LongStayTablePanel extends JPanel {

    //Table, scrollpane and tablemodel
    private JTable table;
    private JScrollPane scrollPane;
    private MyTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Ward",
            "Capacity (%)",
            "Male Free Beds",
            "Female Free Beds",
            "Male/Female Free Beds",
            "Expected Male Discharges",
            "Expected Female Discharges"
    };

    //2D Object that will contain all data to be introduced in the table
    private Object data[][];

    //Methods we will call
    private ControlCentre methods;


    public LongStayTablePanel(ControlCentre methods){
        this.methods = methods;

        //Filling up the data objects with data from database
        LongStayInfoData lsInfoData = new LongStayInfoData(methods.getClient());
        data = lsInfoData.getData();

        //Instantiating tablemodel, table and scrollpane
        tableModel = new MyTableModel(columnName, data);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);
        scrollPane.setBackground(Color.WHITE);

        //Editing table
        setupTable(table);

        //Setting layout and adding scrollpane with table into the tableModel
        this.setLayout(new GridLayout());
        add(scrollPane);
    }

    //Function that edits table
    public void setupTable (JTable...tables){
        //Editing table
        for (JTable t : tables) {
            t.setRowHeight(187);                                     //Setting rowheight
            t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
        }
    }
}




