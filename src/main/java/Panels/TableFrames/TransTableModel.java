package Panels.TableFrames;

import java.util.Vector;

public class TransTableModel extends MyTableModel{
    public TransTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }

    public TransTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex ==4  || columnIndex>=6){
            return true;
        }
        else return false;
    }
}
