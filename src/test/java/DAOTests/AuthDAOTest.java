



package DAOTests;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import Model.AuthToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways

public class AuthDAOTest {
    private Connect db;
    private AuthToken token;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Connect();
        //and a new event with random data
        token = new AuthToken("123", "Xx_Faze_xX");
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void updatePass() throws Exception {
        AuthToken compareTest = null;
        //give it a new token and the username
        AuthToken newToken = new AuthToken("345", "Xx_Faze_xX");
        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);

            //insert the data
            uDao.insert(token);
            //update it
            uDao.update(newToken);
            ///Find the new token
            compareTest = uDao.find("345");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);
        assertEquals(newToken, compareTest);
    }
    @Test
    public void updateFail() throws Exception {
        AuthToken compareTest = null;

        //give it an invalid username
        AuthToken newToken = new AuthToken("345", "FAZE");

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);

            //insert the data
            uDao.insert(token);
            //update it
            uDao.update(newToken);
            ///Find the new token
            compareTest = uDao.find("345");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Make sure it did not find any data
        assertNull(compareTest);
    }
    @Test
    public void insertPass() throws Exception {
        AuthToken compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);

            uDao.insert(token);
            compareTest = uDao.find(token.getUserAuthToken());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNotNull(compareTest);

        assertEquals(token, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);

            uDao.insert(token);
            uDao.insert(token);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);

        AuthToken compareTest = token;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            compareTest = uDao.find(token.getUserAuthToken());
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
        AuthToken compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            uDao.insert(token);
            compareTest = uDao.find(token.getUserAuthToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
    }

    @Test
    public void findFail() throws Exception {
        AuthToken compareTest = null;

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            uDao.insert(token);
            compareTest = uDao.find("1234"); //A random ID
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    @Test
    public void testClear() throws Exception {
        AuthToken compareTest = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDao pDao = new AuthTokenDao(conn);
            pDao.insert(token); //insert data

            compareTest = pDao.find(token.getUserAuthToken());
            assertNotNull(compareTest); // Check to make sure there is data

            pDao.clear();  // Clear it
            compareTest = pDao.find(token.getUserAuthToken());
            assertNull(compareTest); // Check to make sure data is not there

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
