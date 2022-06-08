package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import request.LoginRequest;
import results.LoginResults;
import results.RegisterResults;
import services.LoginService;

import java.io.*;
import java.net.HttpURLConnection;

public class LoginHandler extends Handler{
    boolean success = false;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {

               //getting request body data
                InputStream requestBody = exchange.getRequestBody();
                String reqData = readString(requestBody);

                //convert to gson to send to server
                Gson gson = new Gson();
                LoginRequest request = gson.fromJson(reqData, LoginRequest.class);

                //call the login service on the request
                LoginService loginService = new LoginService();
                LoginResults loginResults = loginService.login(request);

                //send response headers and update the response body then close it

                if(loginResults.isSuccess()){
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);

                }
                else{
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }

                OutputStream resBody = exchange.getResponseBody();
                writeString(gson.toJson(loginResults), resBody);
                resBody.close();
            }
        }catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }
}
