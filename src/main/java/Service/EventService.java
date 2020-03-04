package Service;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.EventDao;
import Model.AuthToken;
import Model.Event;
import Results.EventResult;

import java.sql.Connection;

public class EventService {
    private Connect db = new Connect();

    /**
     * The getEvent method will get a single event when given an
     * eventID and the users authToken
     * @param authToken the user's authToke
     * @param eventID the eventID the user is requesting
     * @return the event result object
     */
    public EventResult getEvent(String authToken, String eventID) {
        EventResult newEvent = new EventResult();

        // make sure personID and authToken match, then return that person object
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newEvent = new EventResult(false, "Internal server error");
            e.printStackTrace();
        }

        AuthTokenDao aDao = new AuthTokenDao(connect);
        AuthToken token = null;
        //Use the DAOs to find the specific data we are looking for
        try {
            token = aDao.find(authToken);
        } catch (DataAccessException e) {
            newEvent = new EventResult(false, "Internal server error");
            e.printStackTrace();
        }
        if(token == null) {
            newEvent = new EventResult(false, "error: Invalid auth token");
        } else {
            EventDao eDao = new EventDao(connect);
            try {
                Event event = eDao.find(eventID);

                //Makes sure the event username matches with AuthToken username
                if(!event.getAssociatedUsername().equals(token.getUserName())) {
                    newEvent = new EventResult(false, "error:  Requested person does not belong to this user");
                } else {
                    //they match, good to make the new event Result
                    newEvent = new EventResult(event.getAssociatedUsername(), event.getEventID(),
                            event.getPersonID(), event.getLatitude(), event.getLongitude(),
                            event.getCountry(), event.getCity(), event.getEventType(),
                            event.getYear(), true);
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        //Close the database
        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newEvent = new EventResult(false, "Internal server error");
            e.printStackTrace();
        }

        return newEvent;
    }
    
    /**
     * This function will return all events for all family members
     * of the current user.
     * @return the event result object
     */
    public EventResult getAllEvents(String authToken) {
        EventResult newEvent = new EventResult();

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newEvent = new EventResult(false, "Internal server error");
            e.printStackTrace();
        }

        // Create DAO vars to find the data
        AuthTokenDao aDao = new AuthTokenDao(connect);
        AuthToken token = null;
        try {
            token = aDao.find(authToken);
        } catch (DataAccessException e) {
            newEvent = new EventResult(false, "Internal server error");
            e.printStackTrace();
        }
        // Check the token to see if it is valid
        if(token == null) {
            newEvent = new EventResult(false, "error: Invalid request data");
        } else {
            //token is valid, get the family for the user
            EventDao eDao = new EventDao(connect);
            newEvent = new EventResult(eDao.getEvents(token.getUserName()) ,true);
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return newEvent;
    }
}