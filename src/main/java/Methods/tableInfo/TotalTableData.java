package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TotalTableData extends dateFormat implements dataForTable{
    private int totalNumber;
    private int wardId;
    Client client;
    ArrayList<Patient> totalList;

    public TotalTableData(Client client, int wardId){
        this.client = client;
        this.wardId = wardId;
        totalList = getList();
        totalNumber = totalList.size();
    };


    @Override
    public String getNumber() {
        return String.valueOf(totalNumber);
    }

    @Override
    public Object[][] getData() {
        Object[][] data = new Object[totalNumber][9];
        for(int i=0; i<totalNumber; i++) {
            Patient p = totalList.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = durationFormatter(Duration.between(p.getArrivalDateTime(), LocalDateTime.now()));

            if(p.getNextDestination()==0){
                data[i][6] = null;
            }
            ArrayList<String> json = null;
            try {
                json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<Ward> wards = client.wardsFromJson(json);

            if(wards.size()!=0){
                data[i][6] = wards.get(0).getWardName();
            }
            data[i][7] = "Delete Patient";
        }
        return data;
    }

    private ArrayList<Patient> getList(){
        ArrayList<String> json = null;
        try {
            json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client.patientsFromJson(json);
    }
}
