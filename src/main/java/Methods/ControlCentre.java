package Methods;
import Client.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
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

    public void refresh() throws IOException, SQLException {
        incomingNumbers();
        amcNumbers();
        longStayNumbers();
    }

    private void amcNumbers() throws IOException, SQLException {
        amcCapacityPerc = 0;
        freeBeds = 0;
        dischargePatients = 0;
        transferPatients = 0;
        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation='AMC'");//todo AMC id
        ArrayList<Patient> amcPatients = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "beds", "wardId='AMC'");
        ArrayList<Bed> amcBeds = client.bedsFromJson(json);
        amcCapacityPerc = amcPatients.size()*100/amcBeds.size();
        freeBeds = amcBeds.size()-amcPatients.size();
        json = client.makeGetRequest("id", "patients", "currentLocation='AMC'");//todo AMC id
        ArrayList<Patient> inAMC = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "nextDestination!=NULL");
        ArrayList<Patient> leavingAMC = client.patientsFromJson(json);
        //todo how are we signalling discharge?
        json = client.makeGetRequest("id", "patients", "nextDestination='Discharge'");
        ArrayList<Patient> discharges = client.patientsFromJson(json);
        for(int i=0; i<inAMC.size(); i++){
            for(int j=0; j<leavingAMC.size();i++) {
                if (inAMC.get(i).getId() == leavingAMC.get(j).getId()){
                    transferPatients = transferPatients +1;
                }
                if (inAMC.get(i).getId() == discharges.get(j).getId()){
                    dischargePatients = dischargePatients +1;
                }
            }
        }
    }

    private void longStayNumbers() throws IOException, SQLException {
        longstayCapacityPerc = 0;
        longstayFreeBeds = 0;
        int longStayCapacity = 0;
        ArrayList<String> json = client.makeGetRequest("id", "patients", "currentLocation!='AMC'");
        ArrayList<Patient> notInAMC = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "currentLocation!='AandE'");
        ArrayList<Patient> notInAandE = client.patientsFromJson(json);
        for(int i=0; i<notInAMC.size(); i++){
            for(int j=0; j<notInAandE.size();i++) {
                if (notInAandE.get(i).getId() == notInAandE.get(j).getId()){
                    longStayCapacity = longStayCapacity +1;
                }
            }
        }
        //SQLstr = "SELECT id FROM beds WHERE wardId != 'AMC';";
        json = client.makeGetRequest("id", "beds", "wardid!='AMC'");
        ArrayList<Bed> longstayBeds = client.bedsFromJson(json);
        longstayCapacityPerc = longStayCapacity*100/longstayBeds.size();
        longstayFreeBeds = longstayBeds.size() - longStayCapacity;
    }

    private void incomingNumbers() throws IOException, SQLException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;
        String SQLstr = "SELECT arrivalTime FROM patients WHERE currentLocation = 'AandE' AND acceptedByMedicine = true;";
        ArrayList<String> json = client.makeGetRequest("id,arrivalTime", "patients", "currentLocation='AandE'");
        ArrayList<Patient> inAandE = client.patientsFromJson(json);
        json = client.makeGetRequest("id", "patients", "acceptedByMedicine=true");
        ArrayList<Patient> accepted = client.patientsFromJson(json);
        ArrayList<Patient> incoming = new ArrayList<Patient>();
        for(int i=0; i<inAandE.size(); i++){
            for(int j=0; j<accepted.size();i++) {
                if (inAandE.get(i).getId() == incoming.get(j).getId()){
                    incoming.add(inAandE.get(i));
                }
            }
        }
        //FIXME time manipulation with neo
       /* Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        calendar.setTime(java.sql.Time.valueOf(LocalTime.now()));
        calendar2.setTime(java.sql.Time.valueOf(LocalTime.now()));
        calendar3.setTime(java.sql.Time.valueOf(LocalTime.now()));
        calendar2.add(Calendar.HOUR_OF_DAY, -2);
        calendar3.add(Calendar.HOUR_OF_DAY, -3);
        for(Patient p : incoming){
            if(p.getArrivalDateTime().after(calendar2.getTime())){
                greenPatients = greenPatients +1;
            }
            else if(p.arrivalTime.after(calendar3.getTime())){
                orangePatients = orangePatients +1;
            }
            else {
                redPatients = redPatients +1;
            }
        }*/
    }

    public ArrayList<Patient> seeIncomingList() throws IOException, SQLException {
        //String SQLstr = "SELECT id, sex, initialDiagnosis, acceptedByMedicine, arrivalTime FROM patients WHERE currentLocation = 'AandE';";
        ArrayList<String> json = client.makeGetRequest("*", "patients", "currentLocation='AandE'");
        return client.patientsFromJson(json);
    }

    @Override
    public ArrayList<Patient> getWardInfo(int wardId) throws IOException, SQLException {
        //String SQLstr = "SELECT * FROM patients WHERE wardId = '"+wardId+"';";
        ArrayList<String> json = client.makeGetRequest("*", "patients", "wardid="+wardId);
        return client.patientsFromJson(json);
    }

    @Override
    public ArrayList<Patient> getPatientInfo(int wardId) {
        return null;
    }
}
