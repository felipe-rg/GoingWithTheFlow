package Methods.tableInfo;
import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OtherTableData  extends dateFormat implements dataForTable{
    private int otherNumber;
    private int wardId;
    Client client;
    ArrayList<Patient> otherList;

    public OtherTableData(Client client, int wardId){
        this.client = client;
        this.wardId = wardId;
        otherList = getList();
        otherNumber = otherList.size();
    };

    @Override
    public String getNumber(){
        return String.valueOf(otherNumber);
    }

    @Override
    public Object[][] getData() {
        Object[][] data = new Object[otherList.size()][9];
        for(int i=0; i<otherList.size(); i++) {
            Patient p = otherList.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = dateFormatter(p.getEstimatedTimeOfNext());

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
            data[i][8] = "Delete Patient";
        }
        return data;
    }

    private ArrayList<Patient> getList() {
        ArrayList<Patient> output = new ArrayList<Patient>();
        try {
            ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
            ArrayList<Patient> patients = client.patientsFromJson(json);
            ArrayList<Patient> othPatients = new ArrayList<Patient>();

            json = client.makeGetRequest("*", "wards", "wardtype='other'");
            ArrayList<Ward> othDestinations = client.wardsFromJson(json);

            for(Ward w: othDestinations){
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                othPatients.addAll(client.patientsFromJson(json));
            }
            output.addAll(client.crossReference(othPatients, patients));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
