package Methods;

import Client.Bed;
import Client.Patient;
import Client.Ward;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class LongstayWard extends GeneralWard{
    protected int inNumber;
    public LongstayWard(int wardId) throws IOException, SQLException {
        super(wardId);
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination=" + wardId);
        ArrayList<Patient> incoming = new ArrayList<>();
        if (json.size() != 0) {
            incoming = client.patientsFromJson(json);
        }
        inNumber = incoming.size();
        System.out.println(inNumber);
    }

    //Removes a patient from the database
    //Ensures that if they were in a bed then the bed status is made free


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

    public ArrayList<Ward> getAllTransWards(){
        ArrayList<Ward> output = new ArrayList<Ward>();
        output.addAll(dcWards);
        output.addAll(othWards);
        return output;
    }


}
