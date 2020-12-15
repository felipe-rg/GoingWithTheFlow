//Main Class
import Client.Client;
import Panels.UIController;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.sql.Time;


public class Main {
    public static void main (String[] args) throws IOException {

        Client c = new Client();
        boolean n = true;
        c.makePostRequest("INSERT INTO patients (nameinitials,currentlocation,sex,arrivaltime) values ('NV','Limas','M','04:05')");
        //c.makeGetRequest("SELECT * from patients where id=1");






    }

}
