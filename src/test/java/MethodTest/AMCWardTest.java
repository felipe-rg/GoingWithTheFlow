package MethodTest;
import Client.*;
import Methods.AMCWard;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
public class AMCWardTest {
    @Test
    public void testGetTransWards(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(11);

            ArrayList<Ward> wards = amc.getAllTransWards();
            for(Ward w:wards){
                Assert.assertTrue(w.getWardId()==3 || w.getWardId()==4 || w.getWardId()==5 || w.getWardId()==6 ||w.getWardId()==7 ||w.getWardId()==8);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
