package Methods;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import Client.*;

public class AandE{
    Client client;
    public AandE(int wardId){
        client = new Client();
    }

    //Creates patient with essential information
    public void createPatient(Patient p) throws IOException {
        client.makePostRequest(p);
    }
}
