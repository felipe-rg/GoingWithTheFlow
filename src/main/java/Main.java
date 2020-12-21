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
        AMCWard amc = new AMCWard(13);
        AandE ande = new AandE(17);
        ArrayList<String> initials = new ArrayList<String>();
        initials.add("AA");
        initials.add("AB");
        initials.add("AC");
        initials.add("AD");
        initials.add("AE");
        initials.add("AF");
        initials.add("AG");
        for (String s:initials){
            ArrayList<Patient> p = amc.getPatientInfo(s);
             amc.acceptIncoming(p.get(0).getId());
        }


    }

}
