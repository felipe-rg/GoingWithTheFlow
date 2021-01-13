package Methods.tableInfo.WardTableInfo;

import Client.*;
import Methods.dateFormat;
import Methods.tableInfo.dataForTable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DischargeTableData extends dateFormat implements dataForTable {
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
        Object[][] data = new Object[dischargeNumber][10];
        for(int i=0; i<dischargeNumber; i++) {
            Patient p = incomingList.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getCurrentBedId();
            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getInitialDiagnosis();
            data[i][5] = p.getNeedsSideRoom();
            data[i][6] = p.getTtaSignedOff();
            data[i][7] = p.getSuitableForDischargeLounge();
            data[i][8] = dateFormatter(p.getEstimatedTimeOfNext());
            data[i][9] = "Delete Patient";
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<Patient> output = new ArrayList<Patient>();
        try {
            ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
            ArrayList<Patient> patients = client.patientsFromJson(json);

            json = client.makeGetRequest("*", "wards", "wardtype='discharge'");
            ArrayList<Ward> dischargeWards = client.wardsFromJson(json);

            ArrayList<Patient> discharging = new ArrayList<Patient>();

            for(Ward w: dischargeWards){
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                discharging.addAll(client.patientsFromJson(json));
            }

            output = client.crossReference(patients, discharging);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
    public void refresh(){
        getList();
    }
}