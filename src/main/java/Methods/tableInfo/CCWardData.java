package Methods.tableInfo;

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
}
