package Client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Client extends fromJson{

    private static final Logger log = Logger.getLogger(Client.class.getName());

    public Client() {   }

    /*Makes a doGet request to the servlet by passing appropriate search parameters as key pair values into the
      URL extension to be used for a database SELECT query. Then reads the body of the servlet response decoding the data
      coming as a JSON string into an ArrayList of JSON strings.*/
    public ArrayList<String> makeGetRequest(String fields,String table,String condition) throws IOException {
        ArrayList<String> jsonStrings = new ArrayList<String>();
        Gson gson = new Gson();
        String url = "https://goingwiththeflowservlet.herokuapp.com/home?fields="+fields+"&table="+table+"&condition="+condition;
        URL servletURL = new URL(url);

        try {
            HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            log.info("Attempted connection to servlet to make a doGet request.");
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream(), "utf-8"));
            String inputLine;

            // Read the body of the response
            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonStrings = gson.fromJson(inputLine,ArrayList.class);
            }
            bufferedReader.close();
            log.info("Received response with "+ jsonStrings.size() + " entries of " +table+ " objects.");

        } catch (IOException ioException) {
            log.severe("Unsuccessful connection to servlet to make a doGet request.");
            log.severe(ioException.toString());
        }
        return jsonStrings;
    }

    /*Makes a doPostRequest to the servlet sending a Patient object encoded in JSON.*/
    public void makePostRequest(Patient p) throws IOException {
        // Set up the body data
        Gson gson = new Gson();
        String jsonString = gson.toJson(p);
        byte[] body = jsonString.getBytes(StandardCharsets.UTF_8);
        URL servletURL = new URL("https://goingwiththeflowservlet.herokuapp.com/home");
        try {
            HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
            // Set up the header
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(body.length));
            conn.setDoOutput(true);
            log.info("Attempted connection to servlet to make a doPost request.");
            // Write the body of the request
            try (OutputStream outputStream = conn.getOutputStream()) {
                outputStream.write(body, 0, body.length);

                BufferedReader bufferedReader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream(), "utf-8"));
                bufferedReader.close();
                log.info("Succesfully posted Patient with patiend id: "+p.getPatientId()+".");
            }
        } catch (IOException ioException) {
            log.severe("Unsuccessful connection to servlet to make a doPost request.");
            log.severe(ioException.toString());
        }
    }

    /*Makes a doPut request to the servlet by passing appropriate edit parameters as key pair values into the
      URL extension to be used for a database UPDATE query.*/
    public void makePutRequest(String table,String change,String condition) throws IOException {
        try {
            String url = "https://goingwiththeflowservlet.herokuapp.com/home?table=" + table + "&change=" + change + "&condition=" + condition;
            URL servletURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-type", "application/html");
            conn.setRequestProperty("charset", "utf-8");
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream(), "utf-8"));
            log.info("Attempted connection to servlet to make a doPut request to edit "+table+" entry/ies where "+condition+" to "+change+".");
        } catch (IOException ioException) {
            log.severe("Unsuccessful connection to servlet to make a doPut request.");
            log.severe(ioException.toString());
        }
    }

    /*Makes a doDelete request to the servlet by passing appropriate parameters as key pair values into the
      URL extension to be used for a database DELETE query.*/
    public void makeDeleteRequest(String table,String condition) throws IOException {
        try {
            String url = "https://goingwiththeflowservlet.herokuapp.com/home?table=" + table + "&condition=" + condition;
            URL servletURL = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-type", "application/html");
            conn.setRequestProperty("charset", "utf-8");
            BufferedReader bufferedReader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream(), "utf-8"));
            log.info("Attempted connection to servlet to make a doDelete request to delete "+table+" entry/ies where "+condition+".");
        } catch (IOException ioException) {
            log.severe("Unsuccessful connection to servlet to make a doDelete request.");
            log.severe(ioException.toString());
        }
    }

}
