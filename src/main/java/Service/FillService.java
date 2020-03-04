package Service;

import DAOs.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.FillRequest;
import Results.FillResult;

import java.sql.Connection;
import java.util.ArrayList;

public class FillService {
    private Connect db = new Connect();

    /**
     * This function will take in a FillRequest object, get the user's
     * family data to a number of generations back, and fill a FillResult object
     * with the data;
     * @param r the request object to draw data
     * @return the result object of the call
     */
    public FillResult fill(FillRequest r) {
        FillResult filled = new FillResult();

        //check to see if generations is a valid number
        int generations = r.getGenerations();
        if (generations <= 0) {
            filled = new FillResult("error: Invalid generations parameter", false);
            return filled;
        }

        //check userName to see if in database
        if(!findUser(r.getUserName())) {
            filled = new FillResult("error: Invalid username parameter", false);
            return filled;
        }

        //Everthing is fine, now we have to delete all the users relevant data in the database for person and events
        deleteData(r.getUserName());

        //Data deleted, time to make all of the data
        ArrayList<Person> persons = null;
        try {
            persons = makeFamilyTree(r.getUserName(), r.getGenerations());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        //Return a peron array, making events with personID and asssociatedUsername
        ArrayList<Event> events = null;
        for(Person person: persons) {
            events = makeEvents(person.getAssociatedUsername(), person.getPersonID());
        }

        //know that there should be events and persons added
        filled.setMessage("Successfully added " + persons.size() + " persons and "
                + events.size() + " events to the database.");
        filled.setSuccess(true);
        return filled;
    }

    public ArrayList<Person> makeFamilyTree(String username, int generations) throws DataAccessException {
        ArrayList<Person> persons = new ArrayList<>();

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        PersonDao pDao = new PersonDao(connect);
        persons = pDao.makeFamTree(username, generations);

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        //set father and mother ID throughout
        return persons;
    }

    public ArrayList<Event> makeEvents(String userName, String personID) {
        ArrayList<Event> events = new ArrayList<>();

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        EventDao pDao = new EventDao(connect);
        //Might only need that personID
        events = pDao.createEvents(personID);

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        //Birth, Death, and marriage event

        return events;
    }

    public void deleteData(String username) {
        //go into events and persons table and delete all the data
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        EventDao eDao = new EventDao(connect);
        PersonDao pDao = new PersonDao(connect);
        eDao.deleteUserData(username);
        pDao.deleteUserData(username);

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public boolean findUser(String userName) {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        UserDao uDao = new UserDao(connect);
        User newUser = null;
        try {
            newUser = uDao.find(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        if(newUser == null) {
            return false;
        }
        return true;
    }

}
