package services;

import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.DatabaseManager;
import dao.UserDao;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import results.LoginResults;

import java.util.UUID;

/**
 * Logs the user in
 * Returns an authtoken.
 */

public class LoginService {

    /**
     * Returns the login result
     * @param r a login request
     * @return the result
     */
    public LoginResults login(LoginRequest r) {
        DatabaseManager db = new DatabaseManager();
        try{
            db.openDB();
            UserDao user = new UserDao(db.getConnection());
            User newUser = user.find(r.getUsername());
            String authToken = UUID.randomUUID().toString();
            if(newUser == null){
                db.closeDB(true);
                return new LoginResults("Error: User does not exist", false);
            }
            else if((newUser.getPassword().equals( r.getPassword())) && (newUser.getUsername().equals(r.getUsername()))){
                AuthTokenDao aDao = new AuthTokenDao(db.getConnection());
                AuthToken authToken1 = new AuthToken(authToken, r.getUsername());
                aDao.insert(authToken1);
                db.closeDB(true);
                return new LoginResults(authToken, newUser.getUsername(), newUser.getPersonID(), true   );
            }
            else {
                db.closeDB(true);
                return new LoginResults("Error: Password is incorrect", false);
            }

        } catch (DataAccessException e) {
            LoginResults LoginResults = new LoginResults(e.toString(), false);
            db.closeDB(false);
            e.printStackTrace();
            return LoginResults;
        }

    }
}
