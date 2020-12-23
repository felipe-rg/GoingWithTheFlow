package Client;

public class Bed {
    private int id;
    private int wardId;
    private String status;
    private String forSex;
    private boolean hasSideRoom;
    //FIXME bed number for actual bed number rather than our id?

    public Bed(){}

    public void setId(int id){this.id = id;}
    public void setWardId(int wardId){this.wardId=wardId;}
    public void setStatus(String status){this.status = status;}
    public void setForSex(String sex){this.forSex=sex;}
    public void setHasSideRoom(boolean hasSideRoom){this.hasSideRoom = hasSideRoom;}

    public int setId(){return this.id;}
    public int setWardId(){return this.wardId;}
    public String setStatus(){return this.status;}
    public String setForSex(){return this.forSex;}
    public boolean setHasSideRoom(){return this.hasSideRoom;}
}
