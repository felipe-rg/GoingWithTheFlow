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
        //FIXME wardId in int or string?
        //String SQLstr = "SELECT id FROM patients WHERE currentLocation = 'AMC';";
        ArrayList<Patient> amcPatients = client.makeGetRequest("id", "patients", "currentLocation='AMC'");
        //SQLstr = "SELECT id FROM beds WHERE wardId = 'AMC';";
        ArrayList<Patient> amcBeds = client.makeGetRequest("id", "beds", "wardId='AMC'");
        amcCapacityPerc = amcPatients.size()*100/amcBeds.size();
        freeBeds = amcBeds.size()-amcPatients.size();
        //SQLstr = "SELECT id FROM patients WHERE currentLocation = 'AMC' AND nextDestination != NULL;";
        ArrayList<Patient> inAMC = client.makeGetRequest("id", "patients", "currentLocation='AMC'");
        ArrayList<Patient> leavingAMC = client.makeGetRequest("id", "patients", "nextDestination!=NULL");
        //todo how are we signalling discharge?
        //SQLstr = "SELECT id FROM patients WHERE currentLocation = 'AMC' AND nextDestination = 'Discharge';";
        ArrayList<Patient> discharges = client.makeGetRequest("id", "patients", "nextDestination='Discharge'");
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
        //String SQLstr = "SELECT id FROM patients WHERE currentLocation != 'AMC' AND currentLocation != 'AandE';";
        ArrayList<Patient> notInAMC = client.makeGetRequest("id", "patients", "currentLocation!='AMC'");
        ArrayList<Patient> notInAandE = client.makeGetRequest("id", "patients", "currentLocation!='AandE'");
        for(int i=0; i<notInAMC.size(); i++){
            for(int j=0; j<notInAandE.size();i++) {
                if (notInAandE.get(i).getId() == notInAandE.get(j).getId()){
                    longStayCapacity = longStayCapacity +1;
                }
            }
        }
        //SQLstr = "SELECT id FROM beds WHERE wardId != 'AMC';";
        ArrayList<Patient> longstayBeds = client.makeGetRequest("id", "beds", "wardid!='AMC'");
        longstayCapacityPerc = longStayCapacity*100/longstayBeds.size();
        longstayFreeBeds = longstayBeds.size() - longStayCapacity;
    }

    private void incomingNumbers() throws IOException, SQLException {
        greenPatients = 0;
        redPatients = 0;
        orangePatients = 0;
        String SQLstr = "SELECT arrivalTime FROM patients WHERE currentLocation = 'AandE' AND acceptedByMedicine = true;";
        ArrayList<Patient> inAandE = client.makeGetRequest("id,arrivalTime", "patients", "currentLocation='AandE'");
        ArrayList<Patient> accepted= client.makeGetRequest("id", "patients", "acceptedByMedicine=true");
        ArrayList<Patient> incoming = new ArrayList<Patient>();
        for(int i=0; i<inAandE.size(); i++){
            for(int j=0; j<accepted.size();i++) {
                if (inAandE.get(i).getId() == incoming.get(j).getId()){
                    incoming.add(inAandE.get(i));
                }
            }
        }
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
        return client.makeGetRequest("id,sex,initialDiagnosis,acceptedByMedicine,arrivalTime", "patients", "currentLocation='AandE'");
    }

    @Override
    public ArrayList<Patient> getWardInfo(int wardId) throws IOException, SQLException {
        //String SQLstr = "SELECT * FROM patients WHERE wardId = '"+wardId+"';";
        return client.makeGetRequest("*", "patients", "wardid="+wardId);
    }

    @Override
    public ArrayList<Patient> getPatientInfo(int wardId) {
        return null;
    }
}
