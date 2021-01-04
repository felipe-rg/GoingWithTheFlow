package Methods;

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
        client = new Client();
        getWardName();
        refresh();
    }

    //Set the variable wardName from input wardId
    public void getWardName() throws IOException {
        ArrayList<String> json = client.makeGetRequest("wardname", "wards", "id="+wardId);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        if(wards.size()!=0) {
            wardName = wards.get(0).getWardName();
        }
        else {
            wardName = "Unknown Ward";
        }
    }

    //Updates local variables with appropriate numbers
    //Assigns colours to beds in ward
    //todo separate to do beds individually, then setBed only has to assign the bed it has changed
    public void bedColours() throws IOException {
        greenBeds =0;
        orangeBeds =0;
        redBeds =0;
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
                json = client.makeGetRequest("*", "patients", "currentbedid="+b.getBedId());
                ArrayList<Patient> patients = client.patientsFromJson(json);
                if(patients.get(0).getEstimatedTimeOfNext()==null){
                    redBeds = redBeds+1;
                    //todo colour bed red
                }
                else {
                    orangeBeds = orangeBeds+1;
                    //todo colour bed orange
                }
            }
        }
    }

    //Updates local variables with appropriate numbers
    public void wardNumbers() throws IOException, SQLException {
        //Incoming patients
        incomingNumber = getIncomingList().size();

        //Discharge patients
        dischargeNumber = getDischargeList().size();

        //Other patients
        otherNumber = getOtherList().size();

        //Patients in ward
        patientsInWard = getPatientList().size();
    }

    public void refresh() throws IOException, SQLException {
       //bedColours();
        wardNumbers();
    }



    //Returns all patients where they're next destination is the input ward
    //Used to see who needs to be accepted/rejected or who needs to be put in a bed once accepted
    public ArrayList<Patient> getIncomingList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination="+wardId);
        return client.patientsFromJson(json);
    }

    public Object[][] getIncomingData() throws IOException, SQLException {
        ArrayList<Patient> patients = getIncomingList();
        Object[][] data = new Object[patients.size()][9];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = dateFormatter(p.getArrivalDateTime());
            data[i][6] = p.getAcceptedByMedicine();
            data[i][7] = "Select Bed";
            data[i][8] = "Delete Patient";
            }
        return data;
    }

    public Object[][] getLSIncomingData() throws IOException, SQLException {
        ArrayList<Patient> patients = getIncomingList();
        Object[][] data = new Object[patients.size()][9];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = dateFormatter(p.getEstimatedTimeOfNext());
            data[i][6] = p.getTransferRequestStatus();
            data[i][7] = "Select Bed";
            data[i][8] = "Delete Patient";
        }
        return data;
    }

    public ArrayList<Patient> getPatientList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        return client.patientsFromJson(json);
    }

    public Object[][] getPatientData() throws IOException, SQLException {
        ArrayList<Patient> patients = getPatientList();
        Object[][] data = new Object[patients.size()][9];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = durationFormatter(Duration.between(p.getArrivalDateTime(), LocalDateTime.now()));
            if(p.getNextDestination()==0){
                data[i][6] = null;
            }
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            ArrayList<Ward> wards = client.wardsFromJson(json);
            if(wards.size()!=0){
                data[i][6] = wards.get(0).getWardName();
            }
            data[i][7] = "Delete Patient";
        }
        return data;
    }

    public String durationFormatter(Duration duration){
        long hours = duration.toHours();
        return String.valueOf(hours);
    }

    public String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    public void deletePatient(int patientId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        int bedid = patients.get(0).getCurrentBedId();
        client.makePutRequest("beds", "status='F'", "bedid="+bedid);
        client.makeDeleteRequest("patients", "id="+patientId);
    }

    //Returns all patients in ward who have had a TTA signoff
    //todo make it not tta but discharge
    //Used to see who will be leaving and when
    public ArrayList<Patient> getDischargeList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("*", "patients", "nextdestination=6");
        ArrayList<Patient> discharging = client.patientsFromJson(json);
        return client.crossReference(patients, discharging);
    }

    public Object[][] getDischargeData() throws IOException, SQLException {
        ArrayList<Patient> patients = getDischargeList();
        Object[][] data = new Object[patients.size()][9];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = p.getTtaSignedOff();
            data[i][6] = p.getSuitableForDischargeLounge();
            data[i][7] = dateFormatter(p.getEstimatedTimeOfNext());
            data[i][8] = "Delete Patient";
        }
        return data;
    }

    //Returns all patients in ward who have died or going to ICU
    //FIXME better way?
    //Used to see who will be leaving and when
    public ArrayList<Patient> getOtherList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "deceased=true");
        ArrayList<Patient> deceased = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "nextdestination=7");
        ArrayList<Patient> toICU = client.patientsFromJson(json);
        ArrayList<Patient> ICUorRIP = client.crossReference(toICU, deceased);
        return client.crossReference(patients, ICUorRIP);
    }

    public Object[][] getOtherData() throws IOException, SQLException {
        ArrayList<Patient> patients = getOtherList();
        Object[][] data = new Object[patients.size()][9];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = dateFormatter(p.getEstimatedTimeOfNext());
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+p.getNextDestination());
            ArrayList<Ward> wards = client.wardsFromJson(json);
            if(wards.size()!=0){
                data[i][6] = wards.get(0).getWardName();
            }
            data[i][8] = "Delete Patient";
        }
        return data;
    }


    // Returns a list of beds in the ward which have the correct characteristics for the chosen patient
    // Used when clicking select bed in incoming table
    public ArrayList<Bed> getAcceptableBeds(int patientId) throws IOException {
        ArrayList<Bed> acceptableBeds = new ArrayList<Bed>();
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> bedsInWard = client.bedsFromJson(json);
        json = client.makeGetRequest("*", "patients", "id="+patientId);
        Patient patientInfo = client.patientsFromJson(json).get(0);
        for(Bed b: bedsInWard){
            if(b.getStatus() == "F") {
                acceptableBeds.add(b);
            }
        }
        return bedsInWard;
    }

    //Changes transfer request status to confirmed
    //Used to accept patients into next destination, setBed will then change their location
    public void acceptIncoming(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "transferrequeststatus='C'", "id="+patientId);
    }

    public void acceptByMedicine(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "acceptedbymedicine=true", "id="+patientId);
    }
    public void rejectByMedicine(int patientId) throws IOException, SQLException {
        client.makePutRequest("patients", "acceptedbymedicine=false", "id="+patientId);
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
        client.makePutRequest("patients", "currentbedid="+bedId, "id="+patientId);
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+bedId);
        int wardid = client.bedsFromJson(json).get(0).getWardId();
        System.out.println(wardid);
        client.makePutRequest("patients", "currentwardid="+wardid, "id="+patientId);
        client.makePutRequest("patients", "nextdestination=0", "id="+patientId);
        client.makePutRequest("beds", "status='O'", "bedid="+bedId);
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
        client.makePutRequest("patients", "currentbedid=0", "id="+patientId);
        client.makePutRequest("beds", "status='F'", "bedid="+bedId);
        client.makePutRequest("patients", "currentwardid=0", "id="+patientId);
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+bedId);
        int wardid = client.bedsFromJson(json).get(0).getWardId();
        client.makePutRequest("patients", "nextdestination="+wardid, "id="+patientId);
        client.makePutRequest("patients", "transferrequeststatus='C'", "id="+patientId);
    }

    //Edits the designated column in the table for the bed
    //Used to edit qualities of bed, eg which sex it is for
    public void editBed(int bedId, String columnId, String newVal) throws IOException, SQLException {
        //String SQLstr = "UPDATE beds SET "+columnId+ " = "+newVal+" WHERE id =" +bedId+";";
        client.makePutRequest("beds", columnId+"="+newVal, "bedid="+bedId);
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
        client.makePutRequest("patients", columnId+"="+newVal, "id="+patientId);
    }

    public void editPatientETON(int patientId, LocalDateTime newVal) throws IOException, SQLException {
        client.makePutRequest("patients", "estimatedatetimeofnext='"+newVal+"'", "id="+patientId);
    }


    //Returns all info about the patient
    //Used to retrieve specific info
    public ArrayList<Patient> getPatientInfo(int patientId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*","patients", "id="+patientId);
        return client.patientsFromJson(json);
    }

    public Patient getPatient(int bedId) throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*","patients", "currentbedid="+bedId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()==0){
            return null;
        }
        return patients.get(0);
    }

    //Changes deceased column to true
    //Used to indicate that a patient has died
    //todo do we need it this specific?
    public void ripPatient(int patientId) throws IOException {
        client.makePutRequest("patients", "deceased=true", "id="+patientId);
    }

    public ArrayList<Bed> getBeds() throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        return client.bedsFromJson(json);
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
