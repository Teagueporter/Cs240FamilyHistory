package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import dao.DataAccessException;
import request.RegRequest;
import results.ClearResult;
import results.RegisterResults;
import services.RegisterService;

import java.io.*;
import java.net.HttpURLConnection;

public class RegisterHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                //get the request data
                InputStream requestBody = exchange.getRequestBody();
                String reqData = readString(requestBody);
                //System.out.println(reqData);

                //convert to gson
                Gson gson = new Gson();
                RegRequest request = gson.fromJson(reqData, RegRequest.class);

                //call the register service on the request
                RegisterService registerService = new RegisterService();
                RegisterResults registerResult = registerService.register(request);

                if(registerResult.isSuccess()) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }
                else{
                    exchange.sendResponseHeaders(400, 0);
                }
                Writer resBody = new OutputStreamWriter(exchange.getResponseBody());
                gson.toJson(registerResult,resBody);
                resBody.close();
                exchange.getResponseBody().close();
//                OutputStream resBody = exchange.getResponseBody();
//                String json = gson.toJson(registerResult);
//                writeString(json, resBody);
//                resBody.close();
            }
        }catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}


