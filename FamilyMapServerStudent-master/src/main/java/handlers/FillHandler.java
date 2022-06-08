package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.FillRequest;
import request.LoadRequest;
import results.DefaultResponse;
import results.FillResult;
import results.LoadResult;
import services.FillService;

import java.io.*;
import java.net.HttpURLConnection;

public class FillHandler extends Handler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().equalsIgnoreCase("post")) {

                String urlPath = exchange.getRequestURI().toString();
                //urlPath.substring(1);
                String strArray[] = urlPath.split("/");

                FillService fillService = new FillService();
                FillResult fillResult;

                if(strArray.length == 3){
                    fillResult = fillService.fill(strArray[2], 4);
                    System.out.println(urlPath);
                }
                else {
                    fillResult = fillService.fill(strArray[2], Integer.parseInt(strArray[3]));
                    System.out.println(urlPath);
                }

                //if the result is successful send correct header
                if(fillResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                //update the response body and close it
                Gson gson = new Gson();
                OutputStream resBody = exchange.getResponseBody();
                writeString(gson.toJson(fillResult), resBody);
                resBody.close();

            }
        }catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }
}
