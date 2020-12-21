import Client.Client;
import com.google.gson.Gson;
import Client.Patient;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws IOException {

        Client c = new Client();
        /*Patient pi = new Patient("IB","F","leg",false);
        c.makePostRequest(pi);*/

        /*ArrayList<Patient> patients = c.makeGetRequest("*","patients","id%3E40");

        for(Patient p:patients) {
            System.out.println(p.getId()+' '+p.getNameInitials()+' '+p.getCurrentWardId()+' '+p.getCurrentBedId());
        }*/

        //c.makeDeleteRequest("patients","id%3E46");

        //c.makePutRequest("patients","nameinitials='ael'","id%3E43");

    }

}
