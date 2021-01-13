package Client;

import java.io.Serializable;

/*Ward class with attributes corresponding to the fields of the table Wards in the database
 and their getter methods.*/
public class Ward implements Serializable {

    private int wardId;
    private String wardName;
    private String wardType;

    public Ward(int wardId,String wardName,String wardType) {
        this.wardId=wardId;
        this.wardName=wardName;
        this.wardType=wardType;
    }

    public int getWardId() {return wardId;}
    public String getWardName() {return wardName;}
    public String getWardType() {return wardType;}
}
