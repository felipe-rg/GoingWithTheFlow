package Methods;
import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
        //refresh();
        redPatients = 3;
        orangePatients = 4;
        greenPatients = 2;
        amcCapacityPerc = 80;
        freeBeds = 2;
        transferPatients = 9;
        dischargePatients = 2;
        longstayCapacityPerc = 90;
        longstayFreeBeds = 10;
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

        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation=2"); //amc id is 2
        ArrayList<Patient> amcPatients = client.patientsFromJson(json); //All amc patients

        json = client.makeGetRequest("id", "beds", "wardId=2");
        ArrayList<Bed> amcBeds = client.bedsFromJson(json); //All amc beds

        amcCapacityPerc = amcPatients.size()*100/amcBeds.size();
        freeBeds = amcBeds.size()-amcPatients.size();
        //todo male and female free beds?
        //beds with sideroom?

        //todo nextdestination!=NULL?
        json = client.makeGetRequest("id", "patients", "transferrequeststatus='C'");
        ArrayList<Patient> leavingAMC = client.patientsFromJson(json);

        //todo how are we signalling discharge?
        json = client.makeGetRequest("id", "patients", "ttasignedoff=TRUE");
        ArrayList<Patient> discharges = client.patientsFromJson(json);

        for(int i=0; i<amcPatients.size(); i++){
            for(int j=0; j<leavingAMC.size();i++) {
                if (amcPatients.get(i).getId() == leavingAMC.get(j).getId()){
                    transferPatients = transferPatients +1;
                }
                if (amcPatients.get(i).getId() == discharges.get(j).getId()){
                    dischargePatients = dischargePatients +1;
                }
            }
        }
    }

    //Updates numbers in longstay section
    private void longStayNumbers() throws IOException, SQLException {
        longstayCapacityPerc = 0;
        longstayFreeBeds = 0;
        int longStayCapacity = 0;

        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation!=2");
        ArrayList<Patient> notInAMC = client.patientsFromJson(json);

        json = client.makeGetRequest("id", "patients", "currentLocation!=1");
        ArrayList<Patient> notInAandE = client.patientsFromJson(json);

        for(int i=0; i<notInAMC.size(); i++){
            for(int j=0; j<notInAandE.size();i++) {
                if (notInAandE.get(i).getId() == notInAandE.get(j).getId()){
                    longStayCapacity = longStayCapacity +1;
                }
            }
        }

        json = client.makeGetRequest("id", "beds", "wardid!=2");
        ArrayList<Bed> longstayBeds = client.bedsFromJson(json);

        longstayCapacityPerc = longStayCapacity*100/longstayBeds.size();
        //todo male and female free beds?
        //beds with sideroom?
        longstayFreeBeds = longstayBeds.size() - longStayCapacity;
    }

    //Updates numbers in incoming section
    private void incomingNumbers() throws IOException, SQLException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;

        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentlocation=1");
        ArrayList<Patient> inAandE = client.patientsFromJson(json);

        //todo do we need them to be accepted?
        json = client.makeGetRequest("*", "patients", "acceptedbymedicine=true");
        ArrayList<Patient> accepted = client.patientsFromJson(json);

        ArrayList<Patient> incoming = client.crossReference(inAandE, accepted);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime red = now.minusHours(3);
        LocalDateTime orange = now.minusHours(2);

        for(Patient p : incoming){
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

    //Returns list of people coming to AMC
    //FIXME same as getIncomingList in generalWard?
    public ArrayList<Patient> seeIncomingList() throws IOException, SQLException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentLocation=1");
        return client.patientsFromJson(json);
    }

    //Returns list of people being discharged from AMC
    //FIXME same as getDischargeList in generalWard?
    public ArrayList<Patient> seeDischargeList() throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentLocation=2");
        ArrayList<Patient> inAMC = client.patientsFromJson(json);
        //Todo - signalling discharge?
        json = client.makeGetRequest("*", "patients", "ttasignedoff=TRUE");
        ArrayList<Patient> discharge= client.patientsFromJson(json);
        return client.crossReference(inAMC, discharge);
    }

    //Returns list of patients set to be transferred from AMC
    public ArrayList<Patient> seeTransferList() throws IOException {
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentLocation=2");
        ArrayList<Patient> inAMC = client.patientsFromJson(json);

        json = client.makeGetRequest("*", "patients", "transferrequeststatus='C'");
        ArrayList<Patient> transfer= client.patientsFromJson(json);

        return client.crossReference(inAMC, transfer);
    }

    //Returns list of strings with info from getWardInfo
    //Used in table for wards
    //todo must be a better way of doing this
    public ArrayList<ArrayList<String>> getAllWardInfo() throws IOException, SQLException {
        ArrayList<ArrayList<String>> output = new ArrayList<ArrayList<String>>();

        ArrayList<String> json = client.makeGetRequest("wards", "information_schema.tables", "table_schema='public'");
        ArrayList<Ward> wards = client.wardsFromJson(json);

        for(Ward w:wards){
            output.add(getWardInfo(w.getId()));
        }

        return output;
    }


    //Returns list of strings with format described below
    //Used in method above
    @Override
    public ArrayList<String> getWardInfo(int wardId) throws IOException, SQLException {
        ArrayList<String> numbers = new ArrayList<String>();
        //format = wardname, capacity, Male free, female free, either sex free, expected male discharge, expected female discharge

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+wardId);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        //add wardName
        numbers.add(wards.get(0).getWardName());

        json = client.makeGetRequest("*", "patients", "wardid="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);
        json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> beds = client.bedsFromJson(json);

        ArrayList<Bed> fullBeds = new ArrayList<Bed>();
        ArrayList<Bed> emptyBeds = new ArrayList<Bed>();
        ArrayList<Bed> emptyMaleBeds = new ArrayList<Bed>();
        ArrayList<Bed> emptyFemaleBeds = new ArrayList<Bed>();
        for(Bed b:beds){
            if(b.getStatus()=="F"){
                emptyBeds.add(b);
                if(b.getForSex()=="M"){
                    emptyMaleBeds.add(b);
                }
                else {
                    emptyFemaleBeds.add(b);
                }
            }
            else {
                fullBeds.add(b);
            }
        }

        //add percentage capacity
        numbers.add((patients.size()*100)/beds.size()+"%");
        //add male free
        numbers.add(String.valueOf(emptyMaleBeds.size()));
        //add female free
        numbers.add(String.valueOf(emptyFemaleBeds.size()));
        //add total free
        numbers.add(String.valueOf(emptyBeds.size()));

        //Todo expected discharges signalled how?
        return numbers;
    }

    //todo why do we need this?
    @Override
    public ArrayList<Patient> getPatientInfo(int wardId) {
        return null;
    }
}
