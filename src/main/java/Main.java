import Client.*;
import Methods.AMCWard;
import Methods.AandE;
import Methods.ControlCentre;
import com.google.gson.Gson;
import Client.Patient;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws IOException, SQLException {

    ControlCentre methods = new ControlCentre();
    methods.getAmcCapacityPerc();
    }

}
