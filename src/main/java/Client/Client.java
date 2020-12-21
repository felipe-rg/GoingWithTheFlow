package Client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client {


    public Client() {   }

    public ArrayList<Patient> makeGetRequest(String fields, String table, String criteria) throws IOException {
        ArrayList<Patient> patients = new ArrayList<Patient>();
        Gson gson = new Gson();
        String url = "https://goingwiththeflowservlet.herokuapp.com/home?fields="+fields+"&table="+table+"&where="+criteria;
        URL servletURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(conn.getInputStream(), "utf-8"));
        String inputLine;

        // Read the body of the response
        while ((inputLine = bufferedReader.readLine()) != null) {
            patients = gson.fromJson(inputLine,ArrayList.class);
        }
        bufferedReader.close();
        return patients;
    }

    public void makePutRequest(String table, String changes, String condition) throws IOException {
        ArrayList<String> patients = new ArrayList<String>();
        Gson gson = new Gson();
        String url = "https://goingwiththeflowservlet.herokuapp.com/home?table="+table+"&changes="+changes+"&condition="+condition;
        URL servletURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(conn.getInputStream(), "utf-8"));
        String inputLine;

        // Read the body of the response
        while ((inputLine = bufferedReader.readLine()) != null) {
            patients = gson.fromJson(inputLine,ArrayList.class);
        }
        bufferedReader.close();
        //return patients;
    }

    public void makePostRequest(Patient p) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);
        URL servletURL = new URL("https://goingwiththeflowservlet.herokuapp.com/home");
        HttpURLConnection conn= (HttpURLConnection) servletURL.openConnection();

        // Set up the header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "text/html");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length));
        conn.setDoOutput(true);

        // Write the body of the request
        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(body, 0, body.length);
        }

        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(conn.getInputStream(), "utf-8"));
        String inputLine;

        // Read the body of the response
        while ((inputLine = bufferedReader.readLine()) != null) {
            System.out.println(inputLine);
        }
        bufferedReader.close();
    }

    public void makeDeleteRequest(String sqlString) throws IOException {
        // Set up the body data
        String message = sqlString;
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        URL servletURL = new URL("https://goingwiththeflowservlet.herokuapp.com/home");
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();

        // Set up the header
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Accept", "text/html");
        conn.setRequestProperty("charset", "utf-8");
        conn.setRequestProperty("Content-Length", Integer.toString(body.length));
        conn.setDoOutput(true);

        // Write the body of the request
        try (OutputStream outputStream = conn.getOutputStream()) {
            outputStream.write(body, 0, body.length);
        }

        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(conn.getInputStream(), "utf-8"));
        String inputLine;

        // Read the body of the response
        while ((inputLine = bufferedReader.readLine()) != null) {
            System.out.println(inputLine);
        }
        bufferedReader.close();
    }



}
