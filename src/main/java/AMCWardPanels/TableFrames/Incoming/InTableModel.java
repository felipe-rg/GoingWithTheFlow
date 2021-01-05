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

        if (columnIndex==7 && (boolean)this.getValueAt(rowIndex, 6)==false){
            return false;
        }
        else return true;
    }


        /*
        if (this.getValueAt(rowIndex, 6).getClass() == boolean.class) {
            //If the checkbox "accepted by medicine" has been clicked, then we can click the button
            if (columnIndex == 7 && (boolean) this.getValueAt(rowIndex, 6) == false) {
                return false;
            }
            else return true;
        }
        else return false;
    }






        if(this.getValueAt(rowIndex, 7).getClass() == String.class){
            //If the checkbox "accepted by medicine" has been clicked, then we can click the button
            if (columnIndex == 7 && String.valueOf(this.getValueAt(rowIndex, 7)) != "C") {
                return false;
            }
            else return true;
        }

         */
        //else{ return true;}

}
