package ServiceTests;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.PersonDao;
import Model.AuthToken;
import Model.Person;
import Results.PersonResult;
import Service.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class PersonServiceTest {
    private PersonResult pResult = new PersonResult();
    private PersonService pService = new PersonService();

    private Connect db;
    private AuthToken newToken;
    private Person bestPerson;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Connect();
        newToken = new AuthToken("123", "GeorgeFOREMAN");
        bestPerson = new Person("GeorgeFOREMAN", "1984", "Jabba", "Hut", "U");
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void getAllPersonPass() throws Exception{
        //Not really sure how to check a data array in JSON, so wil just check if it's true
        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            uDao.insert(newToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        pResult = pService.getFamily("123" );//authToken, eventID
        assertTrue(pResult.isSuccess());
    }

    @Test
    public void getAllPersonFail() throws Exception{

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            uDao.insert(newToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        //Bad authToken check
        pResult = pService.getFamily("456" );//authToken, eventID
        assertEquals("error: Invalid request data", pResult.getMessage());
    }

    @Test
    public void getPersonPass() throws Exception{
        //Since the auth token given is random, we can instead just make sure that the parameters pass

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(bestPerson);
            uDao.insert(newToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        pResult = pService.getPerson("123","1984");//authToken, eventID

        assertNotNull(pResult);

        //test some the result data
        assertEquals("1984", pResult.getPersonID());
        assertEquals("GeorgeFOREMAN", pResult.getAssociatedUsername());
        assertTrue(pResult.isSuccess());
    }

    @Test
    public void getPersonFail() throws Exception{
        //Since the auth token given is random, we can instead just make sure that the parameters pass

        //Insert an event and an authToken, then find the event
        AuthToken newToken = new AuthToken("123", "Gale");
        AuthToken anotherToken = new AuthToken("456", "Gabe");
        Person personFail = new Person("GeorgeFOREMAN", "1984", "Jabba", "Hut", "U");

        try {
            Connection conn = db.openConnection();
            AuthTokenDao uDao = new AuthTokenDao(conn);
            PersonDao eDao = new PersonDao(conn);

            eDao.insert(personFail);
            uDao.insert(newToken);
            uDao.insert(anotherToken);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        //Invalid Auth token check
        pResult = pService.getPerson("27374","1984" );
        assertEquals("error: Invalid auth token", pResult.getMessage());

        //Invalid person ID check
        pResult = pService.getPerson("123","wrongID" );
        assertEquals("error:  Invalid personID parameter", pResult.getMessage());

        //Request does not belong to user check
        pResult = pService.getPerson("456","1984" );
        assertEquals("error:  Requested person does not belong to this user", pResult.getMessage());
    }

}
