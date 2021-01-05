package MethodTest;

import Client.*;
import Methods.AMCWard;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class GeneralWardTest {

    @Test
    public void testConstructor() {
        AMCWard amc = null;
        try {
            amc = new AMCWard(2);
            Assert.assertEquals(amc.getWardId(), 2);
            Assert.assertEquals(amc.getWardName(2), "AMC1");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetWardName(){
        try {
            AMCWard amc = new AMCWard(2);
            Assert.assertEquals(amc.getWardName(0), "No Destination");
            Assert.assertEquals(amc.getWardName(1), "A&E");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testDeletePatient(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);
            Patient p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);

            ArrayList<String> json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            int patientId = patients.get(0).getId();
            amc.deletePatient(patientId);

            json = c.makeGetRequest("*", "patients", "initialdiagnosis=testpatientdiagnosis123");
            Assert.assertEquals(c.patientsFromJson(json).size(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testAcceptableBeds(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);
            Patient p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);
            ArrayList<String> json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            int patientId = patients.get(0).getId();
            c.makePutRequest("patients", "currentwardid=2", "id="+patientId);

            ArrayList<Bed> beds = amc.getAcceptableBeds(patientId);
            for(Bed b:beds){
                Assert.assertEquals(b.getHasSideRoom(), true);
                Assert.assertTrue(b.getForSex().equals("Male") || b.getForSex().equals("Uni"));
                Assert.assertEquals(b.getWardId(), 2);
                Assert.assertEquals(b.getStatus(), "F");
            }
            amc.deletePatient(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Test
    public void testSetBed(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);
            Patient p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);
            ArrayList<String> json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            int patientId = patients.get(0).getId();
            c.makePutRequest("patients", "currentwardid=2", "id="+patientId);

            ArrayList<Bed> beds = amc.getAcceptableBeds(patientId);
            if(beds.size()==0){
                amc.deletePatient(patientId);
                return;
            }

            amc.setBed(patientId, beds.get(0).getBedId());

            json = c.makeGetRequest("*", "patients", "id="+patientId);
            ArrayList<Patient> pat = c.patientsFromJson(json);

            Assert.assertEquals(pat.get(0).getCurrentBedId(), beds.get(0).getBedId());
            Assert.assertEquals(pat.get(0).getCurrentWardId(), beds.get(0).getWardId());
            Assert.assertEquals(pat.get(0).getNextDestination(), 0);

            json = c.makeGetRequest("*", "beds", "bedid="+beds.get(0).getBedId());
            ArrayList<Bed> b = c.bedsFromJson(json);

            Assert.assertEquals(b.get(0).getStatus(), "O");

            c.makePutRequest("beds", "status='F'", "bedid="+beds.get(0).getBedId());

            amc.deletePatient(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
