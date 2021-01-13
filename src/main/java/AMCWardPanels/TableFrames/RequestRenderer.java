package AMCWardPanels.TableFrames;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
        this class is a table renderer that renders the 'Request Status' column in the incoming list at
        LongStay wards as a label with the same  text as the one inputed in the table but different
        background depending on the status (green for confirmed, amber for pending and red for rejected).

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
            case "P":                                       //If P then we write pending and make cell amber
                requestLabel.setText("Pending");
                requestLabel.setBackground(Color.decode("#F9D88C"));
                break;
            case "C":
                requestLabel.setText("Confirmed");          //If C then we write Confirmed and make cell green
                requestLabel.setBackground(Color.decode("#8ABB59"));
                break;
            case "R":
                requestLabel.setText("Rejected");           //If R then we write Rejected and make cell red
                requestLabel.setBackground(Color.decode("#F76262"));
                break;
        }

        return requestLabel;
    }


}
