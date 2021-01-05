package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DischargeTableData extends dateFormat implements dataForTable{
    private int dischargeNumber;
    private int wardId;
    Client client;
    ArrayList<Patient> incomingList;

    public DischargeTableData(Client client, int wardId){
        this.client = client;
        this.wardId = wardId;
        incomingList = getList();
        dischargeNumber = incomingList.size();
    };


    @Override
    public String getNumber() {
        return String.valueOf(dischargeNumber);
    }

    @Override
    public Object[][] getData() {
        Object[][] data = new Object[dischargeNumber][9];
        for(int i=0; i<dischargeNumber; i++) {
            Patient p = incomingList.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = p.getTtaSignedOff();
            data[i][6] = p.getSuitableForDischargeLounge();
            data[i][7] = dateFormatter(p.getEstimatedTimeOfNext());
            data[i][8] = "Delete Patient";
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<Patient> output = new ArrayList<Patient>();
        try {
            ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
            ArrayList<Patient> patients = client.patientsFromJson(json);

            json = client.makeGetRequest("*", "patients", "nextdestination=6");
            ArrayList<Patient> discharging = client.patientsFromJson(json);

            output = client.crossReference(patients, discharging);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}