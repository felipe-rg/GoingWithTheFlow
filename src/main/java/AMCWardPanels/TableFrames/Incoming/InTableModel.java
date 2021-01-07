package AMCWardPanels.TableFrames.Incoming;

import AMCWardPanels.TableFrames.MyTableModel;

import java.util.Vector;

public class InTableModel extends MyTableModel {

    public InTableModel(String[] columnName, Object[][] data) {
        super(columnName, data);
    }

    public InTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    //Overriding the parent's isCellEditable method
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex<4 || columnIndex==5){
            return false;
        }
        //If the checkbox "accepted by medicine" has been clicked, then we can click the select bed button
        if (columnIndex==7 && (boolean)this.getValueAt(rowIndex, 6)==false){
            return false;
        }
        else return true;
    }

}