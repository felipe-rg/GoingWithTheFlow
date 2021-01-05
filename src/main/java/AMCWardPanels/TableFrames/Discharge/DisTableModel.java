package AMCWardPanels.TableFrames.Discharge;

import AMCWardPanels.TableFrames.MyTableModel;

import java.util.Vector;

public class DisTableModel extends MyTableModel {
    public DisTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }

    public DisTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex==5 || columnIndex==6 || columnIndex==7 || columnIndex ==9){
            return true;
        }

        else return false;
    }
}
