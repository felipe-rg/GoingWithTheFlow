import Client.Client;
import Methods.Patient;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import Methods.AMCWard;

public class GeneralWardTest {
    Client client = new Client();
    int patientId;
    int wardId;
    int bedId;
    AMCWard ward;
    @Before
    public void setup() throws IOException {
        //Create test ward, bed, and patient
        Time time = Time.valueOf(LocalTime.now());
        String SQLstr = "INSERT INTO patients(currentLocation, acceptedByMedicine, sex, arrivalTime, initialDiagnosis) VALUES('AandE', false, 'male', "+time+", 'TestDiagnosis');";
        client.makePostRequest(SQLstr);
        SQLstr = "INSERT INTO wards(wardName) VALUES('TestWard');";
        client.makePostRequest(SQLstr);
        SQLstr = "INSERT INTO beds(wardId) VALUES("+wardId+");";
        client.makePostRequest(SQLstr);

        //Get the id for each of the tests
        //SQLstr = "SELECT id FROM patients WHERE initialDiagnosis = 'Test Diagnosis';";
        ArrayList<Patient> patient = client.makeGetRequest( "id", "patients", "initialDiagnosis='TestDiagnosis'");
        patientId = patient.get(0).id;
        //SQLstr = "SELECT id FROM wards WHERE wardName = 'Test Ward';";
        ArrayList<Patient> wards = client.makeGetRequest( "id", "wards", "wardName=TestWard");
        wardId = wards.get(0).id;
        //SQLstr = "SELECT id FROM beds WHERE wardId = "+wardId+";";
        ArrayList<Patient> beds = client.makeGetRequest( "id", "beds", "wardid="+wardId);
        bedId = beds.get(0).id;

        //Initialise a ward to test methods
        ward = new AMCWard(wardId);
    }


    @Test
    public void testDischarge() throws IOException {
        ward.discharge(patientId);
        String SQLstr = "SELECT * FROM patients WHERE id="+patientId+";";
        ArrayList<Patient> patient = client.makeGetRequest( "*", "patients", "id="+patientId);
        Assert.assertEquals(patient.get(0).id, null);
    }


}
