package Methods;
import Client.*;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Calendar;


public class ControlCentre implements statusable{
    Client client;
    private int redPatients;
    private int orangePatients;
    private int greenPatients;
    private float amcCapacityPerc;
    private int freeBeds;
    private int transferPatients;
    private int dischargePatients;
    private float longstayCapacityPerc;
    private int longstayFreeBeds;


    public ControlCentre() throws IOException, SQLException {
        client = new Client();
        refresh();
    }

    public int getRedPatients(){return redPatients;}
    public int getOrangePatients(){return orangePatients;}
    public int getGreenPatients(){return greenPatients;}
    public float getAmcCapacityPerc(){return amcCapacityPerc;}
    public int getTransferPatients(){return transferPatients;}
    public int getDischargePatients(){return dischargePatients;}
    public int getFreeBeds(){return freeBeds;}
    public int getLongstayFreeBeds(){return longstayFreeBeds;}
    public float getLongstayCapacityPerc(){return longstayCapacityPerc;}

    public void refresh() throws IOException, SQLException {
        incomingNumbers();
        amcNumbers();
        longStayNumbers();
    }

    //Updates numbers in amc section
    private void amcNumbers() throws IOException, SQLException {
        amcCapacityPerc = 0;
        freeBeds = 0;
        dischargePatients = 0;
        transferPatients = 0;

        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid=2"); //amc id is 2
        ArrayList<Patient> amcPatients = client.patientsFromJson(json); //All amc patients

        json = client.makeGetRequest("*", "beds", "wardid=2");
        ArrayList<Bed> amcBeds = client.bedsFromJson(json); //All amc beds

        amcCapacityPerc = amcPatients.size()*100/amcBeds.size();

        json = client.makeGetRequest("*", "beds", "status='F'");
        ArrayList<Bed> allFreeBeds = client.bedsFromJson(json); //All amc beds

        ArrayList<Bed> amcFreeBeds = client.bedCrossReference(allFreeBeds, amcBeds);

        freeBeds = amcFreeBeds.size();

        json = client.makeGetRequest("*", "patients", "nextdestination=6");
        ArrayList<Patient> discharges = client.patientsFromJson(json); //All amc patients

        ArrayList<Patient> amcDischarges = client.crossReference(amcPatients, discharges);
        dischargePatients = amcDischarges.size();

        ArrayList<Patient> toLong = new ArrayList<Patient>();
        for(int i=3; i<6; i++){
            json = client.makeGetRequest("*", "patients", "nextdestination="+i);
            toLong.addAll(client.patientsFromJson(json));
        }
        transferPatients = toLong.size();
    }

    //Updates numbers in longstay section
    private void longStayNumbers() throws IOException, SQLException {
        longstayCapacityPerc = 0;
        longstayFreeBeds = 0;
        int longStayCapacity = 0;

        ArrayList<Patient> inLong = new ArrayList<Patient>();
        ArrayList<Bed> longBed = new ArrayList<Bed>();
        for(int i=3; i<6; i++){
            ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid="+i);
            inLong.addAll(client.patientsFromJson(json));
            json = client.makeGetRequest("*", "beds", "wardid="+i);
            longBed.addAll(client.bedsFromJson(json));
        }
        longStayCapacity = inLong.size();
        ArrayList<String> json = client.makeGetRequest("*", "beds", "status='F'");
        ArrayList<Bed> allFreeBeds = client.bedsFromJson(json); //All amc beds

        ArrayList<Bed> longFreeBeds = client.bedCrossReference(allFreeBeds, longBed);

        longstayFreeBeds = longFreeBeds.size();
        longstayCapacityPerc = longStayCapacity*100/longBed.size();
    }

    //Updates numbers in incoming section
    private void incomingNumbers() throws IOException, SQLException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;

        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentwardid=1");
        ArrayList<Patient> inAandE = client.patientsFromJson(json);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime red = now.minusHours(3);
        LocalDateTime orange = now.minusHours(2);

        for(Patient p : inAandE){
            LocalDateTime patientArrival = p.getArrivalDateTime();
            if(patientArrival.isAfter(orange)){
                greenPatients = greenPatients +1;
            }
            else if(patientArrival.isAfter(red)){
                orangePatients = orangePatients +1;
            }
            else {
                redPatients = redPatients +1;
            }
        }
    }


    //Returns list of strings with format described below
    //Used in method above
    @Override
    public ArrayList<String> getWardInfo(int wardId) throws IOException, SQLException {
        ArrayList<String> numbers = new ArrayList<String>();
        //format = wardname, capacity, Male free, female free, either sex free, expected male discharge, expected female discharge, ICU, RIP

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+wardId);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        //add wardName
        numbers.add(wards.get(0).getWardName());

        json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);

        json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> beds = client.bedsFromJson(json);

        float capacity = patients.size()*100/beds.size();
        numbers.add(String.valueOf(capacity));

        json = client.makeGetRequest("*", "beds", "status='F'");
        ArrayList<Bed> allFreeBeds = client.bedsFromJson(json);

        ArrayList<Bed> freeBedsInWard = client.bedCrossReference(allFreeBeds, beds);

        json = client.makeGetRequest("*", "beds", "forsex='Male'");
        ArrayList<Bed> allMaleBeds = client.bedsFromJson(json);

        ArrayList<Bed> freeMaleBedsInWard = client.bedCrossReference(allFreeBeds,allMaleBeds);
        numbers.add(String.valueOf(freeMaleBedsInWard.size()));

        json = client.makeGetRequest("*", "beds", "forsex='Female'");
        ArrayList<Bed> allFemaleBeds = client.bedsFromJson(json);

        ArrayList<Bed> freeFemaleBedsInWard = client.bedCrossReference(allFreeBeds,allFemaleBeds);
        numbers.add(String.valueOf(freeFemaleBedsInWard.size()));

        json = client.makeGetRequest("*", "beds", "forsex='Uni'");
        ArrayList<Bed> allUniBeds = client.bedsFromJson(json);

        ArrayList<Bed> freeUniBedsInWard = client.bedCrossReference(allFreeBeds,allUniBeds);
        numbers.add(String.valueOf(freeUniBedsInWard.size()));

        json = client.makeGetRequest("*", "patients", "nextdestination=6");
        ArrayList<Patient> allDischarges = client.patientsFromJson(json);

        ArrayList<Patient> wardDischarges = client.crossReference(allDischarges, patients);

        json = client.makeGetRequest("*", "patients", "sex='Male'");
        ArrayList<Patient> allMalePatients = client.patientsFromJson(json);

        ArrayList<Patient> allMaleDischarges = client.crossReference(allMalePatients, wardDischarges);
        numbers.add(String.valueOf(allMaleDischarges.size()));

        json = client.makeGetRequest("*", "patients", "sex='Female'");
        ArrayList<Patient> allFemalePatients = client.patientsFromJson(json);

        ArrayList<Patient> allFemaleDischarges = client.crossReference(allFemalePatients, wardDischarges);
        numbers.add(String.valueOf(allFemaleDischarges.size()));

        if(wardId == 2){
            ArrayList<Patient> toLong = new ArrayList<Patient>();
            for(int i=3; i<6; i++){
                json = client.makeGetRequest("*", "patients", "nextdestination="+i);
                toLong.addAll(client.patientsFromJson(json));
            }
            ArrayList<Patient> allMaleTransfers = client.crossReference(allMalePatients, toLong);
            numbers.add(String.valueOf(allMaleTransfers.size()));

            ArrayList<Patient> allFemaleTransfers = client.crossReference(allFemalePatients, toLong);
            numbers.add(String.valueOf(allFemaleTransfers.size()));
        }

        json = client.makeGetRequest("*", "patients", "nextdestination=7");
        ArrayList<Patient> allICU = client.patientsFromJson(json);

        ArrayList<Patient> wardICU = client.crossReference(allICU, patients);
        numbers.add(String.valueOf(wardICU.size()));

        json = client.makeGetRequest("*", "patients", "deceased=true");
        ArrayList<Patient> allRIP = client.patientsFromJson(json);

        ArrayList<Patient> wardRIP = client.crossReference(allICU, patients);
        numbers.add(String.valueOf(wardRIP.size()));

        return numbers;
    }



    //todo why do we need this?
    @Override
    public ArrayList<Patient> getPatientInfo(int wardId) {
        return null;
    }

    //Returns all patients who are being discharged
    public ArrayList<Patient> getDischargeList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination=6");
        return client.patientsFromJson(json);
    }

    //Patients being transferred from amc
    public ArrayList<Patient> getTransferList() throws IOException, SQLException {
        ArrayList<Patient> output = new ArrayList<Patient>();
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination=3");
        ArrayList<Patient> intoThree = client.patientsFromJson(json);
        output.addAll(intoThree);
        json = client.makeGetRequest("*", "patients", "nextdestination=4");
        ArrayList<Patient> intoFour = client.patientsFromJson(json);
        output.addAll(intoFour);
        json = client.makeGetRequest("*", "patients", "nextdestination=5");
        ArrayList<Patient> intoFive = client.patientsFromJson(json);
        output.addAll(intoFive);
        return output;
    }

    //stats needed for amclist
    public ArrayList<String> getAMCList() throws IOException, SQLException {
        return getWardInfo(2);
    }

    //stats needed for longstay
    public ArrayList<ArrayList<String>> getLongStayList() throws IOException, SQLException {
        ArrayList<ArrayList<String>> output = new ArrayList<>();
        for(int i=3; i<6; i++){
            ArrayList<String> wardInfo = getWardInfo(i);
            output.add(wardInfo);
        }
        return output;
    }

    //Returns patients coming from A&E
    public ArrayList<Patient> getIncomingList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "nextdestination=2");
        return client.patientsFromJson(json);
    }



    //Returns an object to be used in the discharge table of the Control Unit
    public Object[][] getDisData() throws IOException, SQLException {
        ArrayList<Patient> patients = getDischargeList();
        Object[][] data = new Object[patients.size()][8];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getCurrentBedId();
            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getInitialDiagnosis();
            data[i][5] = p.getTtaSignedOff();
            data[i][6] = p.getSuitableForDischargeLounge();
            data[i][7] = dateFormatter(p.getEstimatedTimeOfNext());
        }
        return data;
    }

    //Returns an object to be used in the Transfer table of the Control Unit
    public Object[][] getTransData() throws IOException, SQLException {
        ArrayList<Patient> patients = getTransferList();
        Object[][] data = new Object[patients.size()][8];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getCurrentBedId();
            data[i][2] = p.getPatientId();
            data[i][3] = p.getSex();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = p.getInitialDiagnosis();
            data[i][6] = p.getNextDestination();
            data[i][7] = dateFormatter(p.getEstimatedTimeOfNext());
        }
        return data;
    }

    //Returns an object to be used in the Incoming table of the Control Unit
    public Object[][] getIncomingData() throws IOException, SQLException {
        ArrayList<Patient> patients = getIncomingList();
        Object[][] data = new Object[patients.size()][7];
        for(int i=0; i<patients.size(); i++) {
            Patient p = patients.get(i);
            data[i][0] = p.getId();
            data[i][1] = p.getPatientId();
            data[i][2] = p.getSex();
            data[i][3] = p.getInitialDiagnosis();
            data[i][4] = p.getNeedsSideRoom();
            data[i][5] = p.getArrivalDateTime();
            data[i][6] = p.getAcceptedByMedicine();
        }
        return data;
    }


    public Object[][] getLongStayData() throws IOException, SQLException {
        ArrayList<ArrayList<String>> longStayList = getLongStayList();
        Object[][] data= new Object[longStayList.size()][1];
        for(int i=0; i<longStayList.size(); i++){
            data[0][i] = longStayList.get(i);
        }
        return data;


    }


    public Object[][] getAMCData() throws IOException, SQLException {
        ArrayList<String> amcList = getAMCList();
        Object[][] data= new Object[1][amcList.size()];
        for(int i=0; i<amcList.size(); i++){
            data[0][i] = amcList.get(i);
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
}
