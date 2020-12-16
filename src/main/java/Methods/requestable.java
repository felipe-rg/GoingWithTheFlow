package Methods;

import java.io.IOException;

public interface requestable {
    //FIXME this can be a client that is inherited rather than an interface
    void makeRequest(String patientId, String idealDestination) throws IOException;
}
