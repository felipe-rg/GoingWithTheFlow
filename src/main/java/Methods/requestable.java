package Methods;

import java.io.IOException;
import java.sql.SQLException;

public interface requestable {
    //FIXME this can be a client that is inherited rather than an interface
    void makeRequest(int patientId, String idealDestination) throws IOException, SQLException;
}
