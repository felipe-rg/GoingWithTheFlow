package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.util.ArrayList;

public class TransTableData extends dateFormat implements dataForTable{

    private int transNumber;
    private int wardId;
    Client client;
    ArrayList<Patient> transList;

    public TransTableData(Client client, int wardId){
        this.client = client;
        this.wardId = wardId;
        transList = getList();
        transNumber = transList.size();
    };

    @Override
    public String getNumber(){
        return String.valueOf(transNumber);
    }

    @Override
    public Object[][] getData() {
        Object[][] data = new Object[transList.size()][10];
        for(int i=0; i<transList.size(); i++) {
            Patient p = transList.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getCurrentBedId();
            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getInitialDiagnosis();
            data[i][5] = p.getNeedsSideRoom();
            data[i][6] = dateFormatter(p.getEstimatedTimeOfNext());
            ArrayList<String> json = null;

            try {
                json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            } catch (IOException e) {
                e.printStackTrace();
            }

            ArrayList<Ward> wards = client.wardsFromJson(json);
            if(wards.size()!=0){
                data[i][7] = wards.get(0).getWardName();
            }

            if(p.getTransferRequestStatus().equals("P")){
                data[i][8] = "Pending";
            }
            else if(p.getTransferRequestStatus().equals("C")){
                data[i][8] = "Confirmed";
            }
            else if(p.getTransferRequestStatus().equals("R")){
                data[i][8] = "Rejected";
            }
            else {data[i][8] = p.getTransferRequestStatus();}
            data[i][9] = "Delete Patient";
        }
        return data;
    }

    private ArrayList<Patient> getList() {
        ArrayList<Patient> output = new ArrayList<Patient>();
        ArrayList<String> json = null;
        try {
            //Get all patients in ward
            json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
            ArrayList<Patient> patients = client.patientsFromJson(json);

            json = client.makeGetRequest("*", "wards", "wardtype='LS'");
            ArrayList<Ward> lsWards = client.wardsFromJson(json);
            //Get all patients transferring to long stay wards
            ArrayList<Patient> transfers = new ArrayList<Patient>();
            for(Ward w:lsWards){
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                transfers.addAll(client.patientsFromJson(json));
            }

            //Return all patients in ward who are transferring
            output = client.crossReference(patients,transfers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
    public void refresh(){
        getList();
    }
}
