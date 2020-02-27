package Service;

import DAOs.Connect;
import DAOs.DataAccessException;
import Results.ClearResult;

public class ClearService {

    /**
     * This function will clear the entire database
     * @return the result object returned
     */
    public ClearResult clear() {
        ClearResult cleared = new ClearResult();
        //call the DAO clears
        Connect conn = new Connect();
        try {
            conn.clearTables();
        } catch (DataAccessException e) {
            //Didn't work. Should return the fail method
            cleared.setMessage("Database could not be accessed. Did not delete data");
            cleared.setSuccess(false);
            e.printStackTrace();
        }
        //Wasn't there something said about how the data access exception should be thrown earlier?

        cleared.setMessage("Database cleared");
        cleared.setSuccess(true);
        return cleared;
    }
}
