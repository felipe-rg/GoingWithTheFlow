package Client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.sql.Timestamp;

public class Client {


    public Client() {   }
    //FIXME this is not at all useful - need neo's
    public ArrayList<Patient> makeGetRequest(String fields, String table, String criteria) throws IOException, SQLException {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        try {// Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= DriverManager.getConnection(dbUrl, "postgres", "root");
        try {
            Statement s = conn.createStatement();
            String sqlStr = "SELECT "+fields+" FROM "+table+" WHERE "+criteria+";";
            ResultSet ret = s.executeQuery(sqlStr);
            while(ret.next()) {
                /*Patient p = new Patient(ret.getInt("id"), ret.getString("nameinitials") ,
                        ret.getInt("currentlocation"),ret.getString("sex"),
                        ret.getTimestamp("arrivaldatetime"), ret.getString("initialDiagnosis"),
                ret.getBoolean("needssideroom"),ret.getBoolean("acceptedbymedicine"),
                        ret.getInt("nextdestination"),ret.getTimestamp("estimatedtimeofnext"),
                        ret.getBoolean("ttasignedoff"),ret.getBoolean("suitablefordischargelounge"),
                        ret.getString("transferrequeststatus"),ret.getBoolean("deceased"), ret.getInt("bedid"));*/
                Patient p = new Patient();
                p.setId(ret.getInt("id"));
                patients.add(p);
            }
            ret.close();
        } catch (Exception e) {}

        return patients;
    }

    public void makePutRequest(String table, String changes, String condition) throws IOException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        try {// Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= DriverManager.getConnection(dbUrl, "postgres", "root");
        try {
            Statement s = conn.createStatement();
            String sqlStr = "UPDATE "+table+" SET "+changes+" WHERE "+condition+";";
            s.execute(sqlStr);
        } catch (Exception e) {}
    }

    public void makePostRequest(Patient p) throws IOException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        try {// Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= DriverManager.getConnection(dbUrl, "postgres", "root");
        try {
            Statement s = conn.createStatement();
            String sqlStr = "INSERT INTO patients(nameinitials, sex, initialdiagnosis, needssideroom)" +
                    "VALUES('"+p.getNameInitials()+"','"+p.getSex()+"','"+p.getInitialDiagnosis()+"',"+p.getNeedsSideRoom()+")";
            s.execute(sqlStr);
        } catch (Exception e) {}
    }

    public void makeDeleteRequest(String sqlString) throws IOException, SQLException {
        String dbUrl = "jdbc:postgresql://localhost:5432/postgres";
        try {// Registers the driver
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {}
        Connection conn= DriverManager.getConnection(dbUrl, "postgres", "root");
        try {
            Statement s = conn.createStatement();
            s.execute(sqlString);
        } catch (Exception e) {}
    }



}
