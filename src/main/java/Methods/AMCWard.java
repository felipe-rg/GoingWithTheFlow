package Methods;

import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AMCWard extends GeneralWard{

    private int transNumber;

    public AMCWard(int wardId) throws IOException, SQLException {
        super(wardId);
        ArrayList<Patient> incoming = new ArrayList<Patient>();
        for(int i=0; i<amuWardIds.length; i++) {
            ArrayList json = client.makeGetRequest("*", "patients", "nextdestination="+amuWardIds[i]);
            if (json.size() != 0) {
                incoming.addAll(client.patientsFromJson(json));
            }
        }
        inNumber = incoming.size();
    }

    public int getTransNumber(){
        return transNumber;
    };

    public void changeTransNumber(int i){
        transNumber = transNumber+i;
        wardInfo.setTransText(String.valueOf(transNumber));
    }

    public void updateDestinationNumber(int dest, int number){
        for(int i:dcWardIds){
            if(dest==i){
                changeDischargeNumber(number);
            }
        }
        for(int i:lsWardIds){
            if(dest==i){
                changeTransNumber(number);
            }
        }
        for(int i:othWardIds){
            if(dest==i){
                changeOtherNumber(number);
            }
        }
    }

    public ArrayList<Ward> getAllTransWards(){
        ArrayList<Ward> output = new ArrayList<Ward>();
        output.addAll(lsWards);
        output.addAll(dcWards);
        output.addAll(othWards);
        return output;
    }

}
