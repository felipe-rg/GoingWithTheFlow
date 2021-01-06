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

}
