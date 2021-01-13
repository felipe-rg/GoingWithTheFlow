package Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.logging.Logger;

import Client.*;

public class AandE{
    Client client;
    fromJson fJsn;

    private static final Logger log= Logger.getLogger(AandE.class.getName());

    public AandE(int wardId){
        client = new Client();
    }

    //Creates patient with essential information
    public void createPatient(Patient p) throws IOException {
        client.makePostRequest(p);
        log.info("Client has done makePostRequest");
    }
    //Checks whether patient has been added to database
    public boolean checkAddedPatient(Patient p) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "patientid='"+p.getPatientId()+"'");
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()==1) {
            return true;
        }
        else {return false;}
    }
    //Checks if there is an existing patient with the submited patientID to prevent duplicates
    public boolean checkExistingId(String pId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "patientid='"+pId+"'");
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()!=0) {
            return true;
        }
        else return false;
    }





}
