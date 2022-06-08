package com.example.fmsclient;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import request.LoginRequest;
import request.RegRequest;
import results.ClearResult;
import results.EveryEventResult;
import results.EveryPersonResult;
import results.LoginResults;
import results.RegisterResults;


public class ServerProxy {

    private String host;
    private String port;

    public ServerProxy(String serverHost, String serverPort) {
        this.host = serverHost;
        this.port = serverPort;
    }

    public LoginResults login(LoginRequest request) {

        String reqData = "{\"username\":\"" + request.getUsername() +
                "\", \"password\":\"" + request.getPassword() +
                "\"}";

        String respData = serverPost(reqData, "/user/login");

        if (respData == null) {
            return new LoginResults("Error: Password is incorrect", false);
        }

        Gson gson = new Gson();
        LoginResults loginResults = gson.fromJson(respData, LoginResults.class);

        return loginResults;
    }

    public RegisterResults register(RegRequest request) {

        String reqData = "{\"username\":\"" + request.getUsername() +
                "\", \"password\":\"" + request.getPassword() +
                "\", \"email\":\"" + request.getEmail() +
                "\", \"firstName\":\"" + request.getFirstName() +
                "\", \"lastName\":\"" + request.getLastName() +
                "\", \"gender\":\"" + request.getGender() +
                "\"}";

        String respData = serverPost(reqData, "/user/register");

        if (respData == null) {
            return new RegisterResults("Error: Password is incorrect", false);
        }

        Gson gson = new Gson();
        RegisterResults results = gson.fromJson(respData, RegisterResults.class);

        return results;
    }

    public EveryPersonResult getPeople(String authtoken) {
        String people = serverGet(authtoken, "/person");
        Gson gson = new Gson();
        EveryPersonResult everyPersonResult = gson.fromJson(people, EveryPersonResult.class);
        return everyPersonResult;
    }

    public EveryEventResult getEvents(String authtoken) {
        String events = serverGet(authtoken, "/event");
        Gson gson = new Gson();
        EveryEventResult everyEventResult = gson.fromJson(events, EveryEventResult.class);
        return everyEventResult;

    }

    public ClearResult clear() {
        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + host + ":" + port + "/clear");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);

            // Connect to the server and send the HTTP request
            http.connect();

            OutputStream reqBody = http.getOutputStream();

            reqBody.close();

            String respData = readString(http.getInputStream());

            Gson gson = new Gson();

            ClearResult result = gson.fromJson(respData, ClearResult.class);

            return result;
        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    private String serverPost(String requestData, String funcname) {
        try {
            // Create a URL indicating where the server is running, and which
            // web API operation we want to call
            URL url = new URL("http://" + host + ":" + port + funcname);

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Specify that we are sending an HTTP POST request
            http.setRequestMethod("POST");

            // Indicate that this request will contain an HTTP request body
            http.setDoOutput(true);

            // Connect to the server and send the HTTP request
            http.connect();

            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(requestData, reqBody);

            reqBody.close();


            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                return respData;

            } else {
                return null;
            }
        } catch (IOException e) {
            // An exception was thrown, so display the exception's stack trace
            e.printStackTrace();
        }
        return null;
    }

    private String serverGet(String authtoken, String name) {
        String result = null;

        try {
            URL url = new URL("http://" + host + ":" + port + name);


            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection) url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");

            // Indicate that this request will not contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authtoken);

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                result = readString(respBody);
            } else {
                //System.out.println("serverGet ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
