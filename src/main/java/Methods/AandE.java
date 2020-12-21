package Methods;

import java.io.IOException;
import java.sql.Time;
import Client.*;

public class AandE implements requestable{
    Client client;
    public AandE(){
        client = new Client();
    }

    public void createPatient(String initials, String sex, String initialDiagnosis, boolean sideroom) throws IOException {
        //todo fit this in with UI
        Patient patient = new Patient(initials, sex, initialDiagnosis, sideroom);
        //String SQLstr = "INSERT INTO patients(currentLocation, acceptedByMedicine, sex, arrivalTime, initialDiagnosis) VALUES('AandE', false, '"+sex+"', "+arrivalTime+", '"+initialDiagnosis+"');";
        client.makePostRequest(patient);
    }

    @Override
    public void makeRequest(int patientId, String idealDestination) throws IOException {
        //String SQLstr = "UPDATE patients SET transferReqStatus = "+idealDestination+" WHERE id = "+patientId+";";
        client.makePutRequest("patients", "transferReqStatus="+idealDestination, "id="+patientId);
    }
}
