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
        To do this we compare the current time and the time the patient entered the hospital.

 */

public class TimeRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Cells are rendered as labels
        JLabel timeLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //Making label opaque so we can see the color
        timeLabel.setOpaque(true);

        //This is the time in hospital (rounding to lowest hour)
        long timeInHospital = durationFormatter(Duration.between((LocalDateTime)value, LocalDateTime.now()));

        //Editing text in label (the one that will be displayed in cell)
        timeLabel.setText(dateFormatter((LocalDateTime) value));

        if (timeInHospital < 2){
            timeLabel.setBackground(Color.decode("#8ABB59"));   //Making background green

        }

        if (timeInHospital >= 2 && timeInHospital < 3){
            timeLabel.setBackground(Color.decode("#F9D88C"));   //Making background amber
        }

        else if (timeInHospital >= 3){
            timeLabel.setBackground(Color.decode("#F76262"));    //Making background red
        }

        return timeLabel;
    }


    //Transforming a LocalDateTime object into a string displaying hours and minutes in the form "HH:mm"
    public String dateFormatter(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    //Return durtion as a long
    public long durationFormatter(Duration duration){
        long hours = duration.toHours();
        return hours;
    }

}
