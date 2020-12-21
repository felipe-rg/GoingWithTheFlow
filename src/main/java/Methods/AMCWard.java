package Methods;

import Client.*;

import java.io.IOException;
import java.util.ArrayList;

public class AMCWard extends GeneralWard implements requestable{
    public AMCWard(int wardId){
        super(wardId);
    }

    public ArrayList<Patient> getTransferList(String wardId) throws IOException {
        //String SQLstr = "SELECT id, sex, nextDestination, estimatedTimeNext, transferReqStatus, needsSideRoom FROM patients WHERE currentLocation = "+wardId+";";
        return client.makeGetRequest("id,sex,nextDestination,estimatedTimeNext,transferReqStatus,needsSideRoom", "patients", "currentLocation="+wardId);
    }

    @Override
    public void makeRequest(int patientId, String idealDestination) throws IOException {
        String SQLstr = "UPDATE patients SET transferReqStatus = "+idealDestination+" WHERE id = "+patientId+";";
        client.makePutRequest("patients", "transferReqStatus="+idealDestination, "id="+patientId);
    }
}
