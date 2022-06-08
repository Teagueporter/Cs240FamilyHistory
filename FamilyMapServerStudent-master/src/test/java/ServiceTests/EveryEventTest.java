package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.EveryEventResult;
import services.EveryEventService;

import static org.junit.jupiter.api.Assertions.*;

public class EveryEventTest {
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
        Event event2 = new Event("123456789123", "Ogtwigs", "987654321", 97.098f, 83.898f, "USA", "South Jordan", "Death", 18898);
        Event event3 = new Event("126789", "Ogtwigs", "987654321", 1.098f, 8.898f, "USA", "South Jordan", "Birth", 2032);
        AuthToken authToken = new AuthToken("1234567890", "Ogtwigs");

        userDao.clear();
        eventDao.clear();
        authTokenDao.clear();

        userDao.insert(user);
        eventDao.insert(event);
        eventDao.insert(event2);
        eventDao.insert(event3);
        authTokenDao.insert(authToken);

        db.closeDB(true);

    }
    @Test
    public void EveryEventpass(){
        EveryEventService everyEventService = new EveryEventService();
        EveryEventResult everyEventResult = everyEventService.eEvent("1234567890");
        assertEquals(true, everyEventResult.isSuccess());
    }

    @Test
    public void EveryEventfail(){
        EveryEventService everyEventService = new EveryEventService();
        EveryEventResult everyEventResult = everyEventService.eEvent("17890");
        assertEquals(false, everyEventResult.isSuccess());
    }
}
