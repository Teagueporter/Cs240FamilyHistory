package services;

import dao.*;
import results.ClearResult;

/**
 * Deletes ALL data from the database, including user, authtoken, person, and event data
 */

public class ClearService {

    public ClearResult clear(){
        DatabaseManager database = new DatabaseManager();
        ClearResult clearResult;

        try {
            database.openDB();
            UserDao userDao = new UserDao(database.getConnection());
            AuthTokenDao authDao = new AuthTokenDao(database.getConnection());
            EventDao eventDao = new EventDao(database.getConnection());
            PersonDao personDao = new PersonDao(database.getConnection());

            userDao.clear();
            personDao.clearAll();
            authDao.clear();
            eventDao.clear();

            clearResult = new ClearResult("Clear succeeded.", true);
            database.closeDB(true);
        } catch (DataAccessException e) {
            clearResult = new ClearResult(e.toString(), false);
            database.closeDB(false);
            e.printStackTrace();
        }

        return clearResult;

    }
}
