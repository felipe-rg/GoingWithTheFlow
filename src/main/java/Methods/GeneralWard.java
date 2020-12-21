package Methods;

import Client.*;

import java.io.IOException;
import java.util.ArrayList;

public class GeneralWard {
    Client client;
    int wardId;
    public GeneralWard(int wardId){
        this.wardId = wardId;
        client = new Client();
    }

    public ArrayList<Patient> getIncomingList(int wardId) throws IOException {
        //String SQLstr = "SELECT id FROM patients WHERE nextDestination="+wardId+";";
        return client.makeGetRequest("id", "patients", "nextDestination="+wardId);
    }

    public void acceptIncoming(int patientId, int wardId) throws IOException {
        //String SQLstr = "UPDATE patients SET accepted='Yes' WHERE id = "+patientId+";";
        client.makePutRequest("patients", "accepted='Yes'", "id="+patientId);
        //FIXME: how are we tracking requests?
    }
    public void rejectIncoming(int patientId, int wardId) throws IOException {
        //String SQLstr = "UPDATE patients SET accepted=Null WHERE id = "+patientId+";";
        client.makePutRequest("patients", "accepted='No'", "id="+patientId);
        //todo: alert of rejection to location or request new ward
    }

    public void setBed(int patientId, int bedId) throws IOException {
        //String SQLstr = "UPDATE patients SET bedId="+bedId+" WHERE id = "+patientId+";";
        client.makePutRequest("patients", "bedid="+bedId, "id="+patientId);
    }

    public void discharge(int patientId) throws IOException {
        String SQLstr = "DELETE FROM patients WHERE patientid = "+patientId+";";
        client.makeDeleteRequest(SQLstr);
    }

    public ArrayList<Patient> getAllPatients(int wardId) throws IOException {
        //todo: find needed columns
        //String SQLstr = "SELECT id, bedId, diagnosis FROM patients WHERE wardId = "+wardId+";";
        return client.makeGetRequest( "id,bedId,diagnosis", "patients", "wardid="+wardId);
    }

    public void removePatient(int patientId, int bedId) throws IOException {
        //String SQLstr = "UPDATE patients SET bedId=Null, wardId=Null WHERE id "+patientId+";";
        client.makePutRequest("patients", "bedid=Null", "id="+patientId);
        //SQLstr = "UPDATE beds SET patientId=Null, occupied='No' WHERE id "+bedId+";";
        client.makePutRequest("beds", "occupied='free'", "id="+bedId);
    }

    public void editBed(int bedId, String columnId, String newVal) throws IOException {
        //String SQLstr = "UPDATE beds SET "+columnId+ " = "+newVal+" WHERE id =" +bedId+";";
        client.makePutRequest("beds", columnId+"="+newVal, "id="+bedId);
    }

    public void isBedFree(int bedId) throws IOException {
        //todo: what can the UI do? have bedIsFree which does this...
        String answer = "Yes";
        editBed(bedId, "occupied", answer);
        //todo: have bedIsntFree which does this...
        String time = "1600";
        editBed(bedId, "leavingTime", time);
    }

    public void editPatient(int patientId, String columnId, String newVal) throws IOException {
        String SQLstr = "UPDATE patients SET "+columnId+ " = "+newVal+" WHERE id =" +patientId+";";
        client.makePutRequest("patients", columnId="="+newVal, "id="+patientId);
    }
}
