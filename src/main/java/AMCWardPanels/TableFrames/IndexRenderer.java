package AMCWardPanels.TableFrames;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

//This basically renders the first column of every table (the index) as a string so it is aligned to the left
public class IndexRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Cells are rendered as labels
        JLabel indexLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //Setting text as a string
        indexLabel.setText(String.valueOf(value));

        return indexLabel;
    }
}
