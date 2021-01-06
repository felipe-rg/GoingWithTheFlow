package Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

import Client.*;

public class AandE{
    Client client;
    fromJson fJsn;
    public AandE(int wardId){
        client = new Client();
    }

    //Creates patient with essential information
    public void createPatient(Patient p) throws IOException {
        client.makePostRequest(p);
    }

    public boolean checkAddedPatient(Patient p) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "patientid='"+p.getPatientId()+"'");
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()==1) {
            return true;
        }
        else {return false;}
    }

    public boolean checkExistingId(String pId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "patientid='"+pId+"'");
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()!=0) {
            return true;
        }
        else return false;
    }





}
