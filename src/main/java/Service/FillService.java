package Service;

import Requests.FillRequest;
import Results.FillResult;

public class FillService {

    /**
     * This function will take in a FillRequest object, get the user's
     * family data to a number of generations back, and fill a FillResult object
     * with the data;
     * @param r the request object to draw data
     * @param userName the userName of the requesting user
     * @param generations how many generations back
     * @return the result object of the call
     */
    FillResult fill(FillRequest r, String userName, int generations) {
        FillResult filled = new FillResult();
        return filled;
    }

}
