package AMCWardPanels.TableFrames.Transfer;

import AMCWardPanels.TableFrames.MyTableModel;

import java.util.Vector;

public class TransTableModel extends MyTableModel {

    //Constructors super()
    public TransTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }
    public TransTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex ==5 || columnIndex==8){
            return true;
        }
        else return false;
    }
}
