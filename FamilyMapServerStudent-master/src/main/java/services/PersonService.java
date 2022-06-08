package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.DatabaseManager;
import dao.PersonDao;
import model.AuthToken;
import model.Person;
import results.PersonResult;

/**
 * Returns the single Person object with the specified ID
 * (if the person is associated with the current user).
 * The current user is determined by the provided authtoken.
 */

public class PersonService {
    public PersonResult person(String personID, String authtoken){
        DatabaseManager db = new DatabaseManager();
        try{
            db.openDB();
            PersonDao pDao = new PersonDao(db.getConnection());
            AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
            AuthToken authToken = null;
            if(!aDao.findToken(authtoken)){
                db.closeDB(true);
                return new PersonResult("Error: authToken not found", false);
            }else{
                authToken = aDao.find(authtoken);
            }

            if(pDao.findPerson(personID)){
                Person person = pDao.find(personID);
                if(!person.getAssociatedUsername().equals(authToken.getUsername())){
                    db.closeDB(true);
                    return new PersonResult("Error: Person not in your family tree", false);
                }
                else {
                    db.closeDB(true);
                    return new PersonResult(person.getPersonID(), person.getAssociatedUsername(), person.getFirstName(), person.getLastName(), person.getGender(), person.getFatherID(), person.getMotherID(), person.getSpouseID(), true);
                }
            }
            else{
                db.closeDB(true);
                return new PersonResult("Error: person no found", false);
            }

        } catch (DataAccessException e) {
            e.printStackTrace();
            db.closeDB(false);
            return new PersonResult("Error: error in database", false);
        }

    }
}
