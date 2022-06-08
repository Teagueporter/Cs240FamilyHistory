package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import results.ClearResult;
import services.ClearService;

import java.io.*;
import java.net.HttpURLConnection;


public class ClearHandler extends Handler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                // creates service and calls clear service on the result
                ClearService clearService = new ClearService();
                ClearResult clearResult = clearService.clear();

                //if the clear is successful we send ok otherwise send 400
                if(clearResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else{
                    exchange.sendResponseHeaders(400, 0);
                }

                //creating the response body using gson
                Gson gson = new Gson();
                OutputStream resBody = exchange.getResponseBody();
                writeString(gson.toJson(clearResult), resBody);
                resBody.close();
            }
        }catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }

}

