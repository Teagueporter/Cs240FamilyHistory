package results;

import model.Event;
import model.Person;
import model.User;

/**
 * LoadResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */

public class LoadResult extends DefaultResponse{
    User[] userList;
    Person[] persons;
    Event[] events;

    public LoadResult(User[] userList, Person[] persons, Event[] events, boolean success){
        this.userList = userList;
        this.persons = persons;
        this.events = events;
        this.success = success;
        this.message = null;
    }
    public LoadResult(String message, boolean success){
        this.userList = null;
        this.persons = null;
        this.events = null;
        this.message = message;
        this.success = success;

    }
}
