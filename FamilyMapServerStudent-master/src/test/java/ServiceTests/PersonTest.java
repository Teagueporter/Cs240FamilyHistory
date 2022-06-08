package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.PersonResult;
import services.PersonService;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    DatabaseManager db = new DatabaseManager();
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());
        PersonDao PersonDao = new PersonDao(db.getConnection());
        AuthTokenDao authTokenDao = new AuthTokenDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        AuthToken authToken = new AuthToken("1234567890", "Ogtwigs");
        Person person = new Person("12345", "Ogtwigs", "Tanna", "Clegg", "f", "82938", "092394", "987654321");


        userDao.clear();
        PersonDao.clearAll();
        authTokenDao.clear();

        userDao.insert(user);
        PersonDao.insert(person);
        authTokenDao.insert(authToken);

        db.closeDB(true);

    }
    @Test
    public void Personpass(){
        PersonService PersonService = new PersonService();
        PersonResult PersonResult = PersonService.person("12345", "1234567890");
        assertEquals(true, PersonResult.isSuccess());
    }

    @Test
    public void Personfail(){
        PersonService PersonService = new PersonService();
        PersonResult PersonResult = PersonService.person("1256789", "1234890");
        assertEquals(false, PersonResult.isSuccess());
    }
}

