package Panels.TableFrames;

import javax.swing.*;
import java.awt.*;

public class InTable {
    JPanel mainPanel;
    JTable table;


    public InTable(){
        JFrame frame = new JFrame();
        frame.setSize(800, 600);
        frame.setTitle("Incoming Patients");
        //frame.setResizable(false);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        String[] columnNames = {"First Name",
                "Last Name",
                "Sport",
                "# of Years",
                "Vegetarian"};

        Object[][] data = {
                {"Kathy", "Smith",
                        "Snowboarding", new Integer(5), new Boolean(false)},
                {"John", "Doe",
                        "Rowing", new Integer(3), new Boolean(true)},
                {"Sue", "Black",
                        "Knitting", new Integer(2), new Boolean(false)},
                {"Jane", "White",
                        "Speed reading", new Integer(20), new Boolean(true)},
                {"Joe", "Brown",
                        "Pool", new Integer(10), new Boolean(false)}
        };

        table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(table.getTableHeader(), BorderLayout.PAGE_START);
        mainPanel.add(table, BorderLayout.CENTER);
        table.setRowHeight(50);

        /*
        //Creating table
        String[] columnNames = { "Bed ID", "Patient ID", "Sex", "Initial Diagnosis",
                "Side Room", "Arrival at A&E", "Accepted by Medicine?", "Bed"};
        String[] data = {"1", "066128", "M", "Asthma", "No", "14:00", "Yes", "____"};


        table = new JTable(data, columnNames);
        table.setRowHeight(50);

        */
        //mainPanel.add(table);







        frame.add(mainPanel);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
