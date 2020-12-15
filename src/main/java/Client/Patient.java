package Client;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;

public class Patient implements Serializable {

    private int id;
    private String nameInitials;
    private String currentLocation;
    private char sex;
    private Time arrivalTime;
    private String initialDiagnosis;
    private boolean needsSideRoom;
    private boolean acceptedByMedicine;
    private String nextDestination;
    private Time estimatedTimeOfNext;
    private boolean ttaSignedOff;
    private boolean suitableForDischargeLounge;
    private char transferRequestStatus;
    private boolean deceased;

    public Patient(int id, String nameInitials,String currentLocation,char sex,Time arrivalTime, String initialDiagnosis,
                   boolean needsSideRoom,boolean acceptedByMedicine,String nextDestination,Time estimatedTimeOfNext,
                   boolean ttaSignedOff,boolean suitableForDischargeLounge,char transferRequestStatus,boolean deceased) {
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
}
