package Methods;

import java.io.IOException;
import java.sql.Time;
import Client.Client;

public class AandE implements requestable{
    Client client;
    public AandE(){
        client = new Client();
    }

    public void createPatient(String sex, Time arrivalTime, String initialDiagnosis) throws IOException {
        String SQLstr = "INSERT INTO patients(currentLocation, acceptedByMedicine, sex, arrivalTime, initialDiagnosis) VALUES('AandE', false, '"+sex+"', "+arrivalTime+", '"+initialDiagnosis+"');";
        client.makePostRequest(SQLstr);
    }

    @Override
    public void makeRequest(int patientId, String idealDestination) throws IOException {
        String SQLstr = "UPDATE patients SET transferReqStatus = "+idealDestination+" WHERE id = "+patientId+";";
        client.makePutRequest(SQLstr);
    }
}
