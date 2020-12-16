package Methods;

import Client.Client;

import java.io.IOException;
import java.util.ArrayList;

public class AMCWard extends GeneralWard implements requestable{
    public AMCWard(String wardId){
        super(wardId);
    }

    public ArrayList<Patient> getTransferList(String wardId) throws IOException {
        String SQLstr = "SELECT id, sex, nextDestination, estimatedTimeNext transferReqStatus, needsSideRoom FROM patients WHERE currentLocation = "+wardId+";";
        return client.makeGetRequest(SQLstr);
    }

    @Override
    public void makeRequest(String patientId, String idealDestination) throws IOException {
        String SQLstr = "UPDATE patients SET transferReqStatus = "+idealDestination+" WHERE id = "+patientId+";";
        client.makePutRequest(SQLstr);
    }
}
