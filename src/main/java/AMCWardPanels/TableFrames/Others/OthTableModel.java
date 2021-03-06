package AMCWardPanels.TableFrames.Others;

import AMCWardPanels.TableFrames.MyTableModel;

import java.util.Vector;

public class OthTableModel extends MyTableModel {

    //Constructors super()
    public OthTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }
    public OthTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 8 || columnIndex == 5){return true;}
        else return false;
    }
}
