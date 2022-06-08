package JUnittests;

import dao.DataAccessException;
import dao.DatabaseManager;
import dao.UserDao;
import model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserTest {
    private DatabaseManager db;
    private User bestUser;
    private User wrongUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        bestUser = new User("OgTwigs", "passwrod", "Teagueporter5@gmail.com",
                "Teague", "Porter", "m", "Teague23");
        wrongUser = new User("TannaBanana", "imanerd5", "tanna@hotmail.com",
                "Tanna", "Clegg", "f", "Tanna23");
        Connection conn = db.getConnection();
        uDao = new UserDao(conn);
        uDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeDB(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        uDao.insert(bestUser);
        User compareTest = uDao.find(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        uDao.insert(bestUser);
        assertThrows(DataAccessException.class, () -> uDao.insert(bestUser));
    }

    @Test
    public void findPass() throws DataAccessException {
        uDao.insert(bestUser);
        User compareTest = uDao.find(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        uDao.insert(wrongUser);
        //Person compareTest = pDao.find(bestPerson.getPersonID());
        assertNull(uDao.find(bestUser.getUsername()));


    }

    @Test
    public void clearTest() throws DataAccessException{
        uDao.insert(bestUser);
        uDao.clear();
        assertNull(uDao.find(bestUser.getUsername()));
    }

}


