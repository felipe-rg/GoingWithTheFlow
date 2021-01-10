package AMCWardPanels.TableFrames;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LsTimeRenderer extends DefaultTableCellRenderer {

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Cells are rendered as labels
        JLabel timeLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //Making label opaque so we can see the color
        timeLabel.setOpaque(true);

        //This is the time in hospital (rounding to lowest hour)
        long timeUntilArrival = Math.abs(durationFormatter(Duration.between((LocalDateTime)value, LocalDateTime.now())));

        //Editing text in label (the one that will be displayed in cell)
        timeLabel.setText(dateFormatter((LocalDateTime) value));

        if (timeUntilArrival < 1){
            timeLabel.setBackground(Color.decode("#F76262"));       //Making background red
        }

        if (timeUntilArrival >= 1 && timeUntilArrival <= 3){
            timeLabel.setBackground(Color.decode("#F9D88C"));   //Making background amber
        }

        else if (timeUntilArrival >= 5){
            timeLabel.setBackground(Color.decode("#8ABB59"));   //Making background green
        }


        return timeLabel;
    }


    //Transforming a LocalDateTime object into a string displaying hours and minutes in the form "HH:mm"
    public String dateFormatter(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    public long durationFormatter(Duration duration){
        long hours = duration.toHours();
        return hours;
    }
}
