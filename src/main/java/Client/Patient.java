package Client;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
/*Patient class with attributes corresponding to the fields of the table Patients in the database
 and their getter methods.*/
public class Patient implements Serializable {

    private int id;
    private String patientId;
    private String sex;
    private LocalDate dateOfBirth;
    private int currentWardId;
    private int currentBedId;
    private LocalDateTime arrivalDateTime;
    private String initialDiagnosis;
    private boolean needsSideRoom;
    private boolean acceptedByMedicine;
    private int nextDestination;
    private LocalDateTime estimatedDateTimeOfNext;
    private boolean ttaSignedOff;
    private boolean suitableForDischargeLounge;
    private String transferRequestStatus;
    private boolean deceased;

    public Patient(){}

    /*two constructors available depending on whether the patient is instantiated from the PatientForm to be added
    to the database(fewer attributes/field required) or instantiated from the servlet 'end along with the data from
    all the associated fields from the table (all attributes required)*/
    public Patient(String patientId,String sex,LocalDate dateOfBirth,String initialDiagnosis,boolean needsSideRoom) {
        this.patientId = patientId;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.initialDiagnosis = initialDiagnosis;
        this.needsSideRoom = needsSideRoom;
    }

    public Patient(int id, String patientId,LocalDate dateOfBirth,int currentWardId,int currentBedId,String sex,LocalDateTime arrivalDateTime, String initialDiagnosis,
                   boolean needsSideRoom,boolean acceptedByMedicine,int nextDestination,LocalDateTime estimatedTimeOfNext,
                   boolean ttaSignedOff,boolean suitableForDischargeLounge,String transferRequestStatus,boolean deceased) {
        this.id = id;
        this.patientId = patientId;
        this.dateOfBirth = dateOfBirth;
        this.currentBedId = currentBedId;
        this.currentWardId = currentWardId;
        this.sex = sex;
        this.arrivalDateTime = arrivalDateTime;
        this.initialDiagnosis = initialDiagnosis;
        this.needsSideRoom = needsSideRoom;
        this.acceptedByMedicine = acceptedByMedicine;
        this.nextDestination = nextDestination;
        this.estimatedDateTimeOfNext = estimatedTimeOfNext;
        this.ttaSignedOff = ttaSignedOff;
        this.suitableForDischargeLounge = suitableForDischargeLounge;
        this.transferRequestStatus = transferRequestStatus;
        this.deceased = deceased;
    }

    public int getId() {return id;}
    public String getPatientId() {return patientId;}
    public String getDateOfBirth() {return dateFormatter(dateOfBirth);}
    public int getCurrentWardId() {return currentWardId;}
    public int getCurrentBedId() {return currentBedId;}
    public String getSex() {return sex;}
    public LocalDateTime getArrivalDateTime() {return arrivalDateTime;}
    public String getInitialDiagnosis() {return initialDiagnosis;}
    public boolean getNeedsSideRoom() {return needsSideRoom;}
    public boolean getAcceptedByMedicine() {return acceptedByMedicine;}
    public int getNextDestination() {return nextDestination;}
    public LocalDateTime getEstimatedTimeOfNext() {return estimatedDateTimeOfNext;}
    public boolean getTtaSignedOff() {return ttaSignedOff;}
    public boolean getSuitableForDischargeLounge() {return suitableForDischargeLounge;}
    public String getTransferRequestStatus() {return transferRequestStatus;}
    public boolean getDeceased() {return deceased;}
    public String dateFormatter(LocalDate localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        return localDateTime.format(formatter);
    }
    public void setSex(String sex){ this.sex = sex; }
}
