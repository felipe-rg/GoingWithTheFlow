package Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import Client.Patient;

public interface statusable {
    public ArrayList<Patient> getWardInfo(int wardId) throws IOException, SQLException;
    public ArrayList<Patient> getPatientInfo(int wardId);
}
