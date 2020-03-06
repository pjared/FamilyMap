package ServiceTests;

import DAOs.Connect;
import Requests.RegisterRequest;
import Results.RegisterResult;
import Service.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RegisterServiceTest {

    private RegisterResult rResult = new RegisterResult();
    private RegisterRequest rRequest = null;
    RegisterService rService = new RegisterService();

    private Connect db;

    @BeforeEach
    public void setUp() throws Exception {
        db = new Connect();
        //String userName, String password, String email, String firstName, String lastName, String gender
        rRequest = new RegisterRequest("xXFazeXx", "123", "Dank@clan.com", "Faze", "Clan", "m");
    }

    @AfterEach
    public void tearDown() throws Exception {
        db.openConnection();
        db.clearTables();
        db.closeConnection(true);
    }

    @Test
    public void registerPass() throws Exception{
        //Since the auth token given is random, we can instead just make sure that the parameters pass
        rResult = rService.register(rRequest);
        assertNotNull(rResult);
        assertEquals("xXFazeXx", rResult.getUserName());
        assertTrue(rResult.isSuccess());
    }

    @Test
    public void registerFail() {
        rResult = rService.register(rRequest);
        //Call the register again
        rResult = rService.register(rRequest);

        RegisterResult compare = new RegisterResult(false,"Error: Username already taken by another user");
        //same result returned as when there is already that user in the DB

        assertEquals(rResult.isSuccess(), compare.isSuccess());
        assertEquals(rResult.getMessage(), compare.getMessage());
    }
}
