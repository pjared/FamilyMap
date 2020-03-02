package Service;

import DAOs.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.LoadResult;

import java.sql.Connection;
import java.util.ArrayList;

public class LoadService {
    private Connect db = new Connect();

    /**
     * This function will take a load request object to fill
     * the server's databse and then return a Load Result object
     * of the function call.
     * @return the load result object
     */
    public LoadResult load(LoadRequest r) throws DataAccessException {
        //Errors: Invalid request data (missing values, invalid values, etc.), Internal server error
        boolean failed = false;
        LoadResult newLoad = new LoadResult();
        Connect conn = new Connect();
        try {
            conn.openConnection();
            conn.clearTables(); //clear the tables
            conn.closeConnection(true);

            ArrayList<User> users= r.getUsers();
            ArrayList<Person> persons= r.getPersons();
            ArrayList<Event> events= r.getEvents();

            Connection connect = db.openConnection();
            UserDao uDao = new UserDao(connect);
            PersonDao pDao = new PersonDao(connect);
            EventDao eDao = new EventDao(connect);

            try {
                for(User user:users) {
                    uDao.insert(user);
                }
                for(Person person:persons) {
                    pDao.insert(person);
                }
                for(Event event:events) {
                    eDao.insert(event);
                }
            } catch (DataAccessException e) {
                db.closeConnection(false);
                failed = true;
                newLoad.setMessage("Invalid Request Data");
                newLoad.setSuccess(false);
                e.printStackTrace();
            }

            if(failed == false) {
                db.closeConnection(true);
                newLoad.setMessage("Successfully added ");
                newLoad.setSuccess(true);
            }
        } catch (DataAccessException e) {
            db.closeConnection(false);
            conn.closeConnection(false);
            newLoad.setMessage("Database could not be accessed. Internal server error");
            newLoad.setSuccess(false);
            e.printStackTrace();
        }

        return newLoad;
    }
}