import Client.Client;
import com.google.gson.Gson;
import Client.Patient;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws IOException {

        Client c = new Client();
        //Patient p = new Patient("REEE","F","od",false);
        //c.makePostRequest(p);

        ArrayList<String> ps = c.makeGetRequest("*","patients","id%3E40");
        Gson gson = new Gson();
        for(String s:ps) {
            Patient p = gson.fromJson(s, Patient.class);
            System.out.println(p.getNameInitials()+p.getArrivalDateTime());
        }
    }

}
