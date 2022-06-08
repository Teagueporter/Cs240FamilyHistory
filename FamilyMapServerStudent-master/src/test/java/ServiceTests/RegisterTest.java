package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import request.RegRequest;
import results.RegisterResults;
import services.RegisterService;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterTest {
    RegRequest request = new RegRequest();
    DatabaseManager db = new DatabaseManager();

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        userDao.clear();
        db.closeDB(true);

        request.setEmail(user.getEmail());
        request.setPassword(user.getPassword());
        request.setUsername(user.getUsername());
        request.setGender(user.getGender());
        request.setFirstName(user.getFirstName());
        request.setLastName(user.getLastName());

    }
    @Test
    public void Eventpass() throws DataAccessException {
        RegisterService registerService = new RegisterService();
        RegisterResults registerResults = registerService.register(request);

        assertEquals(true, registerResults.isSuccess());
    }

    @Test
    public void EventPass2() throws DataAccessException{
        RegisterService registerService = new RegisterService();
        request.setUsername("newUserName");
        RegisterResults registerResults = registerService.register(request);
        assertEquals(true, registerResults.isSuccess());
    }
}

