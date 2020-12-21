package Methods;

import java.io.IOException;
import java.util.ArrayList;

import Client.Patient;

public interface statusable {
    public ArrayList<Patient> getWardInfo(int wardId) throws IOException;
    public ArrayList<Patient> getPatientInfo(int wardId);
}
