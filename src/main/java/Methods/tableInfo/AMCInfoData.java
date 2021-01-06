package Methods.tableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class AMCInfoData extends CCWardData implements dataForTable{
    private int amcInfoNumber;
    Client client;
    ArrayList<String> amcInfoList;

    public AMCInfoData(Client client){
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
            return getWardInfo(2, client);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
