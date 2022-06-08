package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.EventResult;
import services.EventService;
import static org.junit.jupiter.api.Assertions.*;


public class EventTest {
    DatabaseManager db = new DatabaseManager();
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());
        EventDao eventDao = new EventDao(db.getConnection());
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        Event event = new Event("123456789", "Ogtwigs", "987654321", 1.098f, 8.898f, "USA", "South Jordan", "Birth Day", 2022);
        AuthToken authToken = new AuthToken("1234567890", "Ogtwigs");

        userDao.clear();
        eventDao.clear();
        authTokenDao.clear();

        userDao.insert(user);
        eventDao.insert(event);
        authTokenDao.insert(authToken);

        db.closeDB(true);

    }
    @Test
    public void Eventpass(){
        EventService eventService = new EventService();
        EventResult eventResult = eventService.event("123456789", "1234567890");
        assertEquals(true, eventResult.isSuccess());
    }

    @Test
    public void Eventfail(){
        EventService eventService = new EventService();
        EventResult eventResult = eventService.event("1256789", "1234890");
        assertEquals(false, eventResult.isSuccess());
    }
}
