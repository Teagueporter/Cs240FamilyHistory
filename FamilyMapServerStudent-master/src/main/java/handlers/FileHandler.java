package handlers;

import java.io.*;
import java.net.*;
import java.nio.file.Files;

import com.sun.net.httpserver.*;

public class FileHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        boolean success = false;

        try {
            //check if this is a get request
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                // set the url path
                String urlPath = exchange.getRequestURI().toString();

                //if the url path is null or / set to index.html
                if (urlPath.length() != 1 && urlPath != null) {
                } else {
                    urlPath = "/index.html";
                }

                //append the url to web
                String filePath = "web" + urlPath;

                //create new file with and check to see if it exists
                File newFile = new File(filePath);
                OutputStream respBody;

                //check if the new file exists, if not send to the 404 web page
                if(!newFile.exists()){
                    filePath = "web/html/404.html";
                    File file = new File(filePath);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
                    respBody = exchange.getResponseBody();
                    Files.copy(file.toPath(), respBody);
                }
                else{
                    respBody = exchange.getResponseBody();
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    Files.copy(newFile.toPath(), respBody);
                }

                //close the response body
                exchange.getResponseBody().close();
            }
        }
        catch (IOException e) {
            //if there is an exception thrown display a server error
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
        }
    }
}

