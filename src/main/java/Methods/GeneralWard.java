package Methods;

import AMCWardPanels.BedStatus;
import AMCWardPanels.Topography;
import AMCWardPanels.WardInfo;
import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.logging.Logger;

    /*Holds methods for communication to the database and between panels in the amc and long stay wards
    This class is responsible for real time editing of the database, and ward GUIs */

public abstract class GeneralWard{
    protected Client client;
    protected int wardId;
    private String wardName;

    protected int[] bedStatus;
    protected int patientsInWard;
    protected ArrayList<Ward> dcWards;
    protected ArrayList<Ward> othWards;
    protected ArrayList<Ward> lsWards;
    protected ArrayList<Ward> amuWards;
    protected int[] amuWardIds;
    protected int[] lsWardIds;
    protected int[] dcWardIds;
    protected int[] othWardIds;
    protected int dischargeNumber;
    protected int othNumber;
    protected int inNumber;

    BedStatus bedStat;
    Topography topography;
    WardInfo wardInfo;

    private static final Logger log= Logger.getLogger(GeneralWard.class.getName());

    //Constructor creates a client to link to the database
    //Instantiates the local variables for homescreen numbers or use in methods
    //Finds lists of ward types
    public GeneralWard(int wardId) throws IOException, SQLException {
        this.wardId = wardId;
        client = new Client();
        log.info("Client Successfully created");
        wardName = getWardName(wardId);
        bedStatus = new int[5];

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
        amuWards = client.wardsFromJson(json);
        amuWardIds = new int[amuWards.size()];
        for(int i=0; i<amuWards.size(); i++){
            amuWardIds[i] = amuWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "wards", "wardtype='LS'");
        lsWards = client.wardsFromJson(json);
        lsWardIds = new int[lsWards.size()];
        for(int i=0; i<lsWards.size(); i++){
            lsWardIds[i] = lsWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "wards", "wardtype='discharge'");
        dcWards = client.wardsFromJson(json);
        dcWardIds = new int[dcWards.size()];
        for(int i=0; i<dcWards.size(); i++){
            dcWardIds[i] = dcWards.get(i).getWardId();
        }

        json = client.makeGetRequest("*", "wards", "wardtype='other'");
        othWards = client.wardsFromJson(json);
        othWardIds = new int[othWards.size()];
        for(int i=0; i<othWards.size(); i++){
            othWardIds[i] = othWards.get(i).getWardId();
        }
    }

    // Returns a list of beds in the ward which have the correct characteristics for the chosen patient
    // Used when clicking select bed in incoming table
    public ArrayList<Bed> getAcceptableBeds(int patientId) throws IOException {
        ArrayList<Bed> acceptableBeds = new ArrayList<Bed>();

        //Bed must be in this ward
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> bedsInWard = client.bedsFromJson(json);

        //We need the patient info to match the bed
        json = client.makeGetRequest("*", "patients", "id="+patientId);
        Patient patientInfo = client.patientsFromJson(json).get(0);

        for(Bed b: bedsInWard){
            if(!patientInfo.getNeedsSideRoom()) {
                if (b.getStatus().equals("F") && b.getForSex().equals(patientInfo.getSex())) {
                    acceptableBeds.add(b); //These are free, correct side room, correct sex
                } else if (b.getStatus().equals("F") && b.getForSex().equals("Uni")) {
                    acceptableBeds.add(b); //These are free, correct side room, uni sex
                }
            }else {
                if (b.getStatus().equals("F") && b.getHasSideRoom()&& b.getForSex().equals(patientInfo.getSex())) {
                    acceptableBeds.add(b); //These are free, correct side room, correct sex
                } else if (b.getStatus().equals("F") && b.getHasSideRoom() && b.getForSex().equals("Uni")) {
                    acceptableBeds.add(b); //These are free, correct side room, uni sex
                }
            }
        }
        return acceptableBeds;
    }

    //Removes a patient from the database
    //Ensures everything remains functioning
    public void deletePatient(int patientId) throws IOException {

        //Get patient info
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        int bedid = patients.get(0).getCurrentBedId();
        // If the patient was in a bed we need to edit bed info
        if(bedid!=0){
            //Free bed
            client.makePutRequest("beds", "status='F'", "bedid="+bedid);
            topography.makeBedButtonGreen(bedid);

            //If the patient is in this ward, we need to update the ward numbers
            if(patients.get(0).getCurrentWardId()==wardId) {

                LocalDateTime arrival= patients.get(0).getArrivalDateTime();
                LocalDateTime leaving= patients.get(0).getEstimatedTimeOfNext();

                //Updates numbers to do with deletion
                deletePatientUpdateNumbers(arrival, leaving);

                int dest = patients.get(0).getNextDestination();
                //if they are in a bed, and in the ward, they could be on another list
                updateDestinationNumber(dest, -1);

            } else {
                //If they are in a bed, not in the ward, being deleted, then they are on incoming list
                changeIncomingNumber(-1);
            }
        }
        //Not in a bed, must be on incoming list
        changeIncomingNumber(-1);

        //Delete patient from database
        client.makeDeleteRequest("patients", "id="+patientId);
        log.info("Patient successfully deleted");
    }

    public void transferPatient(int patientId, String wardName){
        try {
            //Get ward info
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardname='"+wardName+"'");
            Ward ward = client.wardsFromJson(json).get(0);
            //Update destination number
            json = client.makeGetRequest("*", "patients", "id="+patientId);
            Patient patient = client.patientsFromJson(json).get(0);
            int oldDest = patient.getNextDestination();
            if(oldDest != 0){
                updateDestinationNumber(oldDest, -1);
            }
            updateDestinationNumber(ward.getWardId(), 1);
            //change patient destination
            client.makePutRequest("patients", "nextdestination="+ward.getWardId(), "id="+patientId);
            //change transfer request status
            client.makePutRequest("patients", "transferrequeststatus='P'", "id="+patientId);
            log.info("Patient Successfully transferred");
        } catch (IOException e) {
            e.printStackTrace();
            log.warning("Patient not transferred");
        }

    }


    //Finds bed colour for each bedbutton
    //Whilst doing this, initialise most of the numbers for the ward
    public String getBedColour(int BedID) throws IOException {
        //Gets bed info
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+BedID);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        //If there is no bed then there is a problem we need to fix
        if(beds.size()==0){
            return null;
        }
        Bed newBed = beds.get(0);
        //If the status is free then the bed is green
        if(newBed.getStatus().equals("F")){
            changeGreenBeds(1);
            return "#2ECC71"; //Green
        }
        //If the status is closed then the bed is black
        if(newBed.getStatus().equals("C")){
            changeBlackBeds(1);
            return "#000000"; //black
        }
        else {
            //If not closed or free, the bed is occupied and we get the patient info
            json = client.makeGetRequest("*", "patients", "currentbedid="+newBed.getBedId());
            ArrayList<Patient> patients = client.patientsFromJson(json);;
            //If no patient then there is an issue to fix
            if(patients.size()==0){
                return null;
            }
            //If a patient is found, increase number of patients in the ward
            changePatientsInWard(1);

            //The patient could be on a new destination list as well
            int dest = patients.get(0).getNextDestination();
            updateDestinationNumber(dest, 1);

            //Gte arrival and leaving times from the patient
            LocalDateTime arrival = patients.get(0).getArrivalDateTime();
            LocalDateTime leaving = patients.get(0).getEstimatedTimeOfNext();
            LocalDateTime now = LocalDateTime.now();

            //If arrival is the same as leaving then no leaving time has been made and the bed is red
            if(arrival.isEqual(leaving)){
                changeRedBeds(1);
                return "#E74C3C"; //Red
            }
            //if leaving is in the past then the bed is blue
            else if(leaving.isBefore(now)){
                changeBlueBeds(1);
                return "#1531e8";//Blue
            }
            //If the arrival is different from leaving and is in the future then the bed is orange
            else{
                changeOrangeBeds(1);
                return "#F89820"; //orange
            }
        }
    }

    //Used to assign a bed and change patient location
    public void setBed(int patientId, int bedId) throws IOException, SQLException {

        //Get patient info
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> pats = client.patientsFromJson(json);
        LocalDateTime arrival = null;

        //Find the arrival time and bedid of the patient
        if(pats.size()!=0){
            arrival = pats.get(0).getArrivalDateTime();

            int oldBed = pats.get(0).getCurrentBedId();
            if(oldBed!=0){
                //Frees the bed the patient is currently in
                client.makePutRequest("beds", "status='F'", "bedid="+oldBed);
            }
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

        topography.makeBedButtonRed(bedId); //Immediately changes bed colour

        changePatientsInWard(1); //Immediately increases number of patients in the ward
        changeIncomingNumber(-1); //Immediately decreases number of patients incoming to the ward
        changeRedBeds(1); //Immediately increases number of red beds in the ward
        changeGreenBeds(-1); //Immediately decreases number of green beds in the ward

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

        //Get patient information
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);

        //Find whether the patient is supposed to be leaving
        LocalDateTime arrival= patients.get(0).getArrivalDateTime();
        LocalDateTime leaving= patients.get(0).getEstimatedTimeOfNext();

        deletePatientUpdateNumbers(arrival, leaving); //Updates numbers on ward app
        changeIncomingNumber(1);  //Increases incoming number

        topography.makeBedButtonGreen(bedId); //Immediately makes bed green
        log.info("Patient successfully removed");
    }

    //Used to change numbers on ward app when a patient is deleted or removed
    public void deletePatientUpdateNumbers(LocalDateTime arrival, LocalDateTime leaving){
        changePatientsInWard(-1);
        changeGreenBeds(1);
        if(arrival.isEqual(leaving)){
            changeRedBeds(-1);
        }
        else if(leaving.isBefore(LocalDateTime.now())){
            changeBlueBeds(-1);
        }
        else {
            changeOrangeBeds(-1);
        }
    }


    //Gets the wardName for any ward
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

    //Gets the ward type for any ward
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


    //Edits the designated column in the table for the bed
    //Column names in table need to be known
    public void editBed(int bedId, String columnId, String newVal) throws IOException{
        client.makePutRequest("beds", columnId+"="+newVal, "bedid="+bedId);
        log.info("Bed Successfully editted");
    }

    //Edits the designated column in the table for the patient
    //Column names in table need to be known
    public void editPatient(int patientId, String columnId, String newVal) throws IOException, SQLException {
        client.makePutRequest("patients", columnId+"="+newVal, "id="+patientId);
        log.info("Patient Successfully editted");
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

    //Returns information about a bed, given the id
    public Bed getBed(String bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+bedId);
        return restOfGetBed(json);
    }

    //finishes getBed function
    private Bed restOfGetBed(ArrayList<String> json){
        ArrayList<Bed> beds = client.bedsFromJson(json);
        if(beds.size()==0){
            System.out.println("No bed found");
            return null;
        }
        return beds.get(0);
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

    public void changeBlueBeds(int i){
        bedStatus[3] = bedStatus[3] + i;
        bedStat.setBlueBedsNum(bedStatus[3]);
    }

    public void changeBlackBeds(int i){
        bedStatus[4] = bedStatus[4] + i;
        bedStat.setBlackBedsNum(bedStatus[4]);
    }

    abstract void updateDestinationNumber(int dest, int number);
    public abstract ArrayList<Ward> getAllTransWards();
    public int getWardId(){return wardId;}
    public Client getClient(){return client;}

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

    public void changeDischargeNumber(int i){
        dischargeNumber = dischargeNumber+i;
        wardInfo.setDisText(String.valueOf(dischargeNumber));
    }
    public void changeIncomingNumber(int i){
        inNumber = inNumber+i;
        wardInfo.setInText(String.valueOf(inNumber));
    }
    public void changeOtherNumber(int i){
        othNumber = othNumber+i;
        wardInfo.setOthText(String.valueOf(othNumber));
    }
    public void changePatientsInWard(int i){
        patientsInWard = patientsInWard+i;
        wardInfo.setTotText(String.valueOf(patientsInWard));
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
