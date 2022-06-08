package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.LoadRequest;
import results.LoadResult;
import services.LoadService;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;

public class LoadHandler extends Handler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

                //get the request data
                InputStream requestBody = exchange.getRequestBody();
                String reqData = readString(requestBody);

                //convert the request data to gson and send to load request
                Gson gson = new Gson();
                LoadRequest request = gson.fromJson(reqData, LoadRequest.class);

                //call the register service on the request
                LoadService loadService = new LoadService();
                LoadResult registerResult = loadService.load(request);

                //if the result is successful send correct header
                if(registerResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else{
                    exchange.sendResponseHeaders(400, 0);
                }

                //update the response body and close it
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(registerResult,resBody);
                resBody.close();
                exchange.getResponseBody().close();

            }
        }catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }
}
