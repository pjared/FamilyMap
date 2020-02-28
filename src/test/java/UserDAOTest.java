
package test;

import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.UserDao;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDAOTest {
    private Connect db;
    private User bestUser;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Connect();
        //and a new event with random data
        bestUser = new User("Xx_Faze_xX", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1984");
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void insertPass() throws Exception {
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            uDao.insert(bestUser);
            compareTest = uDao.find(bestUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);

        assertEquals(bestUser, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);

            uDao.insert(bestUser);
            uDao.insert(bestUser);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        User compareTest = bestUser;
        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            compareTest = uDao.find(bestUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Now make sure that compareTest is indeed null
        assertNull(compareTest);
    }

    @Test
    public void findPass() throws Exception {
        //add the object
        //search for it
        //Not null, found it
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            uDao.insert(bestUser);
            compareTest = uDao.find(bestUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
    }

    @Test
    public void findFail() throws Exception {
        User compareTest = null;

        try {
            Connection conn = db.openConnection();
            UserDao uDao = new UserDao(conn);
            uDao.insert(bestUser);
            compareTest = uDao.find("1234"); //A random ID
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    @Test
    public void testClear() throws Exception {
        User compareTest = null;
        try {
            Connection conn = db.openConnection();
            UserDao pDao = new UserDao(conn);
            pDao.insert(bestUser); //insert data

            compareTest = pDao.find(bestUser.getUserName());
            assertNotNull(compareTest); // Check to make sure there is data

            pDao.clear();  // Clear it
            compareTest = pDao.find(bestUser.getUserName());
            assertNull(compareTest); // Check to make sure data is not there

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
