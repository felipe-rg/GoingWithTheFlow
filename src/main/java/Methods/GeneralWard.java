package Methods;

import AMCWardPanels.BedStatus;
import AMCWardPanels.Topography;
import AMCWardPanels.WardInfo;
import Client.*;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

//Holds most methods used for communication to the database
//Instantiated when a ward is chosen - implemented as AMC or longstay

public abstract class GeneralWard {
    protected Client client;
    public int wardId;
    private String wardName;
    private Patient patient;
    private Bed bed;
    private int[] bedStatus;
    private int patientsInWard;
    private int[] amuWardIds;
    private int[] lsWardIds;
    private int[] dcWardIds;
    private int[] othWardIds;
    private int dischargeNumber;
    private int othNumber;
    private int inNumber;
    //TODO in AMCward?
    private int transNumber;

    BedStatus bedStat;
    Topography topography;
    WardInfo wardInfo;

    //Constructor creates a client to link to the database
    //Instantiates the local variables for homescreen numbers or use in methods
    public GeneralWard(int wardId) throws IOException, SQLException {
        this.wardId = wardId;
        client = new Client();
        wardName = getWardName(wardId);
        bedStatus = new int[3];
        bedStatus[0] = 0;
        bedStatus[1] = 0;
        bedStatus[2] = 0;

        ArrayList json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
        ArrayList<Ward> amuWards = client.wardsFromJson(json);
        amuWardIds = new int[amuWards.size()];
        for(int i=0; i<amuWards.size(); i++){
            amuWardIds[i] = amuWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "wards", "wardtype='LS'");
        ArrayList<Ward> lsWards = client.wardsFromJson(json);
        lsWardIds = new int[lsWards.size()];
        for(int i=0; i<lsWards.size(); i++){
            lsWardIds[i] = lsWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "wards", "wardtype='discharge'");
        ArrayList<Ward> dcWards = client.wardsFromJson(json);
        dcWardIds = new int[dcWards.size()];
        for(int i=0; i<dcWards.size(); i++){
            dcWardIds[i] = dcWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "wards", "wardtype='other'");
        ArrayList<Ward> othWards = client.wardsFromJson(json);
        othWardIds = new int[othWards.size()];
        for(int i=0; i<othWards.size(); i++){
            othWardIds[i] = othWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "patients", "nextdestination="+wardId);
        if(json.size()!=0){
            ArrayList<Patient> incoming = client.patientsFromJson(json);
            inNumber = incoming.size();
        }
    }

    public int getWardId(){return wardId;}
    public Client getClient(){return client;}

    //Set the variable wardName from input wardId
    public String getWardName(int wardID) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+wardID);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        String output = null;
        if(wards.size()!=0) {
            output = wards.get(0).getWardName();
        }
        else {
            output = "No Destination";
        }
        return output;
    }

    public String getWardType(int wardID) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+wardID);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        String output = null;
        if(wards.size()!=0) {
            output = wards.get(0).getWardType();
        }
        else {
            output = "No Ward";
        }
        return output;
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

    // Returns a list of beds in the ward which have the correct characteristics for the chosen patient
    // Used when clicking select bed in incoming table
    //FIXME
    public ArrayList<Bed> getAcceptableBeds(int patientId) throws IOException {
        ArrayList<Bed> acceptableBeds = new ArrayList<Bed>();
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> bedsInWard = client.bedsFromJson(json);
        json = client.makeGetRequest("*", "patients", "id="+patientId);
        Patient patientInfo = client.patientsFromJson(json).get(0);
        for(Bed b: bedsInWard){
            if(b.getStatus().equals("F") && b.getHasSideRoom() == patientInfo.getNeedsSideRoom() && b.getForSex().equals(patientInfo.getSex())) {
                acceptableBeds.add(b);
            }
        }
        return acceptableBeds;
    }

    //Used to assign a bed and change patient location
    public void setBed(int patientId, int bedId) throws IOException, SQLException {

        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> pats = client.patientsFromJson(json);
        LocalDateTime arrival = null;
        if(pats.size()!=0){
            arrival = pats.get(0).getArrivalDateTime();
        }
        //Sets time of next to time of arrival which means the bed colour is red
        client.makePutRequest("patients", "estimateddatetimeofnext='"+arrival+"'", "id="+patientId);

        //Changes patient bedid to bedId
        client.makePutRequest("patients", "currentbedid="+bedId, "id="+patientId);
        //Changes patient current location to where the bed is
        client.makePutRequest("patients", "currentwardid="+wardId, "id="+patientId);
        //Changes patient next destination to null
        client.makePutRequest("patients", "nextdestination=0", "id="+patientId);
        //Changes bed status to occupied
        client.makePutRequest("beds", "status='O'", "bedid="+bedId);

        changePatientsInWard(1);
        changeIncomingNumber(-1);
        bedStatus[2] = bedStatus[2] +1;
        bedStat.setRedBedsNum(bedStatus[2]);
        bedStatus[0] = bedStatus[0] -1;
        bedStat.setGreenBedsNum(bedStatus[0]);

    }

    //Used to undo a setBed and will still appear on incoming list
    public void removePatient(int patientId, int bedId) throws IOException, SQLException {
        //Changes patient's bedid to null
        client.makePutRequest("patients", "currentbedid=0", "id="+patientId);
        //Changes beds status to free
        client.makePutRequest("beds", "status='F'", "bedid="+bedId);
        //Changes patient's current location to null
        client.makePutRequest("patients", "currentwardid=0", "id="+patientId);
        //Changes patient's next location to current ward
        client.makePutRequest("patients", "nextdestination="+wardId, "id="+patientId);
        //Changes patient's transfer request status to confirmed
        client.makePutRequest("patients", "transferrequeststatus='C'", "id="+patientId);

        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
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

        changeIncomingNumber(1);
        wardInfo.setInText();
        changePatientsInWard(-1);
        wardInfo.setTotText();

    }

    //Edits the designated column in the table for the bed
    //Column names in table need to be known
    public void editBed(int bedId, String columnId, String newVal) throws IOException{
        client.makePutRequest("beds", columnId+"="+newVal, "bedid="+bedId);
    }

    //Edits the designated column in the table for the patient
    //Column names in table need to be known
    public void editPatient(int patientId, String columnId, String newVal) throws IOException, SQLException {
        client.makePutRequest("patients", columnId+"="+newVal, "id="+patientId);
    }

    //Returns the patient in the specified bed
    public Patient getPatient(int bedId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*","patients", "currentbedid="+bedId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()==0){
            return null;
        }
        return patients.get(0);
    }

    //Returns all beds in ward, used for initialisation
    public ArrayList<Bed> getBeds() throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        return client.bedsFromJson(json);
    }

    //Returns information about a bed, given the id
    public Bed getBed(int bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+bedId);
        return restOfGetBed(json);
    }

    private Bed restOfGetBed(ArrayList<String> json){
        ArrayList<Bed> beds = client.bedsFromJson(json);
        if(beds.size()==0){
            System.out.println("No bed found");
            return null;
        }
        return beds.get(0);
    }
    //Returns information about a bed, given the id
    public Bed getBed(String bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+bedId);
        return restOfGetBed(json);
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
            bedStatus[0] = bedStatus[0] +1;
            return "#2ECC71";
        }
        if(newBed.getStatus().equals("C")){
            bedStatus[2] = bedStatus[2] +1;
            return "#000000";
        }
        else {
            json = client.makeGetRequest("*", "patients", "currentbedid="+newBed.getBedId());
            ArrayList<Patient> patients = client.patientsFromJson(json);
            if(patients.size()==0){
                return "#2ECC71";
            }

            patientsInWard = patientsInWard + 1;
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
                bedStatus[2] = bedStatus[2] +1;
                return "#E74C3C";
            }
            else if(leaving.isBefore(now)){
                bedStatus[2] = bedStatus[2] +1;
                return "#1531e8";
            }
            else{
                bedStatus[1] = bedStatus[1] +1;
                return "#F89820";
            }
        }
    }

    //Outputs an array of three integers which holds the number of
    // green, orange, or red beds
    public int[] getBedStatus() throws IOException {
        return bedStatus;
    }

    public void changeGreenBeds(int i){
        bedStatus[0] = bedStatus[0] + i;
        bedStat.setGreenBedsNum(bedStatus[0]);
    }
    public void changeOrangeBeds(int i){
        bedStatus[1] = bedStatus[1] + i;
        bedStat.setAmbarBedsNum(bedStatus[1]);
    }
    public void changeRedBeds(int i){
        bedStatus[2] = bedStatus[2] + i;
        bedStat.setRedBedsNum(bedStatus[2]);
    }

    public int getPatientsInWard(){
        return patientsInWard;
    };
    public int[] getAmuWardIds(){
        return amuWardIds;
    };
    public int[] getLsWardIds(){
        return lsWardIds;
    };
    public int[] getDcWardIds(){
        return dcWardIds;
    };
    public int[] getOthWardIds(){
        return othWardIds;
    };
    public int getDischargeNumber(){
        return dischargeNumber;
    };
    public int getOthNumber(){
        return othNumber;
    };
    public int getInNumber(){
        return inNumber;
    };
    //TODO in AMCward?
    public int getTransNumber(){
        return transNumber;
    };

    public void changeDischargeNumber(int i){
        dischargeNumber = dischargeNumber+i;
    }
    public void changeIncomingNumber(int i){
        inNumber = inNumber+i;
    }
    public void changeOtherNumber(int i){
        othNumber = othNumber+i;
    }
    public void changePatientsInWard(int i){
        patientsInWard = patientsInWard+i;
    }
    public void changeTransNumber(int i){
        transNumber = transNumber+i;
    }

    public void setBedStat(BedStatus stat){
        bedStat = stat;
    }

    public BedStatus getBedStat(){
        return bedStat;
    }

    public void setTopography(Topography top){
        topography = top;
    }

    public Topography getTopography(){
        return topography;
    }

    public void setWardInfo(WardInfo wardinfo){
        wardInfo = wardinfo;
    }

    public WardInfo getWardInfo(){
        return wardInfo;
    }

}
