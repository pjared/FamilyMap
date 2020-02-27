package DAOs;

import Model.Person;

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

    /**
     * Takes a Person object and will add the data to the person table
     * @param person the person object
     * @throws DataAccessException throws if no connection is made to the database
     */
    public void insert(Person person) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement


        String sql = "INSERT INTO person(associatedUsername, personID, firstName, lastName, gender, fatherID, motherID, spouseID) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getUserName());
            stmt.setString(2, person.getPersonID());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender()); //not sure on this one
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
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to clear table");
        }
    }

    /**
     * This will generate the data when the user requests to see family data
     *  to a number of generations back by accessing the data base.
     * @param numGenerations the amount of generations the user wants to see
     * @return an ArrayList of person objects of the data requested
     */
    public ArrayList<Person> makeFamTree(int numGenerations) {
        ArrayList<Person> familyTree = new ArrayList<>();
        return familyTree;
    }
}
