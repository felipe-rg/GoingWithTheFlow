//Main Class
import Client.Client;
import Client.Patient;
import Panels.UIController;
import com.google.gson.Gson;
import sun.security.krb5.internal.crypto.Aes128;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;


public class Main {
    public static void main (String[] args) throws IOException {

        Client c = new Client();
        //c.makePostRequest("INSERT INTO patients (nameinitials,currentlocation,sex,arrivaltime,needssideroom) values ('LN','AMC2','F','15:28','TRUE')");

        ArrayList<String> ps = c.makeGetRequest("*","patients","id>2");
        Gson gson = new Gson();
        for(String s:ps) {
            Patient p = gson.fromJson(s, Patient.class);
            System.out.println(p.getId());
        }
    }

}
