import Client.*;
import Methods.AMCWard;
import Methods.AandE;
import com.google.gson.Gson;
import Client.Patient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws IOException, SQLException {

    Client c = new Client();
    AMCWard amc = new AMCWard(1);
    amc.ripPatient(43);
    }

}
