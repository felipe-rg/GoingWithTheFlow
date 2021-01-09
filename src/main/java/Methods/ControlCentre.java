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
import java.util.logging.Logger;


public class ControlCentre {
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

    private static final Logger log= Logger.getLogger(ControlCentre.class.getName());

    public ControlCentre() throws IOException, SQLException {
        client = new Client();
        log.info("Client for Control Unit created");
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
    public Client getClient(){return client;}

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

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
        ArrayList<Ward> amuWards = client.wardsFromJson(json); //All amc wards

        ArrayList<Patient> amcPatients = new ArrayList<Patient>(); //All amc patients
        ArrayList<Bed> amcBeds = new ArrayList<Bed>();
        for(Ward w:amuWards){
            int wardid = w.getWardId();

            json = client.makeGetRequest("*", "patients", "currentwardid="+wardid);
            if(json.size()!=0) {
                amcPatients.addAll(client.patientsFromJson(json));
            }
            json = client.makeGetRequest("*", "beds", "wardid="+wardid);
            if(json.size()!=0) {
                amcBeds.addAll(client.bedsFromJson(json));
            }
        }

        amcCapacityPerc = amcPatients.size()*100/amcBeds.size();

        json = client.makeGetRequest("*", "beds", "status='F'");

        ArrayList<Bed> allFreeBeds = new ArrayList<Bed>();
        if(json.size()!=0) {
            allFreeBeds = client.bedsFromJson(json); //All free beds
        }
        ArrayList<Bed> amcFreeBeds = client.bedCrossReference(allFreeBeds, amcBeds);

        freeBeds = amcFreeBeds.size();

        json = client.makeGetRequest("*", "wards", "wardtype='discharge'");
        ArrayList<Ward> dischargeWards = client.wardsFromJson(json); //All discharge areas

        ArrayList<Patient> discharges = new ArrayList<Patient>();
        for(Ward w:dischargeWards) {
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
            if(json.size()!=0) {
                discharges = client.patientsFromJson(json); //All amc patients
            }
        }

        ArrayList<Patient> amcDischarges = client.crossReference(amcPatients, discharges);
        dischargePatients = amcDischarges.size();

        json = client.makeGetRequest("*", "wards", "wardtype='LS'");
        ArrayList<Ward> lsWards = client.wardsFromJson(json); //All long stay wards

        ArrayList<Patient> toLong= new ArrayList<Patient>();
        for(Ward w:lsWards) {
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
            if(json.size()!=0) {
                toLong.addAll(client.patientsFromJson(json));
            }
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

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='LS'");
        ArrayList<Ward> lsWards = client.wardsFromJson(json); //All long stay wards

        for(Ward w:lsWards) {
            json = client.makeGetRequest("*", "patients", "currentwardid="+w.getWardId());
            inLong.addAll(client.patientsFromJson(json));
            json = client.makeGetRequest("*", "beds", "wardid="+w.getWardId());
            longBed.addAll(client.bedsFromJson(json));
        }
        longStayCapacity = inLong.size();

        json = client.makeGetRequest("*", "beds", "status='F'");
        ArrayList<Bed> allFreeBeds = client.bedsFromJson(json); //All free beds

        ArrayList<Bed> longFreeBeds = client.bedCrossReference(allFreeBeds, longBed);

        longstayFreeBeds = longFreeBeds.size();
        longstayCapacityPerc = longStayCapacity*100/longBed.size();
    }

    //Updates numbers in incoming section
    private void incomingNumbers() throws IOException, SQLException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;
        ArrayList<Patient> toAMU = new ArrayList<Patient>();

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
        ArrayList<Ward> amuWards = client.wardsFromJson(json); //All amc wards

        for(Ward w:amuWards){
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
            toAMU.addAll(client.patientsFromJson(json));
        }


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime red = now.minusHours(3);
        LocalDateTime orange = now.minusHours(2);

        for(Patient p : toAMU){
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

    public ArrayList<Ward> findAMUWards(){
        try {
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'");
            if(json.size()!=0){
                return client.wardsFromJson(json);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
