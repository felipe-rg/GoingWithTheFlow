package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.util.ArrayList;

public class IncomingTableData extends dateFormat implements dataForTable{
    private int incomingNumber;
    private int wardId;
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
        if(wardId == 2) {
            for (int i = 0; i < incomingNumber; i++) {
                Patient p = incomingList.get(i);
                data[i][0] = p.getId();
                data[i][1] = p.getPatientId();
                data[i][2] = p.getSex();
                data[i][3] = p.getInitialDiagnosis();
                data[i][4] = p.getNeedsSideRoom();
                data[i][5] = dateFormatter(p.getArrivalDateTime());
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
                data[i][5] = dateFormatter(p.getEstimatedTimeOfNext());
                data[i][6] = p.getTransferRequestStatus();
                data[i][7] = "Select Bed";
                data[i][8] = "Delete Patient";
            }
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<String> json = null;
        try {
            json = client.makeGetRequest("*", "patients", "nextdestination="+wardId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client.patientsFromJson(json);
    }
}
