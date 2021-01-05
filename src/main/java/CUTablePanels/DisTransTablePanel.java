package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Methods.AMCWard;
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
    private String[] transColumnName = {"Patient ID",
            "Sex",
            "Side Room",
            "Diagnosis",
            "New ward",
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


    private GeneralWard disMethods;
    private AMCWard transMethods;

    public DisTransTablePanel(GeneralWard disMethods, AMCWard transMethods) {
        this.disMethods = disMethods;
        this.transMethods = transMethods;

        try {
            disData = disMethods.getCUDisData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        disTableModel = new MyTableModel(disColumnName, disData);
        disTable = new JTable(disTableModel);
        disSp = new JScrollPane(disTable);

        setupTable(disTable);

        this.setLayout(new GridLayout());
        this.add(disSp);

    }


    //Sets rowheight and edits the tableheader
    public void setupTable(JTable... tables) {
        //Editing table
        for (JTable t : tables) {
            t.setRowHeight(35);                                     //Setting rowheight
            t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
        }
    }

    public void addSp(JScrollPane... scrollPanes) {
        for (JScrollPane sp : scrollPanes) {
            this.add(sp);
        }
    }

}