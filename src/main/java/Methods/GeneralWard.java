package Methods;

import Client.*;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class GeneralWard {
    public Client client;
    private int wardId;
    private String wardName;
    public Patient patient;
    public Bed bed;


    public GeneralWard(int wardId) throws IOException {
        this.wardId = wardId;
        client = new Client();
        ArrayList<String> json = client.makeGetRequest("wardname", "wards", "id="+wardId);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        wardName = wards.get(0).getWardName();
    }

    public ArrayList<Patient> getIncomingList(int wardId) throws IOException, SQLException {
        //String SQLstr = "SELECT id FROM patients WHERE nextDestination="+wardId+";";
        ArrayList<String> json = client.makeGetRequest("id", "patients", "nextDestination="+wardId);
        return client.patientsFromJson(json);
    }

    public void acceptIncoming(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "transferrequeststatus='C'", "id="+patientId);
    }
    public void rejectIncoming(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "transferrequeststatus='D'", "id="+patientId);
        //todo: alert of rejection to location or request new ward
    }

    public void setBed(int patientId, int bedId) throws IOException, SQLException {
        client.makePutRequest("patients", "bedid="+bedId, "id="+patientId);
        ArrayList<String> json = client.makeGetRequest("wardid", "beds", "id="+bedId);
        int wardid = client.patientsFromJson(json).get(0).getId();
        client.makePutRequest("patients", "currentlocation="+wardid, "id="+patientId);
        client.makePutRequest("patients", "nextlocation=NULL", "id="+patientId);
        client.makePutRequest("beds", "occupied='occupied'", "id="+bedId);
    }

    public void discharge(int patientId) throws IOException, SQLException {
        String SQLstr = "DELETE FROM patients WHERE id="+patientId+";";
        client.makeDeleteRequest("patients", "id="+patientId);
    }

    public ArrayList<Patient> getAllPatients(int wardId) throws IOException, SQLException {
        //String SQLstr = "SELECT id, bedId, diagnosis FROM patients WHERE wardId = "+wardId+";";
        ArrayList<String> json = client.makeGetRequest( "*", "patients", "wardid="+wardId);
        return client.patientsFromJson(json);
    }

    public void removePatient(int patientId, int bedId) throws IOException, SQLException {
        //String SQLstr = "UPDATE patients SET bedId=Null, wardId=Null WHERE id "+patientId+";";
        client.makePutRequest("patients", "bedid=Null", "id="+patientId);
        //SQLstr = "UPDATE beds SET patientId=Null, occupied='No' WHERE id "+bedId+";";
        client.makePutRequest("beds", "occupied='free'", "id="+bedId);
    }

    public void editBed(int bedId, String columnId, String newVal) throws IOException, SQLException {
        //String SQLstr = "UPDATE beds SET "+columnId+ " = "+newVal+" WHERE id =" +bedId+";";
        client.makePutRequest("beds", columnId+"="+newVal, "id="+bedId);
    }

    public void isBedFree(int bedId, String answer) throws IOException, SQLException {
        editBed(bedId, "occupied", answer);
        if(answer == "Y") {
            ArrayList<String> json = client.makeGetRequest("id", "patients", "bedid=" + bedId);
            ArrayList<Patient> patients = client.patientsFromJson(json);
            int patientId = patients.get(0).getId();
            editPatient(patientId, "bedid", null);
            //TODO make next destination current destination, or other
        }
        //todo: time
        String time = "1600";
        editBed(bedId, "leavingTime", time);
    }

    public void editPatient(int patientId, String columnId, String newVal) throws IOException, SQLException {
        //String SQLstr = "UPDATE patients SET "+columnId+ " = "+newVal+" WHERE id =" +patientId+";";
        client.makePutRequest("patients", columnId="="+newVal, "id="+patientId);
    }

    public ArrayList<Patient> getPatientInfo(int patientId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*","patients", "id="+patientId);
        return client.patientsFromJson(json);
    }

    public void ripPatient(int patientId) throws IOException {
        client.makePutRequest("patients", "deceased=true", "id="+patientId);
    }

    public void emptyBed(int bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "id="+bedId);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        bed = beds.get(0);
    }

    public void filledBed(int bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "bedid="+bedId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        patient = patients.get(0);
    }

}
