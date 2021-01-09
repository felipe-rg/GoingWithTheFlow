package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class DischargeInfoData extends dateFormat implements dataForTable{
    private int dischargeInfoNumber;
    Client client;
    ArrayList<String> dischargeInfoList;

    public DischargeInfoData(Client client){
        this.client = client;
    };


    @Override
    public String getNumber() {
        return String.valueOf(dischargeInfoNumber);
    }

    @Override
    public Object[][] getData() {
        ArrayList<Patient> patients = getList();
        Object[][] data = new Object[patients.size()][8];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getCurrentWardId();
            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getInitialDiagnosis();
            data[i][5] = p.getTtaSignedOff();
            data[i][6] = p.getSuitableForDischargeLounge();
            data[i][7] = dateFormatter(p.getEstimatedTimeOfNext());
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<String> json = null;
        ArrayList<Patient> output = new ArrayList<Patient>();
        try {
            json = client.makeGetRequest("*", "wards", "wardtype='discharge'");
            ArrayList<Ward> dischargeWards = client.wardsFromJson(json);

            for(Ward w: dischargeWards){
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                output.addAll(client.patientsFromJson(json));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
