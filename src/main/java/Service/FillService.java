package Service;

import DAOs.*;
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
        if (generations < 0) {
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
            makeFamilyTree(r.getUserName(), r.getGenerations());
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        //know that there should be events and persons added
        filled.setMessage("Successfully added " + getNumUsers(r.getUserName()) + " persons and "
                + getNumEvents(r.getUserName()) + " events to the database.");
        //Leaving this here, because we need to get the events size with the username
        //I also probably dont even need to make the array list. Just can do the call for persons and events and get the
        //numbers through that
        filled.setSuccess(true);
        return filled;
    }

    UserDao uDao;
    PersonDao pDao;
    EventDao eDao;
    private RandomDataGenerator generator = null;
    private void makeFamilyTree(String username, int generations) throws DataAccessException {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        uDao = new UserDao(connect);
        pDao = new PersonDao(connect);
        eDao = new EventDao(connect);
        makeFam(generations, uDao.find(username));

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    private void makeFam(int numGenerations, User baseUser) throws DataAccessException {
        if(numGenerations < 0) {
            return;
        }
        Person person = new Person(baseUser.getUserName(), baseUser.getPersonID(), baseUser.getFirstName(),
                baseUser.getLastName(), baseUser.getGender());
        int generationCount = 0;
        generator = new RandomDataGenerator();
        makeRecursiveTree(person, numGenerations, generationCount);
    }

    private void makeRecursiveTree(Person person, int numGenerations, int generationCount) throws DataAccessException {
        //We are at the final generation, all we need to do is create the person but not their parents
        if(generationCount == numGenerations) {
            eDao.createBirth(person, generator.getYear());
            generator.subtractYears(40);
            eDao.createDeath(person, generator.getYear());
            generator.addYears(40);
            pDao.insert(person);
            return;
        }
        ++generationCount;

        //Need to make this randomly Generated
        String fatherName = "John";
        String lastName = "Smith";
        String motherName = "Mary";
        Person father = new Person(person.getAssociatedUsername(), GenerateID.genID(),
                fatherName, lastName, "m"); // call for mother, call for father IDs

        Person mother = new Person(person.getAssociatedUsername(), GenerateID.genID(),
                motherName, lastName, "f"); // call for mother, call for father IDs
        //set the parents spouse ID's
        mother.setSpouseID(father.getPersonID());
        father.setSpouseID(mother.getPersonID());
        //set the users parents ID's
        person.setFatherID(father.getPersonID());
        person.setMotherID(mother.getPersonID());

        //Insert into the database, then create events for this person
        pDao.insert(person);
        eDao.createBirth(person, generator.getYear());
        generator.subtractYears(40);
        eDao.createDeath(person, generator.getYear());
        generator.addYears(40);

        //makes the marriage for the parents of the current person
        eDao.MakeMarriage(mother.getPersonID(), father.getPersonID(), person.getAssociatedUsername(), generator.getYear());
        generator.subtractYears(30);

        makeRecursiveTree(father, numGenerations, generationCount);
        makeRecursiveTree(mother, numGenerations, generationCount);
        generator.addYears(30);
        --generationCount;
    }

    private int getNumUsers(String username) {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        PersonDao pDao = new PersonDao(connect);
        int numPersons = pDao.getFamily(username).size();

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return numPersons;
    }

    private int getNumEvents(String username) {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        EventDao eDao = new EventDao(connect);
        int numEvents = eDao.getEvents(username).size();

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return numEvents;
    }

    private void deleteData(String username) {
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

    private boolean findUser(String userName) {
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
