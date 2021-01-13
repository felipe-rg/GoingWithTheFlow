package Methods.tableInfo.CCTableInfo;

import Client.*;
import Methods.dateFormat;
import Methods.tableInfo.dataForTable;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class IncomingInfoData extends dateFormat implements dataForTable {
    private int incomingInfoNumber;
    Client client;
    ArrayList<String> incomingInfoList;

    public IncomingInfoData(Client client){
        this.client = client;
    };


    @Override
    public String getNumber() {
        return String.valueOf(incomingInfoNumber);
    }

    @Override
    public Object[][] getData() {
        ArrayList<Patient> patients = getList();
        Object[][] data = new Object[patients.size()][7];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = p.getArrivalDateTime();
            data[i][6] = p.getAcceptedByMedicine();
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<Patient> output = new ArrayList<Patient>();
        try {
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
            ArrayList<Ward> amuWards = client.wardsFromJson(json);

            for(Ward w:amuWards){
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                output.addAll(client.patientsFromJson(json));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
