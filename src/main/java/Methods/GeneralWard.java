package Methods;

import Client.Client;

import java.io.IOException;
import java.util.ArrayList;

public class GeneralWard {
    Client client;
    String wardId;
    public GeneralWard(String wardId){
        this.wardId = wardId;
        client = new Client();
    }

    public ArrayList<Patient> getIncomingList(String wardId) throws IOException {
        String SQLstr = "SELECT id FROM patients WHERE wardId="+wardId+";";
        return client.makeGetRequest(SQLstr);

    }

    public void acceptIncoming(String patientId, String wardId) throws IOException {
        String SQLstr = "UPDATE patients SET accepted='Yes' WHERE id = "+patientId+";";
        client.makePutRequest(SQLstr);
    }
    public void rejectIncoming(String patientId, String wardId) throws IOException {
        String SQLstr = "UPDATE patients SET accepted=Null WHERE id = "+patientId+";";
        client.makePutRequest(SQLstr);
        //todo: alert of rejection to location or request new ward
    }

    public void setBed(String patientId, String bedId) throws IOException {
        //todo: check which table we edit here
        String SQLstr = "UPDATE patients SET bedId="+bedId+" WHERE id = "+patientId+";";
        client.makePutRequest(SQLstr);
        SQLstr = "UPDATE beds SET occupied='Occupied' WHERE id = "+bedId+";";
        client.makePutRequest(SQLstr);
    }

    public void discharge(String patientId) throws IOException {
        String SQLstr = "DELETE FROM patients WHERE patientid = "+patientId+";";
        client.makeDeleteRequest(SQLstr);
    }

    public ArrayList<Patient> getAllPatients(String wardId) throws IOException {
        //todo: find needed columns
        String SQLstr = "SELECT id, bedId, diagnosis FROM patients WHERE wardId = "+wardId+";";
        return client.makeGetRequest(SQLstr);
    }

    public void removePatient(String patientId, String bedId) throws IOException {
        String SQLstr = "UPDATE patients SET bedId=Null, wardId=Null WHERE id "+patientId+";";
        client.makePutRequest(SQLstr);
        SQLstr = "UPDATE beds SET patientId=Null, occupied='No' WHERE id "+bedId+";";
        client.makePutRequest(SQLstr);
    }

    public void editBed(String bedId, String columnId, String newVal) throws IOException {
        String SQLstr = "UPDATE beds SET "+columnId+ " = "+newVal+" WHERE id =" +bedId+";";
        client.makePutRequest(SQLstr);
    }

    public void isBedFree(String bedId) throws IOException {
        //todo: what can the UI do? have bedIsFree which does this...
        String answer = "Yes";
        editBed(bedId, "occupied", answer);
        //todo: have bedIsntFree which does this...
        String time = "1600";
        editBed(bedId, "leavingTime", time);
    }

    public void editPatient(String patientId, String columnId, String newVal) throws IOException {
        String SQLstr = "UPDATE patients SET "+columnId+ " = "+newVal+" WHERE id =" +patientId+";";
        client.makePutRequest(SQLstr);
    }
}
