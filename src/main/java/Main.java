import Client.*;
import Methods.AMCWard;
import Methods.AandE;
import com.google.gson.Gson;
import Client.Patient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import Client.Client;
import Client.Patient;
import com.google.gson.Gson;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.logging.Level;


public class Main {


    public static void main(String[] args) throws IOException {


        try{
            Log log = new Log("log.txt");       // creates log class and text file to save logs
            log.logger.setLevel(Level.WARNING);         // only logs errors greater than warnings

            UserPage user = new UserPage();

            log.logger.warning("Logger is setup");
        }
        catch (Exception e){ }
    }
}

