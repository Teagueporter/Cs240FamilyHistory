package request;

import model.Event;
import model.Person;
import model.User;

public class LoadRequest {
    private User[] users;
    private Person[] persons;
    private Event[] events;

    public User[] getUsers() {
        return users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }
}
