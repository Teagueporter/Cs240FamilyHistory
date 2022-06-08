package JUnittests;

import dao.DataAccessException;
import dao.DatabaseManager;
import dao.AuthTokenDao;
import model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class AuthTest {
    private DatabaseManager db;
    private AuthToken bestToken;
    private AuthToken wrongToken;
    private AuthTokenDao aDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        bestToken = new AuthToken("12345", "OgTwigs");
        wrongToken = new AuthToken("654654", "Jamesy");
        Connection conn = db.getConnection();
        aDao = new AuthTokenDao(conn);
        aDao.clear();
    }

    @AfterEach
    public void tearDown() {
        db.closeDB(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        aDao.insert(bestToken);
        AuthToken compareTest = aDao.find(bestToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(bestToken, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        aDao.insert(bestToken);
        assertThrows(DataAccessException.class, () -> aDao.insert(bestToken));
    }

    @Test
    public void findPass() throws DataAccessException {
        aDao.insert(bestToken);
        AuthToken compareTest = aDao.find(bestToken.getAuthtoken());
        assertNotNull(compareTest);
        assertEquals(bestToken, compareTest);
    }

    @Test
    public void findFail() throws DataAccessException {
        aDao.insert(wrongToken);
        //Person compareTest = aDao.find(bestToken.getAuthtoken());
        assertNull(aDao.find(bestToken.getAuthtoken()));

    }

    @Test
    public void clearTest() throws DataAccessException{
        aDao.insert(bestToken);
        aDao.clear();
        assertNull(aDao.find(bestToken.getAuthtoken()));
    }
    @Test
    public void findAuth() throws DataAccessException{
        aDao.insert(bestToken);
        boolean success = aDao.findToken("12345");
        assertEquals(true, success);
    }

    @Test
    public void findAuthFail() throws DataAccessException{
        aDao.insert(bestToken);
        boolean success = aDao.findToken("54321");
        assertEquals(false, success);
    }

}

