package AMCWardPanels.TableFrames;

import java.util.Vector;

public class OthTableModel extends MyTableModel{
    public OthTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }

    public OthTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 5 || columnIndex==7 || columnIndex==8){
            return true;
        }
        else return false;
    }
}
