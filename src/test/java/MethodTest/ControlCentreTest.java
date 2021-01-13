package MethodTest;

import Client.Client;
import Methods.tableInfo.CCTableInfo.CCWardData;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControlCentreTest {
    @Test
    public void testGetWardInfo(){
        CCWardData c = new CCWardData();
        try {
            ArrayList<String> info =  c.getWardInfo(12, new Client());
            String wardName = info.get(0);
            String capacity = info.get(1);
            String freeMaleBeds = info.get(2);
            String freeFemaleBeds = info.get(3);
            String freeUniBeds = info.get(4);
            String maleDischarge = info.get(5);
            String femaleDischarge = info.get(6);
            String ICU = info.get(7);
            String RIP = info.get(8);

            Assert.assertTrue(wardName.equals("TestLS"));
            Assert.assertTrue(capacity.equals("75.0"));
            Assert.assertTrue(freeMaleBeds.equals("1"));
            Assert.assertTrue(freeFemaleBeds.equals("0"));
            Assert.assertTrue(freeUniBeds.equals("1"));
            Assert.assertTrue(maleDischarge.equals("0"));
            Assert.assertTrue(femaleDischarge.equals("1"));
            Assert.assertTrue(ICU.equals("2"));
            Assert.assertTrue(RIP.equals("1"));




        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
