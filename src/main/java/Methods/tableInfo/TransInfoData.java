package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransInfoData extends dateFormat implements dataForTable{
    private int transInfoNumber;
    Client client;
    ArrayList<String> transInfoList;

    public TransInfoData(Client client){
        this.client = client;
    };


    @Override
    public String getNumber() {
        return String.valueOf(transInfoNumber);
    }

    @Override
    public Object[][] getData() {
        ArrayList<Patient> patients = getList();
        Object[][] data = new Object[patients.size()][8];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);

            data[i][0] = p.getId();

            try {
                data[i][1] = getWardName(p.getCurrentWardId());
            } catch (IOException e) {
                e.printStackTrace();
            }

            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = p.getInitialDiagnosis();


            ArrayList<String> json = null;
            try {
                json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(json.size()!=0){
                String wardName = client.wardsFromJson(json).get(0).getWardName();
                data[i][6] = wardName;
            }

            data[i][7] = dateFormatter(p.getEstimatedTimeOfNext());
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<Patient> output = new ArrayList<Patient>();
        ArrayList<String> json = null;
        try {
            json = client.makeGetRequest("*", "wards", "wardtype='LS'");
            ArrayList<Ward> lsWards = client.wardsFromJson(json);

            //Get all patients transferring to long stay wards
            for(Ward w:lsWards){
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                output.addAll(client.patientsFromJson(json));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    //Function that returns the wardname if you inout the wardID
    public String getWardName(int wardID) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+wardID);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        String output = null;
        if(wards.size()!=0) {
            output = wards.get(0).getWardName();
        }
        else {
            output = "No Destination";
        }
        return output;
    }
}
