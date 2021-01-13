package Methods.tableInfo.CCTableInfo;

import Client.*;
import Methods.tableInfo.dataForTable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LongStayInfoData extends CCWardData implements dataForTable {
    private int lsInfoNumber;
    Client client;
    ArrayList<String> lsInfoList;

    public LongStayInfoData(Client client){
        this.client = client;
    };


    @Override
    public String getNumber() {
        return String.valueOf(lsInfoNumber);
    }

    //Gets info for table
    @Override
    public Object[][] getData() {
        ArrayList<ArrayList<String>> longStayList = getList();
        Object[][] data= new Object[3][9];
        //Have to undo two array lists
        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++) {
                data[i][j] = longStayList.get(i).get(j); //makes data usable im the table
            }
        }
        return data;
    }

    //Finds info for getData
    private ArrayList<ArrayList<String>> getList(){
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        //For each of the wards, create array of strings with all info necessary; add them into output and return output
        ArrayList<String> json = null;
        try {
            json = client.makeGetRequest("*", "wards", "wardtype='LS'"); //Find all long stay wards
            ArrayList<Ward> lsWards = client.wardsFromJson(json);

            //Get all patients transferring to long stay wards
            for(Ward w:lsWards){
                ArrayList<String> wardInfo = getWardInfo(w.getWardId(), client); //get info for each ward
                output.add(wardInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return output;
    }
}
