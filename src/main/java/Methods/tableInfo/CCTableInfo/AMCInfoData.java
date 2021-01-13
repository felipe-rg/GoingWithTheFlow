package Methods.tableInfo.CCTableInfo;

import Client.*;
import Methods.tableInfo.dataForTable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AMCInfoData extends CCWardData implements dataForTable {
    private int amcInfoNumber;
    Client client;
    ArrayList<String> amcInfoList;
    private int wardId;

    public AMCInfoData(Client client, int wardId){
        this.wardId = wardId;
        this.client = client;
    };


    @Override
    public String getNumber() {
        return String.valueOf(amcInfoNumber);
    }

    @Override
    public Object[][] getData() {
        ArrayList<String> amcList = getList();
        Object[][] data= new Object[1][amcList.size()];
        for(int i=0; i<amcList.size(); i++){
            data[0][i] = amcList.get(i);
        }
        return data;
    }

    private ArrayList<String> getList(){
        try {
            return getWardInfo(wardId, client);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

}
