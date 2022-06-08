package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.LoginRequest;
import results.LoginResults;
import services.LoginService;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    DatabaseManager db = new DatabaseManager();
    LoginRequest loginRequest = new LoginRequest();
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        userDao.clear();

        userDao.insert(user);
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(user.getPassword());

        db.closeDB(true);

    }
    @Test
    public void Eventpass(){
        LoginService loginService = new LoginService();
        LoginResults loginResults = loginService.login(loginRequest);
        assertEquals(true, loginResults.isSuccess());
    }

    @Test
    public void Eventfail(){
        LoginService loginService = new LoginService();
        loginRequest.setPassword("notRightloser");
        LoginResults loginResults = loginService.login(loginRequest);
        assertEquals(false, loginResults.isSuccess());
    }
}

