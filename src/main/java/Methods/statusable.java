package Methods;

import java.io.IOException;
import java.util.ArrayList;

public interface statusable {
    public ArrayList<Patient> getWardInfo(String wardId) throws IOException;
    public ArrayList<Patient> getPatientInfo(String wardId);
}
