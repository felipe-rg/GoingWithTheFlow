package CUTablePanels;

import AMCWardPanels.TableFrames.MyTableModel;
import Methods.ControlCentre;

import javax.swing.*;

public class LongStayTablePanel extends JPanel {

    private JTable table;
    private JScrollPane scrollPane;
    private MyTableModel tableModel;

    //Columnames in our table
    private String[] columnName = {"Ward",
            "Capacity",
            "Male Free Beds",
            "Female Free Beds",
            "Male/Female Free Beds",
            "Expected Male Discharges",
            "Expected Female Discharges"
    };

    private Object data[][];

    private ControlCentre methods;

    public LongStayTablePanel(ControlCentre methods){

        this.methods = methods;



    }
}




