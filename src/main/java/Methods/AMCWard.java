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

    //Returns list of patients going to be transferred from amc
    //Used in table of transfers
    public ArrayList<Patient> getTransferList(int wardId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentLocation="+wardId);
        ArrayList<Patient> inAMC = client.patientsFromJson(json);

        //todo next destination != null
        json = client.makeGetRequest("*", "patients", "nextdestination=3");
        ArrayList<Patient> transferring = client.patientsFromJson(json);

        return client.crossReference(inAMC, transferring);
    }

    //Changes next destination of patient to ideal desination
    //Changes transferrequest status to pending
    //Used to request a transfer to a new ward from AMC
    @Override
    public void makeRequest(int patientId, int idealDestination) throws IOException, SQLException {
        client.makePutRequest("patients", "nextdestination="+idealDestination, "id="+patientId);
        client.makePutRequest("patients", "transferrequeststatus='P'", "id="+patientId);
    }
}
