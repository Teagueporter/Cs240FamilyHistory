package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import results.EventResult;
import results.EveryEventResult;
import results.EveryPersonResult;
import results.PersonResult;
import services.EventService;
import services.EveryEventService;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

public class EventHandler extends Handler{
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        EveryEventResult everyEventResult = new EveryEventResult();
        EventResult eventResult = new EventResult();

        try{
            if(exchange.getRequestMethod().toLowerCase().equals("get")){
                Headers reqHeaders = exchange.getRequestHeaders();
                String authToken = reqHeaders.getFirst("Authorization");

                String urlPath = exchange.getRequestURI().toString();
                String strArray[] = urlPath.split("/");

                //if it is equal to three then we want
                //to send just a single person back
                if(strArray.length == 3){
                    EventService eventService = new EventService();
                    eventResult = eventService.event(strArray[2], authToken);

                    if(eventResult.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else{
                        exchange.sendResponseHeaders(400, 0);
                    }
                    Gson gson = new Gson();
                    OutputStream resBody = exchange.getResponseBody();
                    writeString(gson.toJson(eventResult), resBody);
                    resBody.close();
                }
                //we want to send back all people with associatedUsername
                else if(strArray.length == 2){
                    EveryEventService everyEventService = new EveryEventService();
                    everyEventResult = everyEventService.eEvent(authToken);

                    if(everyEventResult.isSuccess()) {
                        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    }
                    else{
                        exchange.sendResponseHeaders(400, 0);
                    }
                    Gson gson = new Gson();
                    OutputStream resBody = exchange.getResponseBody();
                    writeString(gson.toJson(everyEventResult), resBody);
                    resBody.close();
                }
            }
        } catch(IOException exception){
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            exchange.getResponseBody().close();
            exception.printStackTrace();
        }
    }
}
