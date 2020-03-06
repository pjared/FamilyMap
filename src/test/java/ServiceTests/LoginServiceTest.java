package ServiceTests;

import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.UserDao;
import Model.User;
import Requests.LoginRequest;
import Results.LoginResult;
import Service.LoginService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class LoginServiceTest {
    private Connect db;
    private User newUser;
    LoginRequest loginRequest = new LoginRequest();
    LoginResult lResult = new LoginResult();
    LoginService lService = new LoginService();

    @BeforeEach
    public void setUp() throws Exception {
        db = new Connect();
        newUser = new User("Xx_Faze_xX", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1984");
        //String userName, String password, String email, String firstName, String lastName, String gender

    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void loginPass() throws Exception{
        //Not really sure how to check a data array in JSON, so wil just check if it's true
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);

            uDao.insert(newUser);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        loginRequest = new LoginRequest("Xx_Faze_xX", "password123");
        lResult = lService.login(loginRequest);
        //make sure userName is equal and returns true
        assertEquals("Xx_Faze_xX", lResult.getUserName());
        assertTrue(lResult.isSuccess());
    }

    @Test
    public void LoginFail() throws Exception{
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);

            uDao.insert(newUser);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        loginRequest = new LoginRequest("Xx_Faze_xX", "badPassword");
        lResult = lService.login(loginRequest);

        // Request property missing or has invalid value,
        assertEquals("Error: Request property missing or has invalid value", lResult.getMessage());
    }
}
