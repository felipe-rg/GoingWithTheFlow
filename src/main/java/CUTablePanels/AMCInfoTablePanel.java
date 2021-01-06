package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Methods.ControlCentre;
import Methods.tableInfo.AMCInfoData;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

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
            "Expected Male Discharge",
            "Expected Female Discharge",
            "ICU Transfer",
            "RIP"
    };

    private Object[][] dataAMC1;
    private Object[][] dataAMC2;
    private Object[][] dataAAU;

    private ControlCentre methods;

    public AMCInfoTablePanel(ControlCentre methods){
        AMCInfoData amcInfoData = new AMCInfoData(methods.getClient());
        dataAMC1 = amcInfoData.getData();

        //Instantiating table with appropriate data and tablemodel

        AMC1TableModel = new MyTableModel(columnName, dataAMC1);        //Instance of MytableModel for AMC1
        AMC2TableModel = new MyTableModel(columnName, dataAMC1);        //Instance of MytableModel for AMC2
        AAUTableModel = new MyTableModel(columnName, dataAMC1);        //Instance of MytableModel for AAU

        AMC1Table = new JTable(AMC1TableModel);         //Creating a table of model AMC1Tablemodel (instance of MyTableModel)
        AMC2Table = new JTable(AMC2TableModel);
        AAUTable = new JTable(AAUTableModel);

        AMC1Sp = new JScrollPane(AMC1Table);    //Creating scrollpane where table is located (for viewing purposes)
        AMC2Sp = new JScrollPane(AMC2Table);
        AAUSp = new JScrollPane(AAUTable);

        //Editing table
        setupTable(AMC1Table, AMC2Table, AAUTable);

        JPanel nuthin = new JPanel();
        nuthin.setSize(800, 200);

        GridLayout gridLayout = new GridLayout(3,1);
        gridLayout.setVgap(50);

        //Adding stuff to the JPanel (this class itself)
        this.setLayout(gridLayout);
        this.addSp(AMC1Sp, AMC2Sp, AAUSp);
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
