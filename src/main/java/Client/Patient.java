package Client;

import java.io.Serializable;
import java.sql.Time;

public class Patient implements Serializable {

    private int id;
    private String nameInitials;
    private String currentLocation;
    private String sex;
    private Time arrivalTime;
    private String initialDiagnosis;
    private boolean needsSideRoom;
    private boolean acceptedByMedicine;
    private String nextDestination;
    private Time estimatedTimeOfNext;
    private boolean ttaSignedOff;
    private boolean suitableForDischargeLounge;
    private String transferRequestStatus;
    private boolean deceased;

    public Patient(int id, String nameInitials,String currentLocation,String sex,Time arrivalTime, String initialDiagnosis,
                   boolean needsSideRoom,boolean acceptedByMedicine,String nextDestination,Time estimatedTimeOfNext,
                   boolean ttaSignedOff,boolean suitableForDischargeLounge,String transferRequestStatus,boolean deceased) {
        this.id = id;
        this.nameInitials = nameInitials;
        this.currentLocation = currentLocation;
        this.sex = sex;
        this.arrivalTime = arrivalTime;
        this.initialDiagnosis = initialDiagnosis;
        this.needsSideRoom = needsSideRoom;
        this.acceptedByMedicine = acceptedByMedicine;
        this.nextDestination = nextDestination;
        this.estimatedTimeOfNext = estimatedTimeOfNext;
        this.ttaSignedOff = ttaSignedOff;
        this.suitableForDischargeLounge = suitableForDischargeLounge;
        this.transferRequestStatus = transferRequestStatus;
        this.deceased = deceased;
    }


    public int getId() {return id;}
    public String getNameInitials() {return nameInitials;}
    public String getCurrentLocation() {return currentLocation;}
    public String getSex() {return sex;}
    public Time getArrivalTime() {return arrivalTime;}
    public String getInitialDiagnosis() {return initialDiagnosis;}
    public boolean getNeedsSideRoom() {return needsSideRoom;}
    public boolean getAcceptedByMedicine() {return acceptedByMedicine;}
    public String getNextDestination() {return nextDestination;}
    public Time getEstimatedTimeOfNext() {return estimatedTimeOfNext;}
    public boolean getTtaSignedOff() {return ttaSignedOff;}
    public boolean getSuitableForDischargeLounge() {return suitableForDischargeLounge;}
    public String getTransferRequestStatus() {return transferRequestStatus;}
    public boolean getDeceased() {return deceased;};
}
