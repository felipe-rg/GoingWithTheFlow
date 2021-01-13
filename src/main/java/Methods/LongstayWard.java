package Methods;

import Client.Bed;
import Client.Patient;
import Client.Ward;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
/*      Long stay wards are normal wards where their incoming is from AMU not A&E
*       In the future we need a patient form for each ward for them to log inpatients
*       for now closed beds will do similar*/

public class LongstayWard extends GeneralWard{
    public LongstayWard(int wardId) throws IOException, SQLException {
        super(wardId);
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination=" + wardId);
        ArrayList<Patient> incoming = new ArrayList<>();
        if (json.size() != 0) {
            incoming.addAll(client.patientsFromJson(json));
        }
        inNumber = incoming.size();
    }

    //Long stay destinations are only discharge or other
    public void updateDestinationNumber(int dest, int number){
        for(int i:dcWardIds){
            if(dest==i){
                changeDischargeNumber(number);
            }
        }
        for(int i:othWardIds){
            if(dest==i){
                changeOtherNumber(number);
            }
        }
    }

    //Long stay destinations are only discharge or other
    public ArrayList<Ward> getAllTransWards(){
        ArrayList<Ward> output = new ArrayList<Ward>();
        output.addAll(dcWards);
        output.addAll(othWards);
        return output;
    }


}
