package Panels.TableFrames.Incoming;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeRenderer extends DefaultTableCellRenderer {

    //We return a label with the same text as the one inputed in the table but different background depending on the
    //time the patient has spent in the hospital
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //Cells are by default rendered as a JLabel.
        JLabel timeLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        //timeNowS is the time now as a string ("HH:mm")
        //timeArrival is a string ("HH:mm") of the time the patient entered the A&E
        String timeNowS = DateFormatter(LocalDateTime.now());
        String timeArrival = (String)value;

        //Time spent in hospital already
        double timeInHospital = timeDifference(timeArrival, timeNowS);

        timeLabel.setOpaque(true);

        if (timeInHospital < 2){
            timeLabel.setBackground(Color.decode("#8ABB59"));
        }

        else if (timeInHospital > 2 && timeInHospital < 3){
            timeLabel.setBackground(Color.decode("#F9D88C"));
        }

        else {
            timeLabel.setBackground(Color.decode("#F76262"));
        }

        return timeLabel;
    }


    //Transforming a LocalDateTime object into a string displaying hours and minutes in the form "HH:mm"
    public String DateFormatter(LocalDateTime time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return time.format(formatter);
    }

    //Function finding the difference in time between time now  and arrival time (time2-time1)
    public double timeDifference(String time1, String time2){

        //Hours in int
        int hour1 = findHour(time1);
        int hour2 = findHour(time2);

        //MINUTES
        int minutes1 = findMinutes(time1);
        int minutes2 = findMinutes(time2);

        double hourMinutes1 = hour1 + (double)minutes1/60;
        double hourMinutes2 = hour2 + (double)minutes2/60;

        return (hourMinutes2 - hourMinutes1);

    }

    //Finding hour from an inputted string in the form "HH:mm"
    public int findHour(String time){
        //If the hour has two digits >9 then we get the two first characters in string
        if (time.length() == 5){
            return Integer.parseInt(time.substring(0,2));
        }
        //If the hour has one digit <10 then we get only the first digit
        else if (time.length() == 4){
            return Integer.parseInt(time.substring(0,1));
        }
        else return 0;
    }

    //Finding minutes from an inputted string in the form "HH:mm"
    public int findMinutes(String time){
        //If the hour has two digits >9 then the whole time string will have length 5
        if (time.length() == 5){
            return Integer.parseInt(time.substring(3,5));
        }
        //If the hour has one digits <10 then the whole time string will have length 4
        else if (time.length() == 4){
            return Integer.parseInt(time.substring(2,4));
        }
        else return 0;
    }

}
