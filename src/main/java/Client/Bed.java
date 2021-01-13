package Client;
import java.io.Serializable;
/*Bed class with attributes corresponding to the fields of the table Beds in the database
 and their getter methods*/


public class Bed implements Serializable {

    private int bedId;
    private int wardId;
    private String status;
    private String forSex;
    private boolean hasSideRoom;

    public Bed(int bedId,int wardId,String status,String forSex,boolean hasSideRoom) {
        this.bedId=bedId;
        this.wardId=wardId;
        this.status=status;
        this.forSex=forSex;
        this.hasSideRoom=hasSideRoom;
    }

    public int getBedId(){return bedId;}
    public int getWardId(){return wardId;}
    public String getStatus(){return status;}
    public String getForSex(){return forSex;}
    public boolean getHasSideRoom(){return hasSideRoom;}

    public void setSR(Boolean SR){ hasSideRoom = SR; }
    public void setSex(String forSex){ this.forSex = forSex; }
    public void setStatus(String status){ this.status = status; }
}

