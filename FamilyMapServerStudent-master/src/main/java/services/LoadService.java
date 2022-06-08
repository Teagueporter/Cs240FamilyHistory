package services;

import dao.*;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import results.LoadResult;

import java.util.UUID;

/**
 * Clears all data from the database (just like the /clear API)
 * Loads the user, person, and event data from the request body into the database.
 */

public class LoadService {
    public LoadResult load(LoadRequest r){
        DatabaseManager database = new DatabaseManager();
        try{
            database.openDB();

            //clears database
            UserDao userDao = new UserDao(database.getConnection());
            AuthTokenDao authDao = new AuthTokenDao(database.getConnection());
            EventDao eventDao = new EventDao(database.getConnection());
            PersonDao personDao = new PersonDao(database.getConnection());
            userDao.clear();
            personDao.clearAll();
            authDao.clear();
            eventDao.clear();

            for(User user : r.getUsers()){
                database.getUserDao().insert(user);
                AuthToken newAuth = new AuthToken(UUID.randomUUID().toString(), user.getUsername());
                authDao.insert(newAuth);
            }
            for(Person person : r.getPersons()){
                database.getPersonDao().insert(person);
            }
            for(Event event : r.getEvents()){
                database.getEventDao().insert(event);
            }
            database.closeDB(true);

            return new LoadResult("Successfully added " + r.getUsers().length
                    + " users, " + r.getPersons().length + " persons, and " +
                    r.getEvents().length + " events to the database.", true);

        } catch (DataAccessException e) {
            database.closeDB(false);
            e.printStackTrace();
            return new LoadResult("Error: failed to load data", false);
        }
    }
}
