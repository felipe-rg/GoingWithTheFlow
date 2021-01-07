package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Client.Ward;
import Methods.ControlCentre;
import Methods.tableInfo.AMCInfoData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AMCInfoTablePanel extends JPanel {

    private JTable AMC1Table;
    private JTable AMC2Table;
    private JTable AAUTable;


    private JScrollPane AMC1Sp;
    private JScrollPane AMC2Sp;
    private JScrollPane AAUSp;


    private MyTableModel AMC1TableModel;
    private MyTableModel AMC2TableModel;
    private MyTableModel AAUTableModel;


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

    private Object[][] dataAMC1;
    private Object[][] dataAMC2;
    private Object[][] dataAAU;

    private ControlCentre methods;

    public AMCInfoTablePanel(ControlCentre methods){
        this.methods = methods;
        ArrayList<Ward> amuWards = methods.findAMUWards();

        JPanel nuthin = new JPanel();
        nuthin.setSize(800, 200);

        GridLayout gridLayout = new GridLayout(3,1);
        gridLayout.setVgap(50);

        this.setLayout(gridLayout);

        for(Ward w:amuWards){
            AMCInfoData amcInfoData = new AMCInfoData(methods.getClient(), w.getWardId());
            Object[][] data = amcInfoData.getData();
            MyTableModel tableModel = new MyTableModel(columnName, data);
            JTable table = new JTable(tableModel);
            JScrollPane scroll = new JScrollPane(table);
            setupTable(table);
            this.addSp(scroll);
        }
    }

    //Sets rowheight and edits the tableheader
    public void setupTable(JTable... tables){
        //Editing table
        for (JTable t:tables){
            t.setRowHeight(35);                                     //Setting rowheight
            t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());

        }
    }

    public void addSp(JScrollPane ... scrollPanes){
        for (JScrollPane sp : scrollPanes){
            this.add(sp);
        }
    }
}
