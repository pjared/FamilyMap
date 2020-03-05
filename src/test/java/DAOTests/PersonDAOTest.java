
package DAOTests;

import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.PersonDao;
import Model.Person;
import Model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDAOTest {
    private Connect db;
    private Person bestPerson;

    @BeforeEach
    public void setUp() throws Exception {
        //here we can set up any classes or variables we will need for the rest of our tests
        //lets create a new database
        db = new Connect();
        //and a new event with random data
        bestPerson = new Person("GeorgeFOREMAN", "1984", "Jabba", "Hut", "U");
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
    public void makeFamilyFail() throws Exception {
        Person compareTest = null;
        //Not really sure how to test here

        //Given a user and numGenerations
        User bestUser = new User("Xx_Faze_xX", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1984");

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.makeFamTree(bestUser, -1); //should make nothing - bad generations parameter
            compareTest = eDao.find("1984"); //try to find him
            assertNull(compareTest); // make sure there is nothing

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }

    @Test
    public void makeFamilyPass() throws Exception {
        Person compareTest = null;

        //could give bad generations, and wrong user for fail

        //Given a user and numGenerations
        User bestUser = new User("Xx_Faze_xX", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1984");

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            eDao.makeFamTree(bestUser, 1); //should make him and parents

            assertEquals(eDao.getFamily("Xx_Faze_xX").size(), 3);
            compareTest = eDao.find("1984");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Make sure that compare has data in it, although there is no way to do equals testing
        assertNotNull(compareTest);
    }

    @Test
    public void deleteDataPass() throws Exception {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            //insert data into database
            eDao.insert(bestPerson);

            //Delete all data with that user
            eDao.deleteUserData("GeorgeFOREMAN");

            compareTest = eDao.find(bestPerson.getPersonID());


            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Data should be null
        assertNull(compareTest);
    }

    @Test
    public void deleteDataFail() throws Exception {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);

            //insert data into database
            eDao.insert(bestPerson);

            //Delete all data with that user
            eDao.deleteUserData("Georgeforearm");

            compareTest = eDao.find(bestPerson.getPersonID());


            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Data should be there
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    public void getFamilyFail() throws Exception {
        ArrayList<Person> compareTest = new ArrayList<>();

        Person anotherPerson = new Person("GeorgeFOREMAN", "1985", "Jabba", "Hut", "U");
        //Make an array list of people known
        ArrayList<Person> knownPersons = new ArrayList<>();
        knownPersons.add(bestPerson);
        knownPersons.add(anotherPerson);

        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);

            pDao.insert(bestPerson);
            pDao.insert(anotherPerson);

            //try to get the data with wrong username
            compareTest = pDao.getFamily("GeorgefOREMAN");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Making sure that the array list isn't equal
        assertNotEquals(knownPersons, compareTest);
        //compare test should equal an empty array
        ArrayList<Person> emptyPerson = new ArrayList<>();
        assertEquals(emptyPerson, compareTest);
    }

    @Test
    public void getFamilyPass() throws Exception {
        ArrayList<Person> compareTest = new ArrayList<>();

        Person anotherPerson = new Person("GeorgeFOREMAN", "1985", "Jabba", "Hut", "U");
        //Make an array list of people known
        ArrayList<Person> knownPersons = new ArrayList<>();
        knownPersons.add(bestPerson);
        knownPersons.add(anotherPerson);

        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);

            pDao.insert(bestPerson);
            pDao.insert(anotherPerson);

            compareTest = pDao.getFamily("GeorgeFOREMAN");
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
        assertEquals(knownPersons, compareTest);
    }

    @Test
    public void insertPass() throws Exception {
        //We want to make sure insert works
        //First lets create an Event that we'll set to null. We'll use this to make sure what we put
        //in the database is actually there.
        Person compareTest = null;

        try {
            //Let's get our connection and make a new DAO
            Connection conn = db.openConnection();
            PersonDao eDao = new PersonDao(conn);
            //While insert returns a bool we can't use that to verify that our function actually worked
            //only that it ran without causing an error
            eDao.insert(bestPerson);
            //So lets use a find method to get the event that we just put in back out
            compareTest = eDao.find(bestPerson.getPersonID());
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
        assertEquals(bestPerson, compareTest);

        //Data base locks after running the program once
        //ask TA what is going on, put breakpoint on line 53 and show
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
            PersonDao pDao = new PersonDao(conn);
            //if we call the method the first time it will insert it successfully
            pDao.insert(bestPerson);
            //but our sql table is set up so that "eventID" must be unique. So trying to insert it
            //again will cause the method to throw an exception
            pDao.insert(bestPerson);
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
        Person compareTest = bestPerson;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            //and then get something back from our find. If the event is not in the database we
            //should have just changed our compareTest to a null object
            compareTest = pDao.find(bestPerson.getPersonID());
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
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(bestPerson);
            compareTest = pDao.find(bestPerson.getPersonID()); //A random ID
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareTest);
    }

    @Test
    public void findFail() throws Exception {
        Person compareTest = null;

        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(bestPerson);
            compareTest = pDao.find("1234"); //A random ID
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareTest);
    }

    @Test
    public void testClear() throws Exception {
        Person compareTest = null;
        try {
            Connection conn = db.openConnection();
            PersonDao pDao = new PersonDao(conn);
            pDao.insert(bestPerson); //insert data

            compareTest = pDao.find(bestPerson.getPersonID());
            assertNotNull(compareTest); // Check to make sure there is data

            pDao.clear();  // Clear it
            compareTest = pDao.find(bestPerson.getPersonID());
            assertNull(compareTest); // Check to make sure data is not there

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
    }
}
