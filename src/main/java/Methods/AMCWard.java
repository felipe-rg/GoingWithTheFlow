package Methods;

import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AMCWard extends GeneralWard{

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
    }
    //If bed is free, bed needs to be green
    //If bed is closed, bed needs to be black
    //If patient is leaving in more than four hours - or no leaving time is set
    //(arrival=leaving) then bed needs to be red
    //If patient is leaving in less than 3 hours, bed needs to be orange
    //if patient is leaving in the past - tehy should have left already -
    //bed needs to be blue
    public String getBedColour(int BedID) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+BedID);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        if(beds.size()==0){
            return "#2ECC71";
        }
        Bed newBed = beds.get(0);
        if(newBed.getStatus().equals("F")){
            changeGreenBeds(1);
            return "#2ECC71";
        }
        if(newBed.getStatus().equals("C")){
            changeRedBeds(1);
            return "#000000";
        }
        else {
            json = client.makeGetRequest("*", "patients", "currentbedid="+newBed.getBedId());
            ArrayList<Patient> patients = client.patientsFromJson(json);
            if(patients.size()==0){
                return "#2ECC71";
            }

            changePatientsInWard(1);
            int dest = patients.get(0).getNextDestination();
            for(int i:dcWardIds){
                if(dest==i){
                    changeDischargeNumber(1);
                }
            }
            for(int i:lsWardIds){
                if(dest==i){
                    changeTransNumber(1);
                }
            }
            for(int i:othWardIds){
                if(dest==i){
                    changeOtherNumber(1);
                }
            }

            LocalDateTime arrival = patients.get(0).getArrivalDateTime();
            LocalDateTime leaving = patients.get(0).getEstimatedTimeOfNext();
            LocalDateTime now = LocalDateTime.now();
            if(arrival.isEqual(leaving)){
                changeRedBeds(1);
                return "#E74C3C";
            }
            else if(leaving.isBefore(now)){
                changeRedBeds(1);
                return "#1531e8";
            }
            else{
                changeOrangeBeds(1);
                return "#F89820";
            }
        }
    }

    //Removes a patient from the database
    //Ensures that if they were in a bed then the bed status is made free
    public void deletePatient(int patientId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        int bedid = patients.get(0).getCurrentBedId();

        if(bedid!=0){
            client.makePutRequest("beds", "status='F'", "bedid="+bedid);
            if(patients.get(0).getCurrentWardId()==wardId) {
                changePatientsInWard(-1);

                LocalDateTime arrival= patients.get(0).getArrivalDateTime();
                LocalDateTime leaving= patients.get(0).getEstimatedTimeOfNext();

                if(arrival.isEqual(leaving)){
                    bedStatus[2] = bedStatus[2] -1;
                    bedStat.setRedBedsNum(bedStatus[2]);
                    bedStatus[0] = bedStatus[0] +1;
                    bedStat.setGreenBedsNum(bedStatus[0]);
                }
                else {
                    bedStatus[1] = bedStatus[1] -1;
                    bedStat.setAmbarBedsNum(bedStatus[1]);
                    bedStatus[0] = bedStatus[0] +1;
                    bedStat.setGreenBedsNum(bedStatus[0]);
                }

                int dest = patients.get(0).getNextDestination();
                //if they are in a bed, and in the ward, they could be on another list
                for(int i:dcWardIds){
                    if(dest==i){
                        changeDischargeNumber(-1);
                        wardInfo.setDisText();
                    }
                }
                for(int i:lsWardIds){
                    if(dest==i){
                        changeTransNumber(-1);
                        wardInfo.setTransText();
                    }
                }
                for(int i:othWardIds){
                    if(dest==i){
                        changeOtherNumber(-1);
                        wardInfo.setOthText();
                    }
                }
            } else {
                //If they are in a bed, not in the ward, being deleted, then they are on incoming list
                changeIncomingNumber(-1);
                wardInfo.setInText();
            }
        }else {
            //Not in a bed, must be on incoming list
            changeIncomingNumber(-1);
            wardInfo.setInText();
        }
        client.makeDeleteRequest("patients", "id="+patientId);
    }

}
