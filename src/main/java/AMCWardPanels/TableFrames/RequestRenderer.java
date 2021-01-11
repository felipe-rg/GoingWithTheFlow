package AMCWardPanels.TableFrames;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
        this class is a table renderer that renders the 'arrival at A&E' column as a label with the same
        text as the one inputed in the table but different background depending on the time the patient
        has spent in the hospital. If <2h then green, if in between 2-3h then amber and for >3h then red.

 */

public class RequestRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Cells are rendered as labels
        JLabel requestLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //Making label opaque so we can see the color
        requestLabel.setOpaque(true);

        String request = (String)value;

        switch (request) {
            case "P":
                requestLabel.setText("Pending");
                requestLabel.setBackground(Color.decode("#F9D88C"));
                break;
            case "C":
                requestLabel.setText("Confirmed");
                requestLabel.setBackground(Color.decode("#8ABB59"));
                break;
            case "R":
                requestLabel.setText("Rejected");
                requestLabel.setBackground(Color.decode("#F76262"));
                break;
        }

        return requestLabel;
    }


}
