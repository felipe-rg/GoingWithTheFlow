/*package MethodTest;

import Client.*;
import Methods.AMCWard;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GeneralWardTest {

    //TODO make test ward, beds, patients so that assertion is more accurate
    // For example getBedStatus is effectively running the method again
    @Test
    public void testConstructor() {
        AMCWard amc = null;
        try {
            amc = new AMCWard(11);
            Assert.assertEquals(amc.getWardId(), 11);
            Assert.assertEquals(amc.getWardName(11), "TestAMU");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetWardName(){
        try {
            AMCWard amc = new AMCWard(11);
            Assert.assertEquals(amc.getWardName(11), "TestAMU");
            Assert.assertEquals(amc.getWardName(12), "TestLS");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void testDeletePatient(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(11);

            //Make male patient who requires a sideroom
            Patient p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);
            ArrayList<String> json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            int patientId = patients.get(0).getId();
            c.makePutRequest("patients", "nextdestination=11", "id="+patientId);

            amc.deletePatient(patientId);

            json = c.makeGetRequest("*", "patients", "id="+patientId);
            patients = c.patientsFromJson(json);
            Assert.assertEquals(patients.size(), 0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void testAcceptableBeds(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(11);

            //Make male patient who requires a sideroom
            Patient p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);
            ArrayList<String> json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            int patientId = patients.get(0).getId();

            ArrayList<Bed> beds = amc.getAcceptableBeds(patientId);
            for(Bed b:beds){
                Assert.assertTrue(b.getBedId()==49 || b.getBedId()==53); //Male sr or Uni SR
            }
            amc.deletePatient(patientId);

            //Make female patient who requires a sideroom
            p =new Patient("314159265","Female", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);
            json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            patients = c.patientsFromJson(json);
            patientId = patients.get(0).getId();

            beds = amc.getAcceptableBeds(patientId);
            for(Bed b:beds){
                Assert.assertTrue(b.getBedId()==51 || b.getBedId()==53); //Female sr or Uni SR
            }
            amc.deletePatient(patientId);

            //Make male patient who doesnt require a sideroom
            p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",false);
            c.makePostRequest(p);
            json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            patients = c.patientsFromJson(json);
            patientId = patients.get(0).getId();

            beds = amc.getAcceptableBeds(patientId);
            for(Bed b:beds){
                Assert.assertTrue(b.getBedId()==49 || b.getBedId()==50 || b.getBedId()==53 || b.getBedId()==54 ); //Any male or Uni
            }
            amc.deletePatient(patientId);

            //Make female patient who doesnt require a sideroom
            p =new Patient("314159265","Female", LocalDate.now(),"testpatientdiagnosis123",false);
            c.makePostRequest(p);
            json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            patients = c.patientsFromJson(json);
            patientId = patients.get(0).getId();

            beds = amc.getAcceptableBeds(patientId);
            for(Bed b:beds){
                Assert.assertTrue(b.getBedId()==51 || b.getBedId()==52 || b.getBedId()==53 || b.getBedId()==54 ); //Any female or Uni
            }
            amc.deletePatient(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
/*
    @Test
    public void testSetPatient(){
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

    @Test
    public void testRemoveBed(){
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
            amc.removePatient(patientId, beds.get(0).getBedId());

            json = c.makeGetRequest("*", "patients", "id="+patientId);
            ArrayList<Patient> pat = c.patientsFromJson(json);

            Assert.assertEquals(pat.get(0).getCurrentBedId(), 0);
            Assert.assertEquals(pat.get(0).getCurrentWardId(), 0);
            Assert.assertEquals(pat.get(0).getNextDestination(), 2);
            Assert.assertEquals(pat.get(0).getTransferRequestStatus(), "C");

            json = c.makeGetRequest("*", "beds", "bedid="+beds.get(0).getBedId());
            ArrayList<Bed> b = c.bedsFromJson(json);

            Assert.assertEquals(b.get(0).getStatus(), "F");

            amc.deletePatient(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testEditBed(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);
            amc.editBed(1, "wardid", "15");
            ArrayList<String> json = c.makeGetRequest("*", "beds", "bedid=1");
            ArrayList<Bed> beds = c.bedsFromJson(json);
            Assert.assertEquals(beds.get(0).getWardId(), 15);

            c.makePutRequest("beds", "wardid=2", "bedid=1");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testEditPatient(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);
            Patient p =new Patient("314159265","Male", LocalDate.now(),"testpatientdiagnosis123",true);
            c.makePostRequest(p);
            ArrayList<String> json = c.makeGetRequest("*", "patients", "initialdiagnosis='testpatientdiagnosis123'");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            int patientId = patients.get(0).getId();

            amc.editPatient(patientId, "currentwardid", "15");

            json = c.makeGetRequest("*", "patients", "id="+patientId);
            ArrayList<Patient> pat = c.patientsFromJson(json);

            Assert.assertEquals(pat.get(0).getCurrentWardId(), 15);

            amc.deletePatient(patientId);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetPatient(){
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

            Patient pat = amc.getPatient(beds.get(0).getBedId());
            Assert.assertEquals(pat.getId(), patientId);

            amc.removePatient(patientId, beds.get(0).getBedId());
            amc.deletePatient(patientId);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetBeds(){
        try {
            AMCWard amc = new AMCWard(2);
            ArrayList<Bed> beds = amc.getBeds();
            Assert.assertEquals(beds.size(), 8);
            for(int i=1; i<9; i++){
                Assert.assertEquals(beds.get(i-1).getBedId(), i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetBed(){
        try {
            AMCWard amc = new AMCWard(2);
            Bed bed = amc.getBed(1);
            Assert.assertEquals(bed.getBedId(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetBedColour(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);

            for(int i=2; i<3; i++) {
                Bed bed = amc.getBed(i);
                if (bed.getStatus().equals("C")) {
                    Assert.assertEquals(amc.getBedColour(i), "#000000");
                } else if (bed.getStatus().equals("F")) {
                    Assert.assertEquals(amc.getBedColour(i), "#2ECC71");
                } else if (bed.getStatus().equals("O")) {
                    Patient patient = amc.getPatient(bed.getBedId());
                    LocalDateTime leaving = patient.getEstimatedTimeOfNext();
                    if (leaving.isEqual(patient.getArrivalDateTime())) {
                        Assert.assertEquals(amc.getBedColour(i), "#E74C3C");
                    } else if (leaving.isAfter(LocalDateTime.now().plusHours(4))) {
                        Assert.assertEquals(amc.getBedColour(i), "#E74C3C");
                    } else if (leaving.isBefore(LocalDateTime.now())) {
                        Assert.assertEquals(amc.getBedColour(i), "#1531e8");
                    } else {
                        Assert.assertEquals(amc.getBedColour(i), "#F89820");
                    }

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetBedStatus(){
        try {
            Client c = new Client();
            AMCWard amc = new AMCWard(2);
            ArrayList<String> json = c.makeGetRequest("*", "beds", "wardid=2");
            ArrayList<Bed> beds = c.bedsFromJson(json);
            int green = 0;
            int orange = 0;
            int red = 0;
            for(Bed b:beds){
                if(b.getStatus().equals("F")){
                    green = green +1;
                }
                else if(b.getStatus().equals("C")){
                    red = red +1;
                }
                else if(b.getStatus().equals("O")){
                    Patient patient = amc.getPatient(b.getBedId());
                    LocalDateTime leaving = patient.getEstimatedTimeOfNext();
                    if(leaving.isBefore(LocalDateTime.now())){
                        red = red + 1;
                    }
                    else if(leaving.isAfter(LocalDateTime.now().plusHours(4))){
                        red = red + 1;
                    }
                    else {
                        orange = orange + 1;
                    }
                }
            }
            int colours[] = amc.getBedStatus();
            Assert.assertEquals(colours[0], green);
            Assert.assertEquals(colours[1], orange);
            Assert.assertEquals(colours[2], red);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
*/

}
*/