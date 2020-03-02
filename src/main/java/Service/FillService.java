package Service;

import Requests.FillRequest;
import Results.FillResult;

public class FillService {
    /*
    Success Response Body:{“message”: “Successfully added X persons and Y events to the database.”“success”:”true”}
    Error Response Body:{“message”: “Description of the error”“success”:”false”}
     */

    /**
     * This function will take in a FillRequest object, get the user's
     * family data to a number of generations back, and fill a FillResult object
     * with the data;
     * @param r the request object to draw data
     * @return the result object of the call
     */
    FillResult fill(FillRequest r) {
        FillResult filled = new FillResult();
        filled.setMessage("Successfully added ");
        filled.setSuccess(true);
        return filled;
    }

}
