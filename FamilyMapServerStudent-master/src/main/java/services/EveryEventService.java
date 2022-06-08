package services;

import dao.*;
import model.AuthToken;
import model.Event;
import results.EveryEventResult;

/**
 * Returns ALL events for ALL family members of the current user.
 * The current user is determined from the provided auth token.
 */

public class EveryEventService {
    public EveryEventResult eEvent(String authToken){
        DatabaseManager db = new DatabaseManager();
        try{
            db.openDB();
            EventDao eDao = db.getEventDao();
            AuthTokenDao aDao = db.getAuthDao();

            if(aDao.findToken(authToken)){
                AuthToken authToken1 = aDao.find(authToken);

                Event[] data = eDao.findAll(authToken1.getUsername());
                db.closeDB(true);
                return new EveryEventResult(data, true);
            }
            else{
                db.closeDB(true);
                return new EveryEventResult("Error: Events no found", false);
            }

        } catch (DataAccessException e) {
            return new EveryEventResult("Error in database", false);
        }
    }
}
