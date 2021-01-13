package MethodTest;
import Client.*;
import Methods.AMCWard;
import Methods.LongstayWard;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
public class LongStayWardTest {
    @Test
    public void testGetTransWards(){
        try {
            Client c = new Client();
            LongstayWard ls = new LongstayWard(12);

            ArrayList<Ward> wards = ls.getAllTransWards();
            for(Ward w:wards){
                Assert.assertTrue(w.getWardId()==6 ||w.getWardId()==7 ||w.getWardId()==8);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
