package DAOs;

import Model.Event;
import Model.Person;
import Model.User;
import Service.GenerateID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonDao {
    private final Connection conn;
    public PersonDao(Connection conn) {
        this.conn = conn;
    }
    private RandomDataGenerator generator = null;

    /**
     * Takes a Person object and will add the data to the person table
     * @param person the person object
     * @throws DataAccessException throws if no connection is made to the database
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO person(associatedUsername, personID, firstName, lastName, gender, fatherID, motherID, spouseID) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getAssociatedUsername());
            stmt.setString(2, person.getPersonID());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Makes a new person object from the database with given person ID
     * @param personID the eventID to find in the database
     * @return the event Object
     * @throws DataAccessException thrown now so not returned to the server
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM person WHERE personID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("associatedUsername"), rs.getString("personID"),
                        rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * This function will clear the Person table.
     * @throws DataAccessException thrown when no establishment can be made with the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM person";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to clear table");
        }
    }

    /**
     * This will generate the data when the user requests to see family data
     *  to a number of generations back by accessing the data base.
     * @param numGenerations the amount of generations the user wants to see
     */
    public void makeFamTree(User baseUser, int numGenerations) throws DataAccessException {
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
            createBirth(person);
            generator.subtractYears(40);

            createDeath(person);
            generator.addYears(40);
            insert(person);
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
        insert(person);
        createBirth(person);
        generator.subtractYears(40);
        createDeath(person);
        generator.addYears(40);


        //makes the marriage for the parents of the current person
        MakeMarriage(mother.getPersonID(), father.getPersonID(), person.getAssociatedUsername());
        generator.subtractYears(30);

        makeRecursiveTree(father, numGenerations, generationCount);
        makeRecursiveTree(mother, numGenerations, generationCount);
        generator.addYears(30);
        --generationCount;
    }

    private void MakeMarriage(String motherID, String fatherID, String userName) {
        //give event the connection from here, then add
        EventDao eDao = new EventDao(conn);
        float latitude = (float) 40.2338;
        float longitude = (float) 5.5;
        String getCountry = "Paraguay";
        String getCity = "Caracas";
        int year = generator.getYear() - 5;

        try {
            eDao.insert(new Event(GenerateID.genID(), userName, fatherID, latitude, longitude, getCountry,
                    getCity,  "marriage", year));
            eDao.insert(new Event(GenerateID.genID(), userName, motherID, latitude, longitude, getCountry,
                    getCity, "marriage", year));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    //THESE CAN BE MOVED TO CREATE EVENTS
    //EVENT CONSTRUCTOR: String eventID, String associatedUser, String personID, float latitude, float longitude, String country, String city, String eventType, int year
    private void createDeath(Person person) {
        String eventID = GenerateID.genID();
        EventDao eDao = new EventDao(conn);
        float latitude = (float) 40.2338;
        float longitude = (float) 5.5;
        String getCountry = "Paraguay";
        String getCity = "Caracas";
        int year = generator.getYear();
        try {
            eDao.insert(new Event(eventID, person.getAssociatedUsername(), person.getPersonID(), latitude, longitude, getCountry,
                    getCity,  "birth", year));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    //EVENT CONSTRUCTOR: String eventID, String associatedUser, String personID, float latitude, float longitude, String country, String city, String eventType, int year
    private void createBirth(Person person) {
        String eventID = GenerateID.genID();
        EventDao eDao = new EventDao(conn);
        float latitude = (float) 40.2338;
        float longitude = (float) 5.5;
        String getCountry = "Paraguay";
        String getCity = "Caracas";
        int year = generator.getYear();
        try {
            eDao.insert(new Event(eventID, person.getAssociatedUsername(), person.getPersonID(), latitude, longitude, getCountry,
                    getCity,  "death", year));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Person> getFamily(String userName) {
        ArrayList<Person> familyTree = new ArrayList<>();
        Person person;

        String sql = "SELECT * FROM person WHERE associatedUsername = ?";
        ResultSet rs;
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while(rs.next()) {
                person = new Person(rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getString("firstName"),
                        rs.getString("lastName"), rs.getString("gender"),
                        rs.getString("fatherID"), rs.getString("motherID"),
                        rs.getString("spouseID"));
                familyTree.add(person);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return familyTree;
    }

    public void deleteUserData(String userName) {
        String sql = "DELETE FROM person WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
