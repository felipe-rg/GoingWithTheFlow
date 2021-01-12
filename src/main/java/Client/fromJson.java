package Client;

import com.google.gson.Gson;

import java.util.ArrayList;
/*This class features methods for decoding an array of JSON strings into an array of the respective objects*/
public class fromJson {

    public ArrayList<Patient> patientsFromJson(ArrayList<String> json){
        Gson gson = new Gson();
        ArrayList<Patient> patients = new ArrayList<Patient>();
        for(String s:json) {
            Patient p = gson.fromJson(s, Patient.class);
            patients.add(p);
        }
        return patients;
    }

    public ArrayList<Bed> bedsFromJson(ArrayList<String> json){
        Gson gson = new Gson();
        ArrayList<Bed> beds = new ArrayList<Bed>();
        for(String s:json) {
            Bed b = gson.fromJson(s, Bed.class);
            beds.add(b);
        }
        return beds;
    }

    public ArrayList<Ward> wardsFromJson(ArrayList<String> json){
        Gson gson = new Gson();
        ArrayList<Ward> wards = new ArrayList<Ward>();
        for(String s:json) {
            Ward w = gson.fromJson(s, Ward.class);
            wards.add(w);
        }
        return wards;
    }

    public ArrayList<Patient> crossReference(ArrayList<Patient> patients, ArrayList<Patient> pat){
        ArrayList<Patient> output = new ArrayList<Patient>();
        for(Patient p:patients){
            for(Patient pt:pat){
                if(p.getId()==pt.getId()){
                    output.add(p);
                }
            }
        }
        return output;
    }

    public ArrayList<Bed> bedCrossReference(ArrayList<Bed> beds, ArrayList<Bed> bed){
        ArrayList<Bed> output = new ArrayList<Bed>();
        for(Bed p:beds){
            for(Bed pt:bed){
                if(p.getBedId()==pt.getBedId()){
                    output.add(p);
                }
            }
        }
        return output;
    }
}
