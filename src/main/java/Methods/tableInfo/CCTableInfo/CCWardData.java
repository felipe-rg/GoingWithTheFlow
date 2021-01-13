package Methods.tableInfo.CCTableInfo;

import Client.*;
import Methods.dateFormat;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class CCWardData extends dateFormat {

    public ArrayList<String> getWardInfo(int wardId, Client client) throws IOException, SQLException {
        ArrayList<String> numbers = new ArrayList<String>();
        //format = FOR ALL - wardname, capacity, Male free, female free, either sex free, expected male discharge, expected female discharge,
        // IF AMC - male transfer, female transfer
        // FOR ALL - ICU, RIP

        //Get ward name
        ArrayList<String> json = client.makeGetRequest("*", "wards", "wardid="+wardId);
        ArrayList<Ward> wards = client.wardsFromJson(json);
        //add wardName
        Ward thisWard = wards.get(0);
        numbers.add(thisWard.getWardName());


        //Get patients in ward
        json = client.makeGetRequest("*", "patients", "currentwardid="+wardId);
        ArrayList<Patient> patients = client.patientsFromJson(json);

        //Get beds in ward
        json = client.makeGetRequest("*", "beds", "wardid="+wardId);
        ArrayList<Bed> beds = client.bedsFromJson(json);

        //Get all free beds
        json = client.makeGetRequest("*", "beds", "status='F'");
        ArrayList<Bed> allFreeBeds = client.bedsFromJson(json);

        //Cross ref to get free beds in ward
        ArrayList<Bed> freeBedsInWard = client.bedCrossReference(allFreeBeds, beds);

        //Do some funky maths
        float capacity = (beds.size()-freeBedsInWard.size())*100/beds.size();
        numbers.add(String.valueOf(capacity));

        //Get all male beds
        json = client.makeGetRequest("*", "beds", "forsex='Male'");
        ArrayList<Bed> allMaleBeds = client.bedsFromJson(json);

        //Cross allMale and freeInward to give male free beds in ward
        ArrayList<Bed> freeMaleBedsInWard = client.bedCrossReference(freeBedsInWard,allMaleBeds);
        numbers.add(String.valueOf(freeMaleBedsInWard.size()));

        //Get all female beds
        json = client.makeGetRequest("*", "beds", "forsex='Female'");
        ArrayList<Bed> allFemaleBeds = client.bedsFromJson(json);

        //Cross ref allFemale and Freeinward to give free female in ward
        ArrayList<Bed> freeFemaleBedsInWard = client.bedCrossReference(freeBedsInWard,allFemaleBeds);
        numbers.add(String.valueOf(freeFemaleBedsInWard.size()));

        //Get all unisex beds
        json = client.makeGetRequest("*", "beds", "forsex='Uni'");
        ArrayList<Bed> allUniBeds = client.bedsFromJson(json);

        //Cross ref allUni with free in ward to give free uni in ward
        ArrayList<Bed> freeUniBedsInWard = client.bedCrossReference(freeBedsInWard,allUniBeds);
        numbers.add(String.valueOf(freeUniBedsInWard.size()));

        //Get all discharge wards
        json = client.makeGetRequest("*", "wards", "wardtype='discharge'");
        ArrayList<Ward> dischargeWards = client.wardsFromJson(json);

        //Get all patients to be discharged
        ArrayList<Patient> allDischarges = new ArrayList<Patient>();
        for(Ward w:dischargeWards){
            json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
            allDischarges.addAll(client.patientsFromJson(json));
        }

        //Cross ref patients in ward and all discharges for discharges from ward
        ArrayList<Patient> wardDischarges = client.crossReference(allDischarges, patients);

        //Get all male patients
        json = client.makeGetRequest("*", "patients", "sex='Male'");
        ArrayList<Patient> allMalePatients = client.patientsFromJson(json);

        //Cross wardDischarges and allMale to give male discharges from ward
        ArrayList<Patient> allMaleDischarges = client.crossReference(allMalePatients, wardDischarges);
        numbers.add(String.valueOf(allMaleDischarges.size()));

        //Get all female patients
        json = client.makeGetRequest("*", "patients", "sex='Female'");
        ArrayList<Patient> allFemalePatients = client.patientsFromJson(json);

        //Cross wardDischarges and allFemale to give female discharges from ward
        ArrayList<Patient> allFemaleDischarges = client.crossReference(allFemalePatients, wardDischarges);
        numbers.add(String.valueOf(allFemaleDischarges.size()));

        //Only AMU wards have transfers
        if(thisWard.getWardType().equals("AMU")){
            ArrayList<Patient> toLong = new ArrayList<Patient>();
            ArrayList<Patient> toLongFromWard = new ArrayList<Patient>();
            //Get all long stay wards
            json = client.makeGetRequest("*", "wards", "wardtype='LS'");
            ArrayList<Ward> lsWards = client.wardsFromJson(json);

            for(Ward w:lsWards){
                //Get all patients going to long stay wards
                json = client.makeGetRequest("*", "patients", "nextdestination="+w.getWardId());
                toLong.addAll(client.patientsFromJson(json));
                toLongFromWard.addAll(client.crossReference(toLong, patients));
            }

            //Cross ref transfers and male patients to give male transfers
            ArrayList<Patient> allMaleTransfers = client.crossReference(allMalePatients, toLongFromWard);
            numbers.add(String.valueOf(allMaleTransfers.size()));

            //Cross ref transfers and female patients to give female transfers
            ArrayList<Patient> allFemaleTransfers = client.crossReference(allFemalePatients, toLongFromWard);
            numbers.add(String.valueOf(allFemaleTransfers.size()));
        }

        //Get all patients going to the ICU
        json = client.makeGetRequest("*", "patients", "nextdestination=7");
        ArrayList<Patient> allICU = client.patientsFromJson(json);

        //Cross ref to give all patients to ICu from this ward
        ArrayList<Patient> wardICU = client.crossReference(allICU, patients);
        numbers.add(String.valueOf(wardICU.size()));

        //Get all patients who have died
        json = client.makeGetRequest("*", "patients", "nextdestination=8");
        ArrayList<Patient> allRIP = client.patientsFromJson(json);

        //Cross ref to find those who have died in this ward
        ArrayList<Patient> wardRIP = client.crossReference(allRIP, patients);
        numbers.add(String.valueOf(wardRIP.size()));

        return numbers;
    }
}
