package Panels.TableFrames.Incoming;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*
    This class will be a renderer and an editor, we will assign in tablePanel the column of our table that we want to
    be ButtonColumn (in our case column 7 and column 8).

 */


public class ButtonColumn extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, ActionListener, MouseListener {

    private JTable table;
    private Action action;

    private JButton renderButton;
    private JButton editButton;
    private boolean isButtonColumnEditor;
    private Object editorValue;
    private Border focusBorder;

    /*
        Constructor has the following parameters
        1. The jtable we are going to make a column a buttoncolumn
        2. The action to be performed when the button is clicked
        3. The number of the column of the table that will be a columnbutton
     */
    public ButtonColumn(JTable table, Action action, int column) {
        this.table = table;
        this.action = action;
        renderButton = new JButton();
        editButton = new JButton();
        editButton.addActionListener( this );
        setFocusBorder( new LineBorder(Color.BLUE) );


        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(column).setCellRenderer( this );
        columnModel.getColumn(column).setCellEditor( this );
        table.addMouseListener( this );
    }

    public void setFocusBorder(Border focusBorder)
    {
        this.focusBorder = focusBorder;
        editButton.setBorder( focusBorder );
    }


    // If the button is clicked, we stop editing and call the action
    @Override
    public void actionPerformed(ActionEvent e) {
        //Find row being edited
        int row = table.convertRowIndexToModel(table.getEditingRow());
        //Stop editing
        fireEditingStopped();

        //Invoking the Action
        ActionEvent event = new ActionEvent(table, ActionEvent.ACTION_PERFORMED, "" + row);
        action.actionPerformed(event);
    }

    //We override these
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}


    //Editor invoked when mouse pressed
    @Override
    public void mousePressed(MouseEvent e) {
        if (table.isEditing() &&  table.getCellEditor() == this)
            isButtonColumnEditor = true;
    }

    //Making sure editing is stopped when mouse is released
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isButtonColumnEditor &&  table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
        isButtonColumnEditor = false;
    }


    //Setting the text of the button depending on what was inputed in the string list (Object[][] data)
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.editorValue = value;

        //What we will introduce in the button text depending on what is inputed in the cell
        if (value == null) {                //If cell is empty, button is empty
            editButton.setText("");
        } else {                                         //If there is text in cell, we put that text in a button
            editButton.setText(value.toString());
        }
        editButton.setIcon(null);


        return editButton;
    }

    @Override
    public Object getCellEditorValue() {
        return editorValue;
    }

    //Implementing TableCellRenderer interface (similar to getTableCellEditorComponent)
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        if (value == null) {                //If cell is empty, button is empty
            renderButton.setText("");
        } else {                                         //If there is text in cell, we put that text in a button
            renderButton.setText(value.toString());
        }
        return renderButton;
    }

}