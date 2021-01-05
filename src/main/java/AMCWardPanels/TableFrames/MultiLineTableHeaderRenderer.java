package AMCWardPanels.TableFrames;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/*
      This class renders the tableheader as a JTextArea so we can change some stuff (the most important
      thing is to make the tableheader two lines high so if the header information is too long, it is not
      cut.
 */
public class MultiLineTableHeaderRenderer extends JTextArea implements TableCellRenderer
{

    public MultiLineTableHeaderRenderer() {
        //We make the JTextArea non-editable or focusable, and the same color and looks as the TableHeader
        setEditable(false);
        setLineWrap(true);
        setOpaque(false);
        setFocusable(false);
        setWrapStyleWord(true);
        LookAndFeel.installBorder(this, "TableHeader.cellBorder");
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        int width = table.getColumnModel().getColumn(column).getWidth();
        setText((String)value);
        setSize(width, getPreferredSize().height);
        setRows(2);             //This is the most important part of it all
        setFont(new Font("Verdana", Font.PLAIN, 14));
        return this;
    }
}