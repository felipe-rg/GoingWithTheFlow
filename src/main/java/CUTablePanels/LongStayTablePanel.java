package CUTablePanels;

import AMCWardPanels.TableFrames.MultiLineTableHeaderRenderer;
import AMCWardPanels.TableFrames.MyTableModel;
import Methods.ControlCentre;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;

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

        try {
            data = methods.getLongStayData();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        tableModel = new MyTableModel(columnName, data);
        table = new JTable(tableModel);
        scrollPane = new JScrollPane(table);

        setupTable(table);

        this.setLayout(new GridLayout());
        add(scrollPane);
    }

    public void setupTable (JTable...tables){
        //Editing table
        for (JTable t : tables) {
            t.setRowHeight(35);                                     //Setting rowheight
            t.getTableHeader().setDefaultRenderer(new MultiLineTableHeaderRenderer());
        }
    }
}




