package services;


import dao.AuthTokenDao;
import dao.DataAccessException;
import dao.DatabaseManager;
import dao.UserDao;
import model.AuthToken;
import model.User;
import request.RegRequest;
import results.FillResult;
import results.RegisterResults;

import java.util.UUID;

/**
 * Creates a new user account (user row in the database)
 * Generates 4 generations of ancestor data for the new user (just like the /fill endpoint if called with a generations value of 4 and this new user’s username as parameters)
 * Logs the user in
 * Returns an authtoken
 */

public class RegisterService {

    public RegisterService(){

    }

    public RegisterResults register(RegRequest r) throws DataAccessException{

        DatabaseManager database = new DatabaseManager();

        String personId = UUID.randomUUID().toString();

        User newUser = new User(r.getUsername(), r.getPassword(), r.getEmail(),
                r.getFirstName(), r.getLastName(), r.getGender(), personId);

        try{
            database.openDB();
            UserDao user = new UserDao(database.getConnection());
            user.insert(newUser);
            AuthTokenDao aDao = new AuthTokenDao(database.getConnection());
            String authtoken = UUID.randomUUID().toString();
            aDao.insert(new AuthToken(authtoken, r.getUsername()));
            database.closeDB(true);

            //fill the users tree
            FillService fillService = new FillService();
            FillResult fillResult = fillService.fill(r.getUsername(), 4);

            return new RegisterResults(authtoken, r.getUsername(), personId, true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            database.closeDB(false);
            return new RegisterResults(e.getMessage(), false);
        }

    }
}
