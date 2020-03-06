package ServiceTests;

import Results.ClearResult;
import Service.ClearService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClearServiceTest {
    ClearResult cResult = new ClearResult();
    ClearService cService = new ClearService();

    @Test
    public void clearPass() throws Exception{
        //Not really sure how to check a data array in JSON, so wil just check if it's true
        cResult = cService.clear();//authToken, eventID
        assertTrue(cResult.isSuccess());
    }
    //No need for fail since can't force an internal server error
}
