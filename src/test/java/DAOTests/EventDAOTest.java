package DAOTests;

import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.EventDao;
import Model.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class EventDAOTest {
    private Connect db;
    private Event bestEvent;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Connect();
        //and a new event with random data
        bestEvent = new Event("Biking_123A", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
    }

    @AfterEach
    public void tearDown() throws Exception {
        //here we can get rid of anything from our tests we don't want to affect the rest of our program
        //lets clear the tables so that any data we entered for testing doesn't linger in our files
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void getEventsPass() throws Exception {
        ArrayList<Event> compareTest = new ArrayList<>();
        //Make another event to have multiple events, give a different ID.
        Event anotherEvent = new Event("Biking_123B", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        //make an arraylist containing the events we are going to add
        ArrayList<Event> knownEvents = new ArrayList<>();
        knownEvents.add(bestEvent);
        knownEvents.add(anotherEvent);

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //insert the data
            eDao.insert(bestEvent);
            eDao.insert(anotherEvent);

            //get the list of events with the associated username
            compareTest = eDao.getEvents("Gale");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Make sure that the data matches
        assertNotNull(compareTest);
        assertEquals(knownEvents, compareTest);
    }

    @Test
    public void getEventsFail() throws Exception {
        ArrayList<Event> compareTest = new ArrayList<>();
        //Make another event to have multiple events, give a different ID.
        Event anotherEvent = new Event("Biking_123B", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);

        //Make an empty array of events
        ArrayList<Event> knownEvents = new ArrayList<>();
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //insert the data
            eDao.insert(bestEvent);
            eDao.insert(anotherEvent);

            //get the list of events with an incorrect username
            compareTest = eDao.getEvents("gale");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Make sure that there is no data
        assertEquals(knownEvents, compareTest);
    }

    @Test
    public void deleteDataPass() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //insert the data
            eDao.insert(bestEvent);
            //Delete that specific data
            eDao.deleteUserData("Gale");
            // Try to find that data
            compareTest = eDao.find("Biking_123A");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Make sure if found nothing
        assertNull(compareTest);
    }

    @Test
    public void deleteDataFail() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //insert the data
            eDao.insert(bestEvent);
            //Delete with an incorrect username
            eDao.deleteUserData("gale");
            // Try to find that data
            compareTest = eDao.find("Biking_123A");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Make sure that the data is still there
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertPass() throws Exception {
        //We want to make sure insert works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Event compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.insert(bestEvent);
            //So lets use a find method to get the event that we just put in back out
            compareTest = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //First lets see if our find found anything at all. If it did then we know that if nothing
        //else something was put into our database, since we cleared it in the beginning
        assertNotNull(compareTest);
        //Now lets make sure that what we put in is exactly the same as what we got out. If this
        //passes then we know that our insert did put something in, and that it didn't change the
        //data in any way
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws Exception {
        //lets do this test again but this time lets try to make it fail

        // NOTE: The correct way to test for an exception in Junit 5 is to use an assertThrows
        // with a lambda function. However, lambda functions are beyond the scope of this class
        // so we are doing it another way.
        boolean didItWork = true;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //if we call the method the first time it will insert it successfully
            eDao.insert(bestEvent);
            //but our sql table is set up so that "eventID" must be unique. So trying to insert it
            //again will cause the method to throw an exception
            eDao.insert(bestEvent);
            db.closeConnection(true);
        } catch (DataAccessException e) {
            //If we catch an exception we will end up in here, where we can change our boolean to
            //false to show that our function failed to perform correctly
            db.closeConnection(false);
            didItWork = false;
        }
        //Check to make sure that we did in fact enter our catch statement
        assertFalse(didItWork);

        //Since we know our database encountered an error, both instances of insert should have been
        //rolled back. So for added security lets make one more quick check using our find function
        //to make sure that our event is not in the database
        //Set our compareTest to an actual event
        Event compareTest = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = eDao.find(bestEvent.getEventID());
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
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            compareTest = eDao.find(bestEvent.getEventID()); //A random ID
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
    }

    @Test
    public void findFail() throws Exception {
        Event compareTest = null;

        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent);
            compareTest = eDao.find("1234"); //A random ID
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    @Test
    public void testClear() throws Exception {
        Event compareTest = null;
        try {
            Connection conn = db.openConnection();
            EventDao eDao = new EventDao(conn);
            eDao.insert(bestEvent); //insert data

            compareTest = eDao.find(bestEvent.getEventID());
            assertNotNull(compareTest); // Check to make sure there is data

            eDao.clear();  // Clear it
            compareTest = eDao.find(bestEvent.getEventID());
            assertNull(compareTest); // Check to make sure data is not there

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
