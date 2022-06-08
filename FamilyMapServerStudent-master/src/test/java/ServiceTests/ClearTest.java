package ServiceTests;

import dao.*;
import model.AuthToken;
import model.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import results.ClearResult;
import services.ClearService;


public class ClearTest {
    DatabaseManager db;
    PersonDao pDao;
    AuthTokenDao aDao;
    ClearResult clearResult;
    AuthToken bestToken;
    AuthToken wrongToken;
    Person bestPerson;
    Person wrongPerson;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        pDao = new PersonDao(db.getConnection());
        aDao = new AuthTokenDao(db.getConnection());
        pDao.clearAll();
        aDao.clear();
        db.closeDB(true);

        bestToken = new AuthToken("12345", "OgTwigs");
        wrongToken = new AuthToken("654654", "Jamesy");
        bestPerson = new Person("12345", "OgTwigs", "Teague",
                "Porter", "m", "Kenyon23", "Annie23",
                "Tanna23");
        wrongPerson = new Person("98765", "OgTwigs", "Murph",
                "Porter", "f", "Teague23", "Tanna23",
                "null");

    }

    @AfterEach
    public void tearDown() {

    }

    @Test
    public void pass() throws DataAccessException {
        //inserting people and auth tokens and then clearing
        DatabaseManager database = new DatabaseManager();
        database.openDB();
        PersonDao personDao = new PersonDao(database.getConnection());
        AuthTokenDao authTokenDao = new AuthTokenDao(database.getConnection());
        personDao.insert(bestPerson);
        personDao.insert(wrongPerson);
        authTokenDao.insert(bestToken);
        authTokenDao.insert(wrongToken);
        database.closeDB(true);

        ClearService clearService = new ClearService();
        clearResult = clearService.clear();

        assertNotNull(clearResult);
    }

    @Test
    public void pass2(){
        //clearing a already cleared database

        ClearService clearService = new ClearService();
        clearResult = clearService.clear();

        assertNotNull(clearResult);
    }
}
