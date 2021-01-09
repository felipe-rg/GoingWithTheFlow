package CUTablePanels;

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

    private JTable transTable;
    private JTable disTable;


    private JScrollPane transSp;
    private JScrollPane disSp;


    private MyTableModel transTableModel;
    private MyTableModel disTableModel;

    //Columnames in our tables
    private String[] transColumnName = {"Index",
            "Ward",
            "Patient ID",
            "Sex",
            "Side Room",
            "Diagnosis",
            "New ward",
            "ETT"
    };

    private String[] disColumnName = {"Index",
            "Ward",
            "Patient ID",
            "Sex",
            "Diagnosis",
            "TTA Sign off",
            "Discharge Lounge?",
            "Discharge time",
    };

    private Object transData[][];
    private Object disData[][];


    private ControlCentre methods;


    public DisTransTablePanel(ControlCentre methods) {
        this.methods = methods;

        DischargeInfoData dischargeInfoData = new DischargeInfoData(methods.getClient());
        disData = dischargeInfoData.getData();
        TransInfoData transInfoData = new TransInfoData(methods.getClient());
        transData = transInfoData.getData();

        //Instantiating tablemodel, table and scrollpane
        disTableModel = new MyTableModel(disColumnName, disData);
        disTable = new JTable(disTableModel);
        disSp = new JScrollPane(disTable);

        //Instantiating tablemodel, table and scrollpane
        transTableModel = new MyTableModel(transColumnName, transData);
        transTable = new JTable(transTableModel);
        transSp = new JScrollPane(transTable);

        //Editing the tables so they look good
        setupTable(disTable, transTable);



        GridLayout gridLayout = new GridLayout(2,1);
        gridLayout.setVgap(50);

        this.setLayout(gridLayout);
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