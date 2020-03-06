package ServiceTests;

import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.UserDao;
import Model.User;
import Requests.FillRequest;
import Results.FillResult;
import Service.FillService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    FillService fService = new FillService();
    FillRequest fRequest = null;
    FillResult fResult = new FillResult();

    //need to add a user to database first
    private Connect db;
    @BeforeEach
    public void setUp() throws Exception {
        db = new Connect();
        User newUser = new User("Xx_Faze_xX", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1984");
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            uDao.insert(newUser);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    // Invalid username or generations parameter, Internal server erro
    @Test
    public void fillPass() throws Exception{
        fRequest = new FillRequest("Xx_Faze_xX", 4);
        fResult = fService.fill(fRequest);
        assertEquals("Successfully added 31 persons and 92 events to the database.", fResult.getMessage());
        assertTrue(fResult.isSuccess());
    }

    @Test
    public void fillFail() throws Exception{
        //Bad generations check
        fRequest = new FillRequest("faze", -1);
        fResult = fService.fill(fRequest);
        assertEquals("error: Invalid generations parameter", fResult.getMessage());
        assertFalse(fResult.isSuccess());

        fRequest = new FillRequest("FAZEE", 2);
        fResult = fService.fill(fRequest);
        assertEquals("error: Invalid username parameter", fResult.getMessage());
        assertFalse(fResult.isSuccess());
    }
}
