package Methods;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dateFormat {
    //Needed to format the time difference in getPatientData
    public String durationFormatter(Duration duration){
        long hours = duration.toHours();
        return String.valueOf(hours);
    }

    //Needed to format the times used in tables
    public String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }
}
