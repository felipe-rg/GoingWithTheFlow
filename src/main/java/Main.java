
import java.util.logging.Level;

public class Main {


    public static void main(String[] args) {

        try{
            Log log = new Log("log.txt");       // creates log class and text file to save logs
            log.logger.setLevel(Level.WARNING);         // only logs errors greater than warnings

            UserPage user = new UserPage();

            log.logger.warning("Logger is setup");
        }
        catch (Exception e){ }




    }

}
