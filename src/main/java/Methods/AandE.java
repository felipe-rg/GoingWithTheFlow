package Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import Client.*;

public class AandE implements requestable{
    Client client;
    public AandE(int wardId){
        client = new Client();
    }

    public void createPatient(String initials, String sex, String initialDiagnosis, boolean sideroom) throws IOException, SQLException {
        //todo fit this in with UI
        Patient p = new Patient(initials, sex, initialDiagnosis, sideroom);
        client.makePostRequest(p);
    }

    //FIXME not needed if request automatic
    @Override
    public void makeRequest(int patientId, String idealDestination) throws IOException, SQLException {
        //String SQLstr = "UPDATE patients SET transferReqStatus = "+idealDestination+" WHERE id = "+patientId+";";
        client.makePutRequest("patients", "transferReqStatus="+idealDestination, "id="+patientId);
    }
}
