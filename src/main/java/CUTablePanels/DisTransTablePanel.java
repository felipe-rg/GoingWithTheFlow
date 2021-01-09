package CUTablePanels;

import AMCWardPanels.TableFrames.IndexRenderer;
import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Methods.AMCWard;
import Methods.ControlCentre;
import Methods.GeneralWard;
import Methods.tableInfo.DischargeInfoData;
import Methods.tableInfo.TransInfoData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

public class DisTransTablePanel extends JPanel {

    //Tables
    private JTable transTable;
    private JTable disTable;

    //Scrollpanes where tables will be
    private JScrollPane transSp;
    private JScrollPane disSp;

    //Tablemodels
    private MyTableModel transTableModel;
    private MyTableModel disTableModel;

    //Columnames for transfer table
    private String[] transColumnName = {"Index",
            "Ward",
            "Patient ID",
            "Sex",
            "Side Room",
            "Diagnosis",
            "New ward",
            "ETT"
    };

    //Columnames for discharge table
    private String[] disColumnName = {"Index",
            "Ward",
            "Patient ID",
            "Sex",
            "Diagnosis",
            "TTA Sign off",
            "Discharge Lounge?",
            "Discharge time",
    };

    //2D Objects that will contain all data to be introduced in the table
    private Object transData[][];
    private Object disData[][];

    //Methods that we will call
    private ControlCentre methods;


    public DisTransTablePanel(ControlCentre methods) {
        this.methods = methods;

        //Filling up the data objects with data from database
        DischargeInfoData dischargeInfoData = new DischargeInfoData(methods.getClient());
        disData = dischargeInfoData.getData();
        TransInfoData transInfoData = new TransInfoData(methods.getClient());
        transData = transInfoData.getData();

        //Instantiating tablemodel, table and scrollpane (discharge)
        disTableModel = new MyTableModel(disColumnName, disData);
        disTable = new JTable(disTableModel);
        disSp = new JScrollPane(disTable);

        //Instantiating tablemodel, table and scrollpane (transfer)
        transTableModel = new MyTableModel(transColumnName, transData);
        transTable = new JTable(transTableModel);
        transSp = new JScrollPane(transTable);

        //Editing the tables so they look good ;)
        setupTable(disTable, transTable);

        //Rendering index columns so it displays it as a string (aligned to the left) for viewing purposes
        disTable.getColumnModel().getColumn(0).setCellRenderer(new IndexRenderer());
        transTable.getColumnModel().getColumn(0).setCellRenderer(new IndexRenderer());

        //Setting layout and gap between tables
        GridLayout gridLayout = new GridLayout(2,1);
        gridLayout.setVgap(50);
        this.setLayout(gridLayout);

        //Adding scrollpanes with tables
        addSp(transSp, disSp);
    }

        //Sets rowheight and edits the tableheader
        public void setupTable (JTable...tables){
            //Editing table
            for (JTable t : tables) {
                t.setRowHeight(35);                                     //Setting rowheight
                t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
            }
        }

        //Function that adds all scrollpanes entered into this panel
        public void addSp (JScrollPane...scrollPanes){
            for (JScrollPane sp : scrollPanes) {
                this.add(sp);
            }
        }


}