package Panels.TableFrames;

import javax.swing.table.AbstractTableModel;
import java.util.Vector;

//Our own table model
class MyTableModel extends AbstractTableModel {

    //Our data
    protected Vector dataVector;        //Vector containing the actual data
    protected Vector columnIdentifiers; //Vector containing column titles

    //columnName and data is introduced as strings so we convert them to vectors
    public MyTableModel(String[] columnName, Object[][] data){
        this(convertToVector(data), convertToVector(columnName));
    }


    //We fill in the data vector with the information
    public MyTableModel(Vector data, Vector columnNames){
        setDataVector(data, columnNames);
    }

    //Converting a 2D object into vector (in our case used for Object[][] data)
    protected static Vector convertToVector(Object[][] data) {
        if (data == null)
            return null;
        Vector vector = new Vector(data.length);
        for (int i = 0; i < data.length; i++)
            vector.add(convertToVector(data[i]));
        return vector;
    }

    //Converting a String into a vector (in our case used for String[] columnName)
    protected static Vector convertToVector(Object[] data){
        if (data == null)
        return null;
        Vector vector = new Vector(data.length);
        for (int i = 0; i < data.length; i++) 
            vector.add(data[i]);
        return vector;
    }

    //Function filling datavector with the information given
    public void setDataVector(Vector data, Vector columnNames) {
        if (data == null)
            dataVector = new Vector();
        else
            dataVector = data;
        setColumnIdentifiers(columnNames);
    }

    //Setting columnIdentifiers
    public void setColumnIdentifiers(Vector columnIdentifiers) {
        this.columnIdentifiers = columnIdentifiers;
        setColumnCount(columnIdentifiers == null ? 0 : columnIdentifiers.size());
    }

    //Sets the number of columns in the table
    public void setColumnCount(int columnCount) {
        for (int i = 0; i < dataVector.size(); ++i) {
            ((Vector) dataVector.get(i)).setSize(columnCount);
        }
        if (columnIdentifiers != null)
            columnIdentifiers.setSize(columnCount);
        fireTableStructureChanged();
    }
    
    //Return number of columns
    public int getColumnCount() {
        return columnIdentifiers == null ? 0 : columnIdentifiers.size();
    }

    //Return number of rows
    public int getRowCount() {
        return dataVector.size();
    }

    //Return ColumnName from a specific column
    public String getColumnName(int columnIndex) {
        String result = "";
        if (columnIdentifiers == null)
            result = super.getColumnName(columnIndex);
        else {
            if (columnIndex < getColumnCount()) {
                //checkSize();
                Object id = columnIdentifiers.get(columnIndex);
                if (id != null)
                    result = id.toString();
                else
                    result = super.getColumnName(columnIndex);
            } else
                result = super.getColumnName(columnIndex);
        }
        return result;
    }

    //Get value from specific location of table
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector) dataVector.get(rowIndex)).get(columnIndex);
    }

    //This makes the display a checkbox instead of true/false in case we have a boolean
    public Class getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }

    //This method allows us to edit table and select which rows are editable (it will be overriden)
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    //Removing a row (the one entered as a parameter)
    public void removeRow(int row){
        dataVector.remove(row);         //Remove row from data vector
        fireTableRowsDeleted(row, row); //Updates the data
    }

    //Changing value of a table cell
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        ((Vector) dataVector.get(rowIndex)).set(columnIndex, value);
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    //Function returning patient id (whatever is at column number 1)
    public int getPatientID(int row){
        return (int) getValueAt(row, 0);
    }

}


