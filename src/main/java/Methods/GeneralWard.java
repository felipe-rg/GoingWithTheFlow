package Methods;

import Client.*;

import com.google.gson.Gson;
import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public abstract class GeneralWard {
    public Client client;
    public int wardId;
    private String wardName;
    public Patient patient;
    public Bed bed;
    public int greenBeds;
    public int orangeBeds;
    public int redBeds;
    public int incomingNumber;
    public int dischargeNumber;
    public int otherNumber;
    public int patientsInWard;


    public GeneralWard(int wardId) throws IOException, SQLException {
        this.wardId = wardId;
        getWardName();
        client = new Client();
        refresh();
    }

    //Set the variable wardName from input wardId
    public void getWardName() throws IOException {
        ArrayList<String> json = client.makeGetRequest("wardname", "wards", "id="+wardId);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        wardName = wards.get(0).getWardName();
    }

    //Updates local variables with appropriate numbers
    //Assigns colours to beds in ward
    //todo separate to do beds individually, then setBed only has to assign the bed it has changed
    public void bedColours() throws IOException {
        greenBeds =0;
        orangeBeds =0;
        redBeds =0;
        patientsInWard=0;
        //get bed Ids for hardcoded bed numbers / bednumber = bedid
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        //Todo colour beds - talk to clara about how she wants to do this

        for(Bed b:beds){
            if(b.getStatus()=="F"){
                //add to free beds
                greenBeds = greenBeds+1;
                //todo colour bed green
            }
            else if(b.getStatus()=="C"){
                redBeds = redBeds+1;
                //todo colour bed red with slash
            }
            else {
                json = client.makeGetRequest("*", "patients", "bedid="+b.getId());
                ArrayList<Patient> patients = client.patientsFromJson(json);
                if(patients.get(0).getEstimatedTimeOfNext()==null){
                    redBeds = redBeds+1;
                    //add to patient in ward
                    patientsInWard = patientsInWard+1;
                    //todo colour bed red
                }
                else {
                    orangeBeds = orangeBeds+1;
                    //add to patient in ward
                    patientsInWard = patientsInWard+1;
                    //todo colour bed orange
                }
            }
        }
    }

    //Updates local variables with appropriate numbers
    public void wardNumbers() throws IOException, SQLException {
        //Incoming patients
        incomingNumber = getIncomingList(wardId).size();

        //Discharge patients
        dischargeNumber = getDischargeList(wardId).size();

        //Other patients
        otherNumber = getOtherList(wardId).size();
    }

    public void refresh() throws IOException, SQLException {
        bedColours();
        wardNumbers();
    }



    //Returns all patients where they're next destination is the input ward
    //Used to see who needs to be accepted/rejected or who needs to be put in a bed once accepted
    public ArrayList<Patient> getIncomingList(int wardId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("id", "patients", "nextDestination="+wardId);
        return client.patientsFromJson(json);
    }

    //Returns all patients in ward who have had a TTA signoff
    //todo make it not tta but discharge
    //Used to see who will be leaving and when
    public ArrayList<Patient> getDischargeList(int wardId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "ttasignedoff=TRUE");
        ArrayList<Patient> discharging = client.patientsFromJson(json);
        return client.crossReference(patients, discharging);
    }

    //Returns all patients in ward who have died
    //todo make it not just dying
    //Used to see who will be leaving and when
    public ArrayList<Patient> getOtherList(int wardId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "deceased=true");
        ArrayList<Patient> deceased = client.patientsFromJson(json);
        return client.crossReference(patients, deceased);
    }

    //Changes transfer request status to confirmed
    //Used to accept patients into next destination, setBed will then change their location
    public void acceptIncoming(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "transferrequeststatus='C'", "id="+patientId);
    }

    //Changes transfer request status to rejected
    //Used to reject patients from next destination, need to then request new next location
    public void rejectIncoming(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "transferrequeststatus='D'", "id="+patientId);
        //todo: alert of rejection to location or request new ward
    }

    //Changes patient current location to where the bed is
    //Changes patient bedid to bedId
    //Changes patient next location to null
    //Changes bed status to occupied
    //Refreshes ward to update numbers
    //Used to assign a bed and change patient location
    public void setBed(int patientId, int bedId) throws IOException, SQLException {
        client.makePutRequest("patients", "bedid="+bedId, "id="+patientId);
        ArrayList<String> json = client.makeGetRequest("wardid", "beds", "id="+bedId);
        int wardid = client.patientsFromJson(json).get(0).getId();
        client.makePutRequest("patients", "currentlocation="+wardid, "id="+patientId);
        client.makePutRequest("patients", "nextlocation=NULL", "id="+patientId);
        client.makePutRequest("beds", "status='O'", "id="+bedId);
        refresh();
    }

    //Deletes patient from database
    //todo organise new way of discharging and then deleting maybe?
    //Used to discharge patients
    public void discharge(int patientId) throws IOException, SQLException {
        String SQLstr = "DELETE FROM patients WHERE id="+patientId+";";
        client.makeDeleteRequest("patients", "id="+patientId);
    }

    //Returns all patients in the ward
    //Used for analysis
    public ArrayList<Patient> getAllPatients(int wardId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest( "*", "patients", "currentlocation="+wardId);
        return client.patientsFromJson(json);
    }

    //Changes patient's bedid to null
    //Changes beds status to free
    //Changes patient's current location to null
    //Changes patient's next location to current ward
    //Changes patient's transfer request status to confirmed
    //Used to undo a setBed and will still appear on incoming list
    public void removePatient(int patientId, int bedId) throws IOException, SQLException {
        client.makePutRequest("patients", "bedid=Null", "id="+patientId);
        client.makePutRequest("beds", "occupied='F'", "id="+bedId);
        client.makePutRequest("patients", "currentlocation=Null", "id="+patientId);
        ArrayList<String> json = client.makeGetRequest("wardid", "beds", "id="+bedId);
        int wardid = client.patientsFromJson(json).get(0).getId();
        client.makePutRequest("patients", "nextdestination="+wardid, "id="+patientId);
        client.makePutRequest("patients", "transferrequeststatus='C'", "id="+patientId);
    }

    //Edits the designated column in the table for the bed
    //Used to edit qualities of bed, eg which sex it is for
    public void editBed(int bedId, String columnId, String newVal) throws IOException, SQLException {
        //String SQLstr = "UPDATE beds SET "+columnId+ " = "+newVal+" WHERE id =" +bedId+";";
        client.makePutRequest("beds", columnId+"="+newVal, "id="+bedId);
    }

    //todo do we need this?
    public void isBedFree(int bedId, String answer) throws IOException, SQLException {
        editBed(bedId, "occupied", answer);
        if(answer == "Y") {
            ArrayList<String> json = client.makeGetRequest("id", "patients", "bedid=" + bedId);
            ArrayList<Patient> patients = client.patientsFromJson(json);
            int patientId = patients.get(0).getId();
            editPatient(patientId, "bedid", null);
            //TODO make next destination current destination, or other
        }
        //todo: time
        String time = "1600";
        editBed(bedId, "leavingTime", time);
    }

    //Edits the designated column in the table for the patient
    //Used to edit qualities of patient, eg their sex
    public void editPatient(int patientId, String columnId, String newVal) throws IOException, SQLException {
        //String SQLstr = "UPDATE patients SET "+columnId+ " = "+newVal+" WHERE id =" +patientId+";";
        client.makePutRequest("patients", columnId="="+newVal, "id="+patientId);
    }

    //Returns all info about the patient
    //Used to retrieve specific info
    public ArrayList<Patient> getPatientInfo(int patientId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*","patients", "id="+patientId);
        return client.patientsFromJson(json);
    }

    //Changes deceased column to true
    //Used to indicate that a patient has died
    //todo do we need it this specific?
    public void ripPatient(int patientId) throws IOException {
        client.makePutRequest("patients", "deceased=true", "id="+patientId);
    }

    //Returns the beds in the ward
    //Used for analysis and setup
    //todo why is this needed?
    public void emptyBed(int bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "id="+bedId);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        bed = beds.get(0);
    }

    //Returns all patients in
    //todo why is this needed?
    public void filledBed(int bedId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "bedid="+bedId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        patient = patients.get(0);
    }

}
