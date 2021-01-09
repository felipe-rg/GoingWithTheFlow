import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class Main {


    public static void main(String[] args) throws IOException {

        LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));

        try{

            UserPage user = new UserPage();


        }
        catch (Exception e){ }
    }
}

