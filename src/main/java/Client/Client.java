package Client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Client {

    public Client() {   }

    public void makeGetRequest() throws IOException {
        URL servletURL = new URL("https://goingwiththeflowservlet.herokuapp.com/home");
        HttpURLConnection conn = (HttpURLConnection) servletURL.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept","text/html");
        conn.setRequestProperty("charset","utf-8");

        BufferedReader in = new BufferedReader(new InputStreamReader((servletURL.openStream())));

        String inputLine;
        while((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }

    public void makePostRequest(String sqlString) throws IOException {
        // Set up the body data
        String message = sqlString;
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        URL servletURL = new URL("https://goingwiththeflowservlet.herokuapp.com/home");
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) servletURL.openConnection();

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

    private void makeDeleteRequest(String sqlString) throws IOException {
        // Set up the body data
        String message = sqlString;
        byte[] body = message.getBytes(StandardCharsets.UTF_8);
        URL servletURL = new URL("https://goingwiththeflowservlet.herokuapp.com/home");
        HttpURLConnection conn = null;
        conn = (HttpURLConnection) servletURL.openConnection();

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