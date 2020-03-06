package ServiceTests;

import DAOs.Connect;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.LoadResult;
import Service.LoadService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class LoadServiceTest {
    ArrayList<User> users = new ArrayList<>();
    ArrayList<Person> persons = new ArrayList<>();
    ArrayList<Event> events = new ArrayList<>();
    private Connect db;
    private User bestUser;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Connect();
        bestUser = new User("Xx_Faze_xX", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1984");
        User anotherUser = new User("Faze", "password123", "faze@tryhard.com",
                "george", "foreman", "m", "1985");
        Person bestPerson = new Person("GeorgeFOREMAN", "1984", "Jabba", "Hut", "U");
        Event anotherEvent = new Event("Biking_123B", "Gale", "Gale123A",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        users.add(bestUser);
        users.add(anotherUser);
        persons.add(bestPerson);
        events.add(anotherEvent);
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    private LoadService lService = new LoadService();
    private LoadRequest lRequest;
    private LoadResult lResult = new LoadResult();

    @Test
    public void loadPass() throws Exception {
        lRequest = new LoadRequest(users, persons, events);
        lResult = lService.load(lRequest);
        assertEquals("Successfully added 2 users, 1 persons, and 1 events to the database.", lResult.getMessage());
        assertTrue(lResult.isSuccess());
    }

    @Test
    public void loadFail() throws Exception {
        //add a bad data member
        persons.add(new Person("GeorgeFOREMAN", "1984", "Jabba", "Hut", "U"));

        lRequest = new LoadRequest(users, persons, events);
        lResult = lService.load(lRequest);

        assertEquals("Invalid Request Data", lResult.getMessage());
        assertFalse(lResult.isSuccess());
    }
}
