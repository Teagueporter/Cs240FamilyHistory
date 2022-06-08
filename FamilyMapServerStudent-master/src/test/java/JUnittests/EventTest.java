package JUnittests;

import dao.DataAccessException;
import dao.DatabaseManager;
import dao.EventDao;
import model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventTest {
    private DatabaseManager db;
    private Event bestEvent;
    private Event wrongEvent;
    private Event wrongEvent2;
    private Event wrongEvent3;
    private EventDao eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        bestEvent = new Event("1235", "Jamesy", "Teague",
                12.234f, 25.542f, "Merica", "SouthJordan",
                "Earth Quake", 2012);
        wrongEvent = new Event("1234", "Jamesy", "234623",
                345.234f, 534.2f, "Merica", "SouthJordan",
                "Earth Quake", 2012);
        wrongEvent2 = new Event("1134", "Jamesy", "234623",
                345.234f, 534.2f, "Merica", "SouthJordan",
                "Earth Quake", 2000);
        wrongEvent3 = new Event("1213", "Jamesy", "234623",
                345.234f, 534.2f, "Merica", "SouthJordan",
                "Earth Quake", 23654);
        Connection conn = db.getConnection();
        eDao = new EventDao(conn);
        eDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeDB(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestEvent);
        assertThrows(DataAccessException.class, () -> eDao.insert(bestEvent));
    }

    @Test
    public void findPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.find(bestEvent.getEventID());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        eDao.insert(wrongEvent);
        //Event compareTest = eDao.find(bestEvent.getEventID());
        assertNull(eDao.find(bestEvent.getEventID()));


    }

    @Test
    public void clearTest() throws DataAccessException{
        eDao.insert(bestEvent);
        eDao.clear();
        assertNull(eDao.find(bestEvent.getEventID()));
    }

    @Test void findAllTest() throws DataAccessException{
        eDao.insert(bestEvent);
        eDao.insert(wrongEvent);
        eDao.insert(wrongEvent2);
        eDao.insert(wrongEvent3);
        Event[] eventArray = eDao.findAll("Jamesy");
        assertEquals(4, eventArray.length);

    }

}


