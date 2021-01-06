package Client;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client extends fromJson{

    public Client() {   }

    public ArrayList<String> makeGetRequest(String fields,String table,String condition) throws IOException {
        ArrayList<String> jsonStrings = new ArrayList<String>();
        Gson gson = new Gson();
        String url = "https://goingwiththeflowservlet.herokuapp.com/home?fields="+fields+"&table="+table+"&condition="+condition;
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
            jsonStrings = gson.fromJson(inputLine,ArrayList.class);
        }
        bufferedReader.close();
        return jsonStrings;
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
    }

    public void makePutRequest(String table,String change,String condition) throws IOException {
        String url = "https://goingwiththeflowservlet.herokuapp.com/home?table="+table+"&change="+change+"&condition="+condition;
        URL servletURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-type", "application/html");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(conn.getInputStream(), "utf-8"));
    }

    public void makeDeleteRequest(String table,String condition) throws IOException {
        String url = "https://goingwiththeflowservlet.herokuapp.com/home?table="+table+"&condition="+condition;
        URL servletURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
        conn.setRequestMethod("DELETE");
        conn.setRequestProperty("Content-type", "application/html");
        conn.setRequestProperty("charset", "utf-8");
        BufferedReader bufferedReader = new BufferedReader(new
                InputStreamReader(conn.getInputStream(), "utf-8"));
    }


}
