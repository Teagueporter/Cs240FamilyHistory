package ServiceTests;

import dao.*;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import results.FillResult;
import services.FillService;

import static org.junit.jupiter.api.Assertions.*;

public class TestFill {
    DatabaseManager db = new DatabaseManager();
    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new DatabaseManager();
        db.openDB();

        UserDao userDao = new UserDao(db.getConnection());

        User user = new User("Ogtwigs", "12345", "Teagueporter5@gmail.com", "Teague", "porter", "m", "987654321");
        userDao.clear();

        userDao.insert(user);

        db.closeDB(true);

    }
    @Test
    public void Eventpass(){
        FillService fillService = new FillService();
        FillResult fillResult = fillService.fill("Ogtwigs" ,5);
        assertEquals(true, fillResult.isSuccess());
    }

    @Test
    public void Eventfail(){
        FillService fillService = new FillService();
        FillResult fillResult = fillService.fill("Owigs" , -1);
        assertEquals(false, fillResult.isSuccess());
    }
}
