import Client.Client;

import Client.Ward;
import com.google.gson.Gson;
import Client.Patient;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main (String[] args) throws IOException {
        Gson gson = new Gson();
        Client c = new Client();
        ArrayList<Ward> wards = new ArrayList<Ward>();
        ArrayList<String> jsonStrings = c.makeGetRequest("*","wards","wardid%3E0");
        for (String s:jsonStrings){
            Ward w = gson.fromJson(s,Ward.class);
            wards.add(w);
        }
        for (Ward w:wards){
            System.out.println(w.getWardName());
        }


    }

}
