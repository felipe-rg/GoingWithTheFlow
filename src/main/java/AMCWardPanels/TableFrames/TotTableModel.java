package AMCWardPanels.TableFrames;

import java.util.Vector;

public class TotTableModel extends MyTableModel{
    public TotTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }

    public TotTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex==5 || columnIndex==8){
            return true;
        }

        else return false;
    }
}
