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

        Log log = new Log();
        test logTest = new test();
        UserPage user = new UserPage();

        /*Client c = new Client();
        LocalDate d = LocalDate.of(2005,1,18);
        //Patient p = new Patient("1111111111","Female",d,"eye pain",true);
        //c.makePostRequest(p);
        Gson gson = new Gson();
        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<String> j = c.makeGetRequest("*","patients","currentwardid=1");
        for(String s:j){
            Patient p = gson.fromJson(s,Patient.class);
            patients.add(p);
        }
        catch (Exception e){ }




    }
        /*Client c = new Client();
        LocalDate d = LocalDate.of(2005,1,18);
        //Patient p = new Patient("1111111111","Female",d,"eye pain",true);
        //c.makePostRequest(p);
        Gson gson = new Gson();
        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<String> j = c.makeGetRequest("*","patients","currentwardid=1");
        for(String s:j){
            Patient p = gson.fromJson(s,Patient.class);
            patients.add(p);
        }
        for(Patient r:patients){
            System.out.println(r.getPatientId()+r.getDateOfBirth());
        }*/
    }

