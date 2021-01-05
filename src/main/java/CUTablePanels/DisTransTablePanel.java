package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Methods.AMCWard;
import Methods.ControlCentre;
import Methods.GeneralWard;

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
            "Bed ID",
            "Patient ID",
            "Sex",
            "Side Room",
            "Diagnosis",
            "New ward",
            "ETT"
    };

    private String[] disColumnName = {"Index",
            "Bed ID",
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

        //Filling disData with all relevant data from database
        try {
            disData = methods.getDisData();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Filling transData with all relevant data from database
        try {
            transData = methods.getTransData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        disTableModel = new MyTableModel(disColumnName, disData);
        disTable = new JTable(disTableModel);
        disSp = new JScrollPane(disTable);

        transTableModel = new MyTableModel(transColumnName, transData);
        transTable = new JTable(transTableModel);
        transSp = new JScrollPane(transTable);

        setupTable(disTable, transTable);

        this.setLayout(new GridLayout(2, 1));
        addSp(disSp, transSp);
    }

        //Sets rowheight and edits the tableheader
        public void setupTable (JTable...tables){
            //Editing table
            for (JTable t : tables) {
                t.setRowHeight(35);                                     //Setting rowheight
                t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
            }
        }

        public void addSp (JScrollPane...scrollPanes){
            for (JScrollPane sp : scrollPanes) {
                this.add(sp);
            }
        }


}