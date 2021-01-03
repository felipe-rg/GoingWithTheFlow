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
        transferNumber = getTransferList().size();
    }

    public Object[][] getTransferData() throws IOException, SQLException {
        ArrayList<Patient> patients = getTransferList();
        Object[][] data = new Object[patients.size()][8];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = dateFormatter(p.getEstimatedTimeOfNext());
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            ArrayList<Ward> wards = client.wardsFromJson(json);
            if(wards.size()!=0){
                data[i][6] = wards.get(0).getWardName();
            }
            data[i][7] = "Delete Patient";
        }
        return data;
    }

    //FIXME definitely better way
    public ArrayList<Patient> getTransferList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("*", "patients", "nextdestination!=0");
        ArrayList<Patient> notStaying = client.patientsFromJson(json);
        json = client.makeGetRequest("*", "patients", "nextdestination!=6");
        ArrayList<Patient> notDischarge = client.patientsFromJson(json);
        json = client.makeGetRequest("*", "patients", "nextdestination!=7");
        ArrayList<Patient> notICU = client.patientsFromJson(json);
        ArrayList<Patient> goingToICUorWard = client.crossReference(notStaying,notDischarge);
        ArrayList<Patient> transfers = client.crossReference(goingToICUorWard,notICU);
        return client.crossReference(patients,transfers);
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
