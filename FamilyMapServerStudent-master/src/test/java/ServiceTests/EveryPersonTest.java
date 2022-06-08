package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.EveryPersonResult;
import services.EveryPersonService;

import static org.junit.jupiter.api.Assertions.*;

public class EveryPersonTest {
    DatabaseManager db = new DatabaseManager();
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());
        PersonDao personDao = new PersonDao(db.getConnection());
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        Person person = new Person("12345", "Ogtwigs", "Tanna", "Clegg", "f", "82938", "092394", "987654321");
        Person person2 = new Person("1245", "Ogtwigs", "David", "jones", "m", "2346", "0347", null);
        Person person3 = new Person("2345", "Ogtwigs", "Julian", "Mackelbee", "f", "1236", "3246", null);
        AuthToken authToken = new AuthToken("1234567890", "Ogtwigs");

        userDao.clear();
        personDao.clearAll();
        authTokenDao.clear();

        userDao.insert(user);
        personDao.insert(person);
        personDao.insert(person2);
        personDao.insert(person3);
        authTokenDao.insert(authToken);

        db.closeDB(true);

    }
    @Test
    public void EveryPersonpass(){
        EveryPersonService EveryPersonService = new EveryPersonService();
        EveryPersonResult EveryPersonResult = EveryPersonService.ePerson("1234567890");
        assertEquals(true, EveryPersonResult.isSuccess());
    }

    @Test
    public void EveryPersonfail(){
        EveryPersonService EveryPersonService = new EveryPersonService();
        EveryPersonResult EveryPersonResult = EveryPersonService.ePerson("17890");
        assertEquals(false, EveryPersonResult.isSuccess());
    }
}

