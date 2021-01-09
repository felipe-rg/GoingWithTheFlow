package Methods;

import java.io.IOException;

public interface colourable {
    public String getBedColour(int bedId) throws IOException;
    public void deletePatient(int patientId) throws IOException;
}
