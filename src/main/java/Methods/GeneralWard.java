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

//Holds most methods used for communication to the database
//Instantiated when a ward is chosen - implemented as AMC or longstay

public abstract class GeneralWard {

    protected Client client;
    public int wardId;
    private String wardName;
    private Patient patient;
    private Bed bed;
    private int incomingNumber;
    private int dischargeNumber;
    private int otherNumber;
    private int patientsInWard;

    //Constructor creates a client to link to the database
    //Instantiates the local variables for homescreen numbers or use in methods
    public GeneralWard(int wardId) throws IOException, SQLException {
        this.wardId = wardId;
        client = new Client();
        wardName = getWardName(wardId);
        wardNumbers();
    }

    public int getIncomingNumber(){return incomingNumber;}
    public int getDischargeNumber(){return dischargeNumber;}
    public int getOtherNumber(){return otherNumber;}
    public int getPatientsInWard(){return patientsInWard;}
    public int getWardId(){return wardId;}

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



    //Returns all patients where they're next destination is the current ward
    //Used in getIncomingData and local incomingNumber
    public ArrayList<Patient> getIncomingList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination="+wardId);
        return client.patientsFromJson(json);
    }

    //Returns an object to be used in the incoming table of the amc app
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
            data[i][5] = p.getArrivalDateTime();
            data[i][6] = p.getAcceptedByMedicine();
            data[i][7] = "Select Bed";
            data[i][8] = "Delete Patient";
            }
        return data;
    }

    //returns an object to be used in the incoming table of the long stay apps
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

    //Returns all patients in the ward
    //Used in getPatientData and local patientsInWard
    public ArrayList<Patient> getPatientList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        return client.patientsFromJson(json);
    }

    //returns an object to be used in the total patients in ward tables
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


    //Returns all patients in ward who are going to be discharged
    //Used in getDischargeData and local dischargeNumber
    public ArrayList<Patient> getDischargeList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("*", "patients", "nextdestination=6");
        ArrayList<Patient> discharging = client.patientsFromJson(json);
        return client.crossReference(patients, discharging);
    }

    //Returns an object to be used in the discharge tables
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
    //Used in getOtherData and local othernumber
    public ArrayList<Patient> getOtherList() throws IOException, SQLException {
        ArrayList<Patient> output = new ArrayList<Patient>();
        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "deceased=true");
        ArrayList<Patient> deceased = client.patientsFromJson(json);
        output = client.crossReference(patients, deceased);
        json = client.makeGetRequest("id", "patients", "nextdestination=7");
        ArrayList<Patient> toICU = client.patientsFromJson(json);
        ArrayList<Patient> ICUFromWard = client.crossReference(toICU, patients);
        for(Patient p:ICUFromWard){
            output.add(p);
        }
        return output;
    }

    //returns an object to be used in the other table
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


    //Needed to format the time difference in getPatientData
    public String durationFormatter(Duration duration){
        long hours = duration.toHours();
        return String.valueOf(hours);
    }

    //Needed to format the times used in tables
    public String dateFormatter(LocalDateTime localDateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return localDateTime.format(formatter);
    }

    //Removes a patient from the database
    //Ensures that if they were in a bed then the bed status is made free
    public void deletePatient(int patientId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        int bedid = patients.get(0).getCurrentBedId();
        client.makePutRequest("beds", "status='F'", "bedid="+bedid);
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
        //Changes patient bedid to bedId
        client.makePutRequest("patients", "currentbedid="+bedId, "id="+patientId);
        //Changes patient current location to where the bed is
        client.makePutRequest("patients", "currentwardid="+wardId, "id="+patientId);
        //Changes patient next destination to null
        client.makePutRequest("patients", "nextdestination=0", "id="+patientId);
        //Changes bed status to occupied
        client.makePutRequest("beds", "status='O'", "bedid="+bedId);
        //Refreshes ward to update numbers
        wardNumbers();
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
        ArrayList<Bed> beds = client.bedsFromJson(json);
        if(beds.size()==0){
            System.out.println("No bed found");
            return null;
        }
        return beds.get(0);
    }

    public String getIncomingColour(int patientId) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "id="+patientId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        if(patients.size()==0){
            System.out.println("No patient found");
            return null;
        }
        LocalDateTime arrival = patients.get(0).getArrivalDateTime();
        LocalDateTime now = LocalDateTime.now();
        if(arrival.isBefore(now.plusHours(-3))){
            return "#E74C3C";
        }
        if(arrival.isBefore(now.plusHours(-2))){
            return "F89820";
        }
        else {
            return "#2ECC71";
        }

    }


    public String getBedColour(int BedID) throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "beds", "bedid="+BedID);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        if(beds.size()==0){
            return "#2ECC71";
        }
        Bed newBed = beds.get(0);
        if(newBed.getStatus().equals("F")){
            return "#2ECC71";
        }
        if(newBed.getStatus().equals("C")){
            return "#000000";
        }
        else {
            json = client.makeGetRequest("*", "patients", "currentbedid="+newBed.getBedId());
            ArrayList<Patient> patients = client.patientsFromJson(json);
            if(patients.size()==0){
                return "#2ECC71";
            }
            LocalDateTime arrival = patients.get(0).getArrivalDateTime();
            LocalDateTime leaving = patients.get(0).getEstimatedTimeOfNext();
            LocalDateTime now = LocalDateTime.now();
            if(arrival.isEqual(leaving) || leaving.isAfter(now.plusHours(4))){
                return "#E74C3C";
            }
            else if(leaving.isBefore(now)){
                return "#1531e8";
            }
            else{
                return "#F89820";
            }
        }
    }

    public int[] getBedStatus() throws IOException {
        int[] output = new int[3];
        ArrayList<String> json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> beds = client.bedsFromJson(json);
        if(beds.size()==0){
            output[0] = 0;
            output[1] = 0;
            output[2] = 0;
        }
        for(Bed newBed:beds) {
            if (newBed.getStatus().equals("F")) {
                output[0] = output[0] + 1;
            }
            if (newBed.getStatus().equals("C")) {
                output[2] = output[2] + 1;
            }
            if(newBed.getStatus().equals("O")) {
                json = client.makeGetRequest("*", "patients", "currentbedid=" + newBed.getBedId());
                ArrayList<Patient> patients = client.patientsFromJson(json);
                if (patients.size() == 0) {
                   continue;
                }
                LocalDateTime arrival = patients.get(0).getArrivalDateTime();
                LocalDateTime leaving = patients.get(0).getEstimatedTimeOfNext();
                LocalDateTime now = LocalDateTime.now();
                if (arrival.isEqual(leaving) || leaving.isAfter(now.plusHours(4)) || leaving.isBefore(now)) {
                    output[2] = output[2] + 1;
                } else {
                    output[1] = output[1] + 1;
                    System.out.println(newBed.getBedId());
                }
            }
        }
        return output;

    }

}
