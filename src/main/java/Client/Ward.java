package Client;
import java.io.Serializable;

public class Ward implements Serializable {

    private int wardId;
    private String wardName;

    public Ward(int wardId,String wardName) {
        this.wardId=wardId;
        this.wardName=wardName;
    }

    public int getWardId() {return wardId;}
    public String getWardName() {return wardName;}
}

