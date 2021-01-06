package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class LongStayInfoData extends CCWardData implements dataForTable{
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

    @Override
    public Object[][] getData() {
        ArrayList<ArrayList<String>> longStayList = getList();
        Object[][] data= new Object[3][9];
        for(int i=0; i<3; i++){
            for(int j=0; j<9; j++) {
                data[i][j] = longStayList.get(i).get(j);
            }
        }
        return data;
    }

    private ArrayList<ArrayList<String>> getList(){
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        //For each of the wards, create array of strings with all info necessary; add them into output and return output
        try {
            for(int i=3; i<6; i++){
                ArrayList<String> wardInfo = getWardInfo(i, client);
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
