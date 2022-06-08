package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import results.DefaultResponse;
import results.EveryPersonResult;
import results.PersonResult;
import services.EveryPersonService;
import services.PersonService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class PersonHandler extends Handler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EveryPersonResult everyPersonResult = new EveryPersonResult();
        PersonResult personResult = new PersonResult();

        try{
            if(exchange.getRequestMethod().toLowerCase().equals("get")){
                Headers reqHeaders = exchange.getRequestHeaders();
                String authToken = reqHeaders.getFirst("Authorization");

                String urlPath = exchange.getRequestURI().toString();
                String strArray[] = urlPath.split("/");

                //if it is equal to three then we want
                //to send just a single person back
                if(strArray.length == 3){
                    PersonService personService = new PersonService();
                    personResult = personService.person(strArray[2], authToken);

                    if(personResult.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else{
                        exchange.sendResponseHeaders(400, 0);
                    }
                    Gson gson = new Gson();
                    OutputStream resBody = exchange.getResponseBody();
                    writeString(gson.toJson(personResult), resBody);
                    resBody.close();
                }
                //we want to send back all people with associatedUsername
                else if(strArray.length == 2){
                    EveryPersonService everyPersonService = new EveryPersonService();
                    everyPersonResult = everyPersonService.ePerson(authToken);

                    if(everyPersonResult.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else{
                        exchange.sendResponseHeaders(400, 0);
                    }
                    Gson gson = new Gson();
                    OutputStream resBody = exchange.getResponseBody();
                    writeString(gson.toJson(everyPersonResult), resBody);
                    resBody.close();
                }
            }
        }catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }

    }
}
