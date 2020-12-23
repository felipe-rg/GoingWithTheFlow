package Methods;

import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AMCWard extends GeneralWard implements requestable{
    public int transferNumber;

    public AMCWard(int wardId) throws IOException, SQLException {
        super(wardId);
        amcRefresh();
    }

    public void amcRefresh() throws IOException, SQLException {
        transferNumber = getTransferList(wardId).size();
    }

    public ArrayList<Patient> getTransferList(int wardId) throws IOException, SQLException {
        //String SQLstr = "SELECT id, sex, nextDestination, estimatedTimeNext, transferReqStatus, needsSideRoom FROM patients WHERE currentLocation = "+wardId+";";
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentLocation="+wardId);
        //todo cross reference with people transferring
        return client.patientsFromJson(json);
    }

    @Override
    public void makeRequest(int patientId, int idealDestination) throws IOException, SQLException {
        String SQLstr = "UPDATE patients SET transferReqStatus = "+idealDestination+" WHERE id = "+patientId+";";
        client.makePutRequest("patients", "transferReqStatus="+idealDestination, "id="+patientId);
    }
}
