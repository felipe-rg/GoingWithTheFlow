package MethodTest;

import AMCWardPanels.BedStatus;
import AMCWardPanels.Topography;
import AMCWardPanels.WardInfo;
import Client.*;
import Methods.AMCWard;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class GeneralWardTest {

    private Client c;
    private AMCWard amc;

    @Before
    public void setup() {
        c = new Client();
        try {
            amc = new AMCWard(11);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        try {
            Assert.assertEquals(amc.getWardId(), 11);
            Assert.assertEquals(amc.getWardName(11), "TestAMU");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetWardName() {
        try {
            Assert.assertEquals(amc.getWardName(11), "TestAMU");
            Assert.assertEquals(amc.getWardName(12), "TestLS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeletePatientFromDatabase() {
        try {
            amc.deletePatientFromDatabase(3, 0);
            ArrayList<String> json = c.makeGetRequest("*", "patients", "id=3");
            ArrayList<Patient> patients = c.patientsFromJson(json);
            Assert.assertEquals(patients.size(), 0);

            //Make male patient
            Patient p = new Patient("3141592659", "Male", LocalDate.now(), "testmale", false);
            c.makePostRequest(p);

            json = c.makeGetRequest("*", "patients", "patientid='3141592659'");
            p = c.patientsFromJson(json).get(0);
            c.makePutRequest("patients", "id=3", "id=" + p.getId());
            c.makePutRequest("patients", "currentwardid=0", "id=3");
            c.makePutRequest("patients", "nextdestination=11", "id=3");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptableBeds() {
        try {
            ArrayList<Bed> beds = amc.getAcceptableBeds(5);
            for (Bed b : beds) {
                Assert.assertTrue(b.getBedId() == 49 || b.getBedId() == 53); //Male sr or Uni SR
            }

            beds = amc.getAcceptableBeds(6);
            for (Bed b : beds) {
                Assert.assertTrue(b.getBedId() == 51 || b.getBedId() == 53); //Female sr or Uni SR
            }

            beds = amc.getAcceptableBeds(3);
            for (Bed b : beds) {
                Assert.assertTrue(b.getBedId() == 49 || b.getBedId() == 50 || b.getBedId() == 53 || b.getBedId() == 54); //Any male or Uni
            }

            beds = amc.getAcceptableBeds(4);
            for (Bed b : beds) {
                Assert.assertTrue(b.getBedId() == 51 || b.getBedId() == 52 || b.getBedId() == 53 || b.getBedId() == 54); //Any female or Uni
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testSetBedDatabase() {
        try {

            amc.setBedDatabase(9, 51);

            ArrayList<String> json = c.makeGetRequest("*", "patients", "id=9");

            ArrayList<Patient> pat = c.patientsFromJson(json);

            Assert.assertEquals(pat.get(0).getCurrentBedId(), 51);
            Assert.assertEquals(pat.get(0).getCurrentWardId(), 11);
            Assert.assertEquals(pat.get(0).getNextDestination(), 0);

            json = c.makeGetRequest("*", "beds", "bedid=51");
            ArrayList<Bed> b = c.bedsFromJson(json);

            Assert.assertTrue(b.get(0).getStatus().equals("O"));

            json = c.makeGetRequest("*", "beds", "bedid=57");
            b = c.bedsFromJson(json);

            Assert.assertTrue(b.get(0).getStatus().equals("F"));

            c.makePutRequest("beds", "status='O'", "bedid=57");
            c.makePutRequest("beds", "status='F'", "bedid=51");
            c.makePutRequest("patients", "currentwardid=12", "id=9");
            c.makePutRequest("patients", "nextdestination=11", "id=9");
            c.makePutRequest("patients", "currentbedid=57", "id=9");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRemovePatientFromDatabase() {
        try {
            amc.removePatientFromDatabase(7, 55);

            ArrayList<String> json = c.makeGetRequest("*", "patients", "id=7");
            ArrayList<Patient> pat = c.patientsFromJson(json);

            Assert.assertEquals("Current Bed", pat.get(0).getCurrentBedId(), 0);
            Assert.assertEquals("Current ward", pat.get(0).getCurrentWardId(), 0);
            Assert.assertEquals("Next destination", pat.get(0).getNextDestination(), 11);
            Assert.assertTrue("Transfer Request status", pat.get(0).getTransferRequestStatus().equals("C"));

            json = c.makeGetRequest("*", "beds", "bedid=55");
            ArrayList<Bed> b = c.bedsFromJson(json);

            Assert.assertTrue("Bed status", b.get(0).getStatus().equals("F"));

            c.makePutRequest("patients", "currentbedid=55", "id=7");
            c.makePutRequest("patients", "currentwardid=11", "id=7");
            c.makePutRequest("patients", "nextdestination=0", "id=7");
            c.makePutRequest("patients", "transferrequeststatus='P'", "id=7");
            c.makePutRequest("beds", "status='O'", "bedid=55");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEditBed() {
        try {

            amc.editBed(56, "wardid", "15");
            amc.editBed(56, "forsex", "'Male'");
            amc.editBed(56, "hassideroom", "'true'");
            amc.editBed(56, "status", "'O'");
            ArrayList<String> json = c.makeGetRequest("*", "beds", "bedid=56");
            ArrayList<Bed> beds = c.bedsFromJson(json);
            Assert.assertEquals(beds.get(0).getWardId(), 15);
            Assert.assertTrue(beds.get(0).getForSex().equals("Male"));
            Assert.assertTrue(beds.get(0).getHasSideRoom());
            Assert.assertTrue(beds.get(0).getStatus().equals("O"));


            c.makePutRequest("beds", "wardid=12", "bedid=56");
            c.makePutRequest("beds", "forsex='Uni'", "bedid=56");
            c.makePutRequest("beds", "hassisderoom='false'", "bedid=56");
            c.makePutRequest("beds", "status='F'", "bedid=56");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEditPatient() {
        try {

            amc.editPatient(8, "currentwardid", "15");
            amc.editPatient(8, "sex", "'Female'");
            amc.editPatient(8, "currentbedid", "54");
            amc.editPatient(8, "initialdiagnosis", "'test'");
            amc.editPatient(8, "needssideroom", "false");
            amc.editPatient(8, "acceptedbymedicine", "false");
            amc.editPatient(8, "ttasignedoff", "false");
            amc.editPatient(8, "suitablefordischargelounge", "false");
            amc.editPatient(8, "transferrequeststatus", "'C'");
            amc.editPatient(8, "deceased", "false");
            amc.editPatient(8, "estimatedatetimeofnext", "'2023-03-03 03:03:03.33333'");
            amc.editPatient(8, "patientid", "0000000000");
            amc.editPatient(8, "dateofbirth", "'1903-03-03'");
            amc.editPatient(8, "nextdestination", "15");



            ArrayList<String> json = c.makeGetRequest("*", "patients", "id=8");
            ArrayList<Patient> pat = c.patientsFromJson(json);

            Assert.assertEquals(pat.get(0).getCurrentWardId(), 15);
            Assert.assertEquals(pat.get(0).getNextDestination(), 15);
            Assert.assertEquals(pat.get(0).getCurrentBedId(), 54);
            Assert.assertFalse(pat.get(0).getNeedsSideRoom());
            Assert.assertFalse(pat.get(0).getAcceptedByMedicine());
            Assert.assertFalse(pat.get(0).getTtaSignedOff());
            Assert.assertFalse(pat.get(0).getSuitableForDischargeLounge());
            Assert.assertFalse(pat.get(0).getDeceased());
            Assert.assertTrue(pat.get(0).getSex().equals("Female"));
            Assert.assertTrue(pat.get(0).getInitialDiagnosis().equals("test"));
            Assert.assertTrue(pat.get(0).getTransferRequestStatus().equals("C"));
            Assert.assertTrue(pat.get(0).getPatientId().equals("0000000000"));
            Assert.assertTrue(pat.get(0).getDateOfBirth().equals("1903-03-03"));
            Assert.assertTrue(pat.get(0).getEstimatedTimeOfNext().equals("2023-03-03 03:03:03.33333"));

            c.makePutRequest("patients", "currentwardid=0", "id=8");
            c.makePutRequest("patients", "sex='Male'", "id=8");
            c.makePutRequest("patients", "currentbedid=0", "id=8");
            c.makePutRequest("patients", "initialdiagnosis='editPatientTest'", "id=8");
            c.makePutRequest("patients", "needssideroom=true", "id=8");
            c.makePutRequest("patients", "acceptedbymedicine=true", "id=8");
            c.makePutRequest("patients", "ttasignedoff=true", "id=8");
            c.makePutRequest("patients", "suitablefordischargelounge=true", "id=8");
            c.makePutRequest("patients", "transferrequeststatus='R'", "id=8");
            c.makePutRequest("patients", "deceased=true", "id=8");
            c.makePutRequest("patients", "estimatedatetimeofnext='"+LocalDateTime.now()+"'", "id=8");
            c.makePutRequest("patients", "patientid='3141592654'", "id=8");
            c.makePutRequest("patients", "dateofbirth='2010-01-01'", "id=8");
            c.makePutRequest("patients", "nextdestination=0", "id=8");
            c.makePutRequest("patients", "dateofbirth='2010-01-01'", "id=8");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Test
    public void testGetPatient(){
        try {
            Patient p = amc.getPatient(58);

            Assert.assertEquals(p.getCurrentWardId(), 12);
            Assert.assertEquals(p.getNextDestination(), 0);
            Assert.assertTrue(p.getPatientId().equals("3141592658"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}/*
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
