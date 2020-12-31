package Panels.TableFrames;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class MultiLineTableHeaderRenderer extends JTextArea implements TableCellRenderer
{
    public MultiLineTableHeaderRenderer() {
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
        setRows(2);
        setFont(new Font("Verdana", Font.PLAIN, 14));
        return this;
    }
}