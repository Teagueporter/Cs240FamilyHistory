package JUnittests;

import dao.DataAccessException;
import dao.DatabaseManager;
import dao.PersonDao;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonTest {
    private DatabaseManager db;
    private Person bestPerson;
    private Person wrongPerson;
    private PersonDao pDao;
    private Person wrongPerson2;
    private Person wrongPerson3;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        bestPerson = new Person("12345", "OgTwigs", "Teague",
                "Porter", "m", "Kenyon23", "Annie23",
                "Tanna23");
        wrongPerson = new Person("98765", "OgTwigs", "Murph",
                "Porter", "f", "Teague23", "Tanna23",
                "null");
        wrongPerson2 = new Person("98775", "OgTwigs", "Murph",
                "Porter", "f", "Teague23", "Tanna23",
                "null");
        wrongPerson3 = new Person("965", "OgTwigs", "Murph",
                "Porter", "f", "Teague23", "Tanna23",
                "null");
        Connection conn = db.getConnection();
        pDao = new PersonDao(conn);
        pDao.clearAll();
    }

    @AfterEach
    public void tearDown() {
        db.closeDB(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        pDao.insert(bestPerson);
        Person compareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        pDao.insert(bestPerson);
        assertThrows(DataAccessException.class, () -> pDao.insert(bestPerson));
    }

    @Test
    public void findPass() throws DataAccessException {
        pDao.insert(bestPerson);
        Person compareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        pDao.insert(wrongPerson);
        //Person compareTest = pDao.find(bestPerson.getPersonID());
        assertNull(pDao.find(bestPerson.getPersonID()));


    }

    @Test
    public void clearTest() throws DataAccessException{
        pDao.insert(bestPerson);
        pDao.clearAll();
        assertNull(pDao.find(bestPerson.getPersonID()));
    }

    @Test void findAllTest() throws DataAccessException{
        pDao.insert(bestPerson);
        pDao.insert(wrongPerson);
        pDao.insert(wrongPerson2);
        pDao.insert(wrongPerson3);
        Person[] personArray = pDao.findAll("OgTwigs");
        assertEquals(4, personArray.length);

    }

}

