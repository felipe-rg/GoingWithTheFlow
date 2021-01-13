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

/*  Methods used to retrieve information to display on the control centre
    Used with the tables labelled Info and the CCWardInfo class
    Tested through UI test comparing to database
    More robust tests needed - Separate database needed with specific AMU and LS wards else they will interfere with app*/

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

        //Loop amu wards
        for(Ward w:amuWards){
            int wardid = w.getWardId();

            json = client.makeGetRequest("*", "patients", "currentwardid="+wardid); //Get patients in amu wards
            if(json.size()!=0) {
                amcPatients.addAll(client.patientsFromJson(json)); //Add patients to amcPatients
            }
            json = client.makeGetRequest("*", "beds", "wardid="+wardid); //Get beds in amu wards
            if(json.size()!=0) {
                amcBeds.addAll(client.bedsFromJson(json)); //Add beds to amcBeds
            }
        }

        ArrayList<Bed> allFreeBeds = new ArrayList<Bed>();
        json = client.makeGetRequest("*", "beds", "status='F'"); //Find all free beds
        if(json.size()!=0) {
            allFreeBeds = client.bedsFromJson(json); //Put free beds in array
        }
        ArrayList<Bed> amcFreeBeds = client.bedCrossReference(allFreeBeds, amcBeds); //Find free beds in AMU

        freeBeds = amcFreeBeds.size(); //Add number to those displayed

        amcCapacityPerc = (amcBeds.size()-freeBeds)*100/amcBeds.size(); //Find percentage of not-free beds

        json = client.makeGetRequest("*", "wards", "wardtype='discharge'"); //Get all discharge wards
        ArrayList<Ward> dischargeWards = client.wardsFromJson(json); //All discharge areas

        ArrayList<Patient> discharges = new ArrayList<Patient>();
        for(Ward w:dischargeWards) { //Loop discharge wards
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId()); //Find patients goinf to discharge wards
            if(json.size()!=0) {
                discharges = client.patientsFromJson(json); //Add to discharge array
            }
        }

        ArrayList<Patient> amcDischarges = client.crossReference(amcPatients, discharges); //`Find patients discharging from AMU
        dischargePatients = amcDischarges.size(); //Count discharges and display

        json = client.makeGetRequest("*", "wards", "wardtype='LS'"); //Find all long stay wards
        ArrayList<Ward> lsWards = client.wardsFromJson(json); //All long stay wards

        ArrayList<Patient> toLong= new ArrayList<Patient>();
        for(Ward w:lsWards) { //Loop long stay wards
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId()); //Find patients going to long stay
            if(json.size()!=0) {
                toLong.addAll(client.patientsFromJson(json)); //add to array
            }
        }

        transferPatients = client.crossReference(toLong, amcPatients).size(); //Add transfers from AMU
    }

    //Updates numbers in longstay section
    private void longStayNumbers() throws IOException, SQLException {
        longstayCapacityPerc = 0;
        longstayFreeBeds = 0;

        ArrayList<Bed> longBed = new ArrayList<Bed>();

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='LS'");//Find all long stay wards
        ArrayList<Ward> lsWards = client.wardsFromJson(json); //All long stay wards

        for(Ward w:lsWards) { //Loop long stay wards
            json = client.makeGetRequest("*", "beds", "wardid="+w.getWardId()); //Find beds in long stay
            longBed.addAll(client.bedsFromJson(json));
        }

        json = client.makeGetRequest("*", "beds", "status='F'"); //Find all free beds
        ArrayList<Bed> allFreeBeds = client.bedsFromJson(json); //All free beds

        ArrayList<Bed> longFreeBeds = client.bedCrossReference(allFreeBeds, longBed); //Find free beds in long stay wards

        longstayFreeBeds = longFreeBeds.size(); //Free beds
        longstayCapacityPerc = (longBed.size()-longstayFreeBeds)*100/longBed.size(); //Percentage capacity
    }

    //Updates numbers in incoming section
    private void incomingNumbers() throws IOException, SQLException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;
        ArrayList<Patient> toAMU = new ArrayList<Patient>();

        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'"); //Finf all amu wards
        ArrayList<Ward> amuWards = client.wardsFromJson(json); //All amc wards

        for(Ward w:amuWards){ //Loop amu wards
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId()); //Find patients going to amu
            toAMU.addAll(client.patientsFromJson(json)); //add to array
        }

        LocalDateTime now = LocalDateTime.now(); //Get time now
        LocalDateTime red = now.minusHours(3); //Get time in 3 hours
        LocalDateTime orange = now.minusHours(2); //Get time in 2 hours

        for(Patient p : toAMU){ //Loop patients incoming
            LocalDateTime patientArrival = p.getArrivalDateTime(); //Get patients arrival time
            if(patientArrival.isAfter(orange)){ //If patient has been waiting less than 2 hours
                greenPatients = greenPatients +1;  //Patient is green
            }
            else if(patientArrival.isAfter(red)){//If patient has been waiting more than 2 hours but less than 3 hours
                orangePatients = orangePatients +1; //patient is orange
            }
            else {
                redPatients = redPatients +1; //If they have been waiting for more than 3 hours they are red
            }
        }
    }

    public ArrayList<Ward> findAMUWards(){
        try {
            ArrayList<String> json = client.makeGetRequest("*", "wards", "wardtype='AMU'"); //find amu wards
            if(json.size()!=0){
                return client.wardsFromJson(json); //return array
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
