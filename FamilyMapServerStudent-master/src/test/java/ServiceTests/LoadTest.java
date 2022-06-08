package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoadRequest;
import results.LoadResult;
import services.LoadService;

import static org.junit.jupiter.api.Assertions.*;

public class LoadTest {
    DatabaseManager db = new DatabaseManager();
    LoadRequest loadRequest = new LoadRequest();
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());
        EventDao eventDao = new EventDao(db.getConnection());
        PersonDao personDao = new PersonDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        User user2 = new User("CatoBlack", "12345", "Teagueporter@gmail.com", "James", "porter", "m", "9872654");
        User user3 = new User("WTeagueP", "12345", "Flamingos@gmail.com", "John", "porter", "m", "983457");

        Person person = new Person("12345", "Ogtwigs", "Tanna", "Clegg", "f", "82938", "092394", "987654321");
        Person person2 = new Person("1245", "Ogtwigs", "David", "jones", "m", "2346", "0347", null);
        Person person3 = new Person("2345", "Ogtwigs", "Julian", "Mackelbee", "f", "1236", "3246", null);

        Event event = new Event("123456789", "Ogtwigs", "987654321", 1.098f, 8.898f, "USA", "South Jordan", "Birth Day", 2022);
        Event event2 = new Event("123456789123", "Ogtwigs", "987654321", 97.098f, 83.898f, "USA", "South Jordan", "Death", 18898);
        Event event3 = new Event("126789", "Ogtwigs", "987654321", 1.098f, 8.898f, "USA", "South Jordan", "Birth", 2032);

        userDao.clear();
        personDao.clearAll();
        eventDao.clear();

        User[] users = new User[3];
        Person[] persons = new Person[3];
        Event[] events = new Event[3];

        users[0] = user;
        users[1] = user2;
        users[2] = user3;

        persons[0] = person;
        persons[1] = person2;
        persons[2] = person3;

        events[0] = event;
        events[1] = event2;
        events[2] = event3;

        loadRequest.setEvents(events);
        loadRequest.setPersons(persons);
        loadRequest.setUsers(users);

        db.closeDB(true);

    }
    @Test
    public void Eventpass(){
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertEquals(true, loadResult.isSuccess());
    }

    @Test
    public void EventPass2(){
        LoadService loadService = new LoadService();
        LoadResult loadResult = loadService.load(loadRequest);
        assertEquals("Successfully added 3 users, 3 persons, and 3 events to the database.", loadResult.getMessage());
    }
}
