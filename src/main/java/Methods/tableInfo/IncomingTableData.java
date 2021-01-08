package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.util.ArrayList;

public class IncomingTableData extends dateFormat implements dataForTable{
    private int incomingNumber;
    private int wardId;
    private String wardType;
    Client client;
    ArrayList<Patient> incomingList;

    public IncomingTableData(Client client, int wardId){
        this.client = client;
        this.wardId = wardId;
        incomingList = getList();
        incomingNumber = incomingList.size();
    };


    @Override
    public String getNumber() {
        return String.valueOf(incomingNumber);
    }

    @Override
    public Object[][] getData() {
        Object[][] data = new Object[incomingNumber][9];
        if(wardType.equals("AMU")) {
            for (int i = 0; i < incomingNumber; i++) {
                Patient p = incomingList.get(i);
                data[i][0] = p.getId();
                data[i][1] = p.getPatientId();
                data[i][2] = p.getSex();
                data[i][3] = p.getInitialDiagnosis();
                data[i][4] = p.getNeedsSideRoom();
                data[i][5] = p.getArrivalDateTime();
                data[i][6] = p.getAcceptedByMedicine();
                data[i][7] = "Select Bed";
                data[i][8] = "Delete Patient";
            }
        } else {
            for(int i=0; i<incomingNumber; i++) {
                Patient p = incomingList.get(i);
                data[i][0] = p.getId();
                data[i][1] = p.getPatientId();
                data[i][2] = p.getSex();
                data[i][3] = p.getInitialDiagnosis();
                data[i][4] = p.getNeedsSideRoom();
                data[i][5] = p.getEstimatedTimeOfNext();
                data[i][6] = p.getTransferRequestStatus();
                data[i][7] = "Select Bed";
                data[i][8] = "Delete Patient";
            }
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<String> json = null;
        ArrayList<Patient> output = new ArrayList<Patient>();
        try {
            //get ward
            json = client.makeGetRequest("*", "wards", "wardid="+wardId);
            ArrayList<Ward> ward = client.wardsFromJson(json);
            wardType = ward.get(0).getWardType();
            //if ward is AMU then we find all incoming to all amu wards
            if(wardType.equals("AMU")){
                //get all amu wards
                json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
                ArrayList<Ward> amuWard = client.wardsFromJson(json);
                //get all patients in amu wards
                for(Ward w:amuWard) {
                    json = client.makeGetRequest("*", "patients", "nextdestination=" + w.getWardId());
                    output.addAll(client.patientsFromJson(json));
                }
            }
            else {
                json = client.makeGetRequest("*", "patients", "nextdestination=" + wardId);
                output.addAll(client.patientsFromJson(json));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public void refresh(){
        getList();
    }
}
