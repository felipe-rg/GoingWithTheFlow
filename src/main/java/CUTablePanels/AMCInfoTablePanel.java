package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Client.Ward;
import Methods.ControlCentre;
import Methods.tableInfo.AMCInfoData;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AMCInfoTablePanel extends JPanel {

    //Columnames in our table
    private String[] columnName = {"Ward Name",
            "Capacity (%)",
            "Male Free Beds",
            "Female Free Beds",
            "Male/Female Free Beds",
            "Male Discharge",
            "Female Discharge",
            "Male Transfers",
            "Female Transfers",
            "ICU Transfer",
            "RIP"
    };

    //Methods we will call
    private ControlCentre methods;

    public AMCInfoTablePanel(ControlCentre methods){
        this.methods = methods;

        //List of AMUWards
        ArrayList<Ward> amuWards = methods.findAMUWards();

        //We set gridlayout and a gap between tables
        GridLayout gridLayout = new GridLayout(3,1);
        gridLayout.setVgap(50);
        this.setLayout(gridLayout);

        //Loop through wards in AMUWard list and create a table for each ward
        for(Ward w:amuWards){
            //Filling out data object with data from database
            AMCInfoData amcInfoData = new AMCInfoData(methods.getClient(), w.getWardId());
            Object[][] data = amcInfoData.getData();

            //Defining tablemodel, table and scrollpane
            MyTableModel tableModel = new MyTableModel(columnName, data);
            JTable table = new JTable(tableModel);
            JScrollPane scroll = new JScrollPane(table);

            //Editing table
            setupTable(table);

            //Adding scrollpane into this tablePanel
            this.add(scroll);
        }
    }

    //Sets rowheight and edits the tableheader
    public void setupTable(JTable... tables){
        //Editing table
        for (JTable t:tables){
            t.setRowHeight(120);                                     //Setting rowheight
            t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());

        }
    }

}
