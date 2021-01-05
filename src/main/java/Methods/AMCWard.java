package Methods;

import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AMCWard extends GeneralWard{
    private int transferNumber;

    public AMCWard(int wardId) throws IOException, SQLException {
        super(wardId);
        //Sets transfer number for homescreen
        transferNumber = getTransferList().size();
    }

    public int getTransferNumber(){return transferNumber;}

    //Returns an object to be used in transfer table
    public Object[][] getTransferData() throws IOException, SQLException {
        ArrayList<Patient> patients = getTransferList();
        Object[][] data = new Object[patients.size()][9];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getCurrentBedId();
            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getInitialDiagnosis();
            data[i][5] = p.getNeedsSideRoom();
            data[i][6] = dateFormatter(p.getEstimatedTimeOfNext());
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            ArrayList<Ward> wards = client.wardsFromJson(json);
            if(wards.size()!=0){
                data[i][7] = wards.get(0).getWardName();
            }
            data[i][8] = "Delete Patient";
        }
        return data;
    }


    //Used in transferNumber and getTransferData
    //Returns patients intending to transfer to other wards
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
}
