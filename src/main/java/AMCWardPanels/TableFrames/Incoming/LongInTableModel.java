package AMCWardPanels.TableFrames.Incoming;

import AMCWardPanels.TableFrames.MyTableModel;

import java.util.Vector;

public class LongInTableModel extends MyTableModel {

    //Constructors super()
    public LongInTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }
    public LongInTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        //We make delete button, side room and request status editable
        if (columnIndex == 8 || columnIndex == 4 || columnIndex ==6){return true;}

        //If request status is "C" (Confirmed) then we can click select bed button
        if (columnIndex==7 && this.getValueAt(rowIndex, 6).equals("C")){
            return true;
        }

        else return false;

    }
}
