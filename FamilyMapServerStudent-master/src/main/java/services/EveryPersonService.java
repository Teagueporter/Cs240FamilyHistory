package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.DatabaseManager;
import dao.PersonDao;
import model.AuthToken;
import model.Person;
import results.EveryPersonResult;

/**
 * Returns ALL family members of the current user.
 * The current user is determined by the provided authtoken.
 */

public class EveryPersonService {
    public EveryPersonResult ePerson(String authToken){
        DatabaseManager db = new DatabaseManager();
        try{
            db.openDB();
            PersonDao pDao = db.getPersonDao();
            AuthTokenDao aDao = db.getAuthDao();

            if(aDao.findToken(authToken)){
                AuthToken authToken1 = aDao.find(authToken);

                Person[] data = pDao.findAll(authToken1.getUsername());
                db.closeDB(true);
                return new EveryPersonResult(data, true);
            }
            else{
                db.closeDB(true);
                return new EveryPersonResult("Error: person not found", false);
            }

        } catch (DataAccessException e) {
            return new EveryPersonResult("Error in database", false);
        }
    }
}
