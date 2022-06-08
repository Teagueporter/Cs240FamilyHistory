package services;

import dao.*;
import model.AuthToken;
import model.Event;
import results.EventResult;

/**
 * Returns the single Event object with the specified ID
 * (if the event is associated with the current user).
 * The current user is determined by the provided authtoken.
 */

public class EventService {
    public EventResult event(String eventID, String authtoken){
        DatabaseManager db = new DatabaseManager();
        try{
            db.openDB();
            EventDao eDao = new EventDao(db.getConnection());
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = null;
            if(!aDao.findToken(authtoken)){
                db.closeDB(true);
                return new EventResult("Error: authToken not found", false);
            }else {
                authToken = aDao.find(authtoken);
            }

            if(eDao.findEvent(eventID)){
                Event event = eDao.find(eventID);
                if(!event.getAssociatedUsername().equals(authToken.getUsername())){
                    db.closeDB(true);
                    return new EventResult("Error: Event for person not in your family tree", false);
                }
                else {
                    db.closeDB(true);
                    return new EventResult(event.getEventID(), event.getAssociatedUsername(), event.getPersonID(), event.getLatitude(), event.getLongitude(), event.getCountry(), event.getCity(), event.getEventType(), event.getYear(), true);
                }
            }
            else{
                db.closeDB(true);
                return new EventResult("Error: Event not found", false);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeDB(false);
            return new EventResult("Error: error in database", false);
        }
    }
}
