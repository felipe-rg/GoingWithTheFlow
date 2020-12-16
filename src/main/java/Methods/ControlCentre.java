package Methods;
import Client.Client;

import java.io.IOException;
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


    public ControlCentre() throws IOException {
        client = new Client();
        refresh();
    }

    public void refresh() throws IOException {
        incomingNumbers();
        amcNumbers();
        longStayNumbers();
    }

    private void amcNumbers() throws IOException {
        amcCapacityPerc = 0;
        freeBeds = 0;
        dischargePatients = 0;
        //FIXME wardId in int or string?
        String SQLstr = "SELECT id FROM patients WHERE currentLocation = 'AMC';";
        ArrayList<Patient> amcPatients = client.makeGetRequest(SQLstr);
        SQLstr = "SELECT id FROM beds WHERE wardId = 'AMC';";
        ArrayList<Patient> amcBeds = client.makeGetRequest(SQLstr);
        amcCapacityPerc = amcPatients.size()*100/amcBeds.size();
        freeBeds = amcBeds.size()-amcPatients.size();
        SQLstr = "SELECT id FROM patients WHERE currentLocation = 'AMC' AND nextDestination != NULL;";
        ArrayList<Patient> transfers = client.makeGetRequest(SQLstr);
        transferPatients = transfers.size();
        //todo how are we signalling discharge?
        SQLstr = "SELECT id FROM patients WHERE currentLocation = 'AMC' AND nextDestination = 'Discharge';";
        ArrayList<Patient> discharges = client.makeGetRequest(SQLstr);
        dischargePatients = discharges.size();
    }

    private void longStayNumbers() throws IOException {
        longstayCapacityPerc = 0;
        longstayFreeBeds = 0;
        String SQLstr = "SELECT id FROM patients WHERE currentLocation != 'AMC' AND currentLocation != 'AandE';";
        ArrayList<Patient> longstayPatients = client.makeGetRequest(SQLstr);
        SQLstr = "SELECT id FROM beds WHERE wardId != 'AMC';";
        ArrayList<Patient> longstayBeds = client.makeGetRequest(SQLstr);
        longstayCapacityPerc = longstayPatients.size()*100/longstayBeds.size();
        longstayFreeBeds = longstayBeds.size() - longstayPatients.size();
    }

    private void incomingNumbers() throws IOException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;
        String SQLstr = "SELECT arrivalTime FROM patients WHERE currentLocation = 'AandE' AND acceptedByMedicine = true;";
        ArrayList<Patient> incoming = client.makeGetRequest(SQLstr);
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        Calendar calendar3 = Calendar.getInstance();
        calendar.setTime(java.sql.Time.valueOf(LocalTime.now()));
        calendar2.setTime(java.sql.Time.valueOf(LocalTime.now()));
        calendar3.setTime(java.sql.Time.valueOf(LocalTime.now()));
        calendar2.add(Calendar.HOUR_OF_DAY, -2);
        calendar3.add(Calendar.HOUR_OF_DAY, -3);
        for(Patient p : incoming){
            if(p.arrivalTime.after(calendar2.getTime())){
                greenPatients = greenPatients +1;
            }
            else if(p.arrivalTime.after(calendar3.getTime())){
                orangePatients = orangePatients +1;
            }
            else {
                redPatients = redPatients +1;
            }
        }
    }

    public ArrayList<Patient> seeIncomingList() throws IOException {
        String SQLstr = "SELECT id, sex, initialDiagnosis, acceptedByMedicine, arrivalTime FROM patients WHERE currentLocation = 'AandE';";
        return client.makeGetRequest(SQLstr);
    }

    @Override
    public ArrayList<Patient> getWardInfo(String wardId) throws IOException {
        String SQLstr = "SELECT * FROM patients WHERE wardId = '"+wardId+"';";
        return client.makeGetRequest(SQLstr);
    }

    @Override
    public ArrayList<Patient> getPatientInfo(String wardId) {
        return null;
    }
}
