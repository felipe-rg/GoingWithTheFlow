package Panels.TableFrames;

import java.util.Vector;

public class DisTableModel extends MyTableModel{
    public DisTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }

    public DisTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex==4 || columnIndex==5 || columnIndex==6 || columnIndex ==8){
            return true;
        }

        else return false;
    }
}
