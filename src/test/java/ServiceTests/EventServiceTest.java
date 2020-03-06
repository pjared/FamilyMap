package ServiceTests;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.EventDao;
import Model.AuthToken;
import Model.Event;
import Results.EventResult;
import Service.EventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class EventServiceTest {
    private EventResult eResult = new EventResult();
    private EventService eService = new EventService();

    private Connect db;
    private AuthToken newToken;
    private Event newEvent;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Connect();
        newToken = new AuthToken("123", "Gale");
        newEvent = new Event("Asteroids", "Gale", "123", 10, 10, "France", "Paris", "disaster", 200);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void getAllEventPass() throws Exception{
        //Not really sure how to check a data array in JSON, so wil just check if it's true
        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            EventDao eDao = new EventDao(conn);

            eDao.insert(newEvent);
            uDao.insert(newToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        eResult = eService.getAllEvents("123" );//authToken, eventID
        assertTrue(eResult.isSuccess());
    }

    @Test
    public void getAllEventFail() throws Exception{

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            EventDao eDao = new EventDao(conn);

            eDao.insert(newEvent);
            uDao.insert(newToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        eResult = eService.getAllEvents("456" );//authToken, eventID
        assertEquals("error: Invalid request data", eResult.getMessage());
    }

    @Test
    public void getEventPass() throws Exception{
        //Since the auth token given is random, we can instead just make sure that the parameters pass

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            EventDao eDao = new EventDao(conn);

            eDao.insert(newEvent);
            uDao.insert(newToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        eResult = eService.getEvent("123","Asteroids" );//authToken, eventID

        assertNotNull(eResult);
        //test some the result data
        assertEquals("Asteroids", eResult.getEventID());
        assertEquals("Gale", eResult.getAssociatedUsername());
        assertEquals("France", eResult.getCountry());
    }

    @Test
    public void getEventFail() throws Exception{
        //Since the auth token given is random, we can instead just make sure that the parameters pass

        //Insert an event and an authToken, then find the event
        AuthToken newToken = new AuthToken("123", "Gale");
        AuthToken anotherToken = new AuthToken("456", "Gabe");
        Event newEvent = new Event("Asteroids", "Gale", "123", 10, 10, "France", "Paris", "disaster", 200);
        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            EventDao eDao = new EventDao(conn);

            eDao.insert(newEvent);
            uDao.insert(newToken);
            uDao.insert(anotherToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Invalid Auth token check
        eResult = eService.getEvent("27374","Asteroids" );
        assertEquals("error: Invalid auth token", eResult.getMessage());

        //Invalid event ID check
        eResult = eService.getEvent("123","Birth" );
        assertEquals("error: Invalid eventID parameter", eResult.getMessage());

        //Request does not belong to user check
        eResult = eService.getEvent("456","Asteroids" );
        assertEquals("error:  Requested person does not belong to this user", eResult.getMessage());
    }

}
