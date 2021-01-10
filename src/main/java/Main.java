import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) throws IOException {

        // set up log manager to record the platform's performance
        LogManager.getLogManager().readConfiguration(new FileInputStream("logging.properties"));

        UserPage user = new UserPage();         // opens the User Page of platform

    }
}

