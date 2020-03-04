package Service;

import DAOs.Connect;
import DAOs.DataAccessException;
import Model.Person;
import Results.PersonResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersonService {
    private Connect db = new Connect();

    /**
     * The getFamily method will return all all family members,
     * given an auth token as a parameter from the user.
     * @param authToken the authToken from the user
     * @return The personResult object
     */
    public PersonResult getFamily(String authToken) {
        PersonResult newPerson = new PersonResult();
        //check the authToken and get the user from the authToken

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        String sql = "SELECT * FROM authToken WHERE authToken = ?";
        //get the user associated with the authToken
        ResultSet rs;
        String userName = null;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("username");
            }
        } catch (SQLException e) {
            newPerson = new PersonResult(false, "Internal Server error");
            e.printStackTrace();
        }

        ArrayList<Person> allPersons = new ArrayList();
        Person person;
        if(userName != null) {
            sql = "SELECT * FROM event WHERE associatedUsername = ?";
            //Get the events associated with the user - push to array lis
            try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                stmt.setString(1, userName);
                rs = stmt.executeQuery();
                while(rs.next()) {
                    person = new Person(rs.getString("associatedUsername"),
                            rs.getString("eventID"), rs.getString("firstName"),
                            rs.getString("lastName"), rs.getString("gender"),
                            rs.getString("fatherID"), rs.getString("motherID"),
                            rs.getString("spouseID"));
                    allPersons.add(person);
                }
            } catch (SQLException e) {
                newPerson = new PersonResult(false, "Internal Server error");
                e.printStackTrace();
            }
        } else {
            newPerson = new PersonResult(false, "Invalid auth token");
            return newPerson;
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        if (allPersons.size() > 0) {
            newPerson = new PersonResult(allPersons, true);
        }
        //get all family members and put into an array of Person
        return newPerson;
    }

    /**
     * The getPerson method will return a single person object with
     * a personID in a Person Result object.
     * @return the person result object
     */
    public PersonResult getPerson(String authToken, String personID) {
        PersonResult newPerson = new PersonResult();

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        //get the username from the person ID, make sure username is with authtoken
        String sql = "SELECT * FROM person WHERE personID = ?";
        ResultSet rs = null;
        String userName = null;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("associatedUsername");
            } else {
                newPerson = new PersonResult(false, "Invalid personID parameter");
            }
        } catch (SQLException e) {
            newPerson = new PersonResult(false, "Internal Server error");
            e.printStackTrace();
        }

        if(!(userName == null)) {
            //Check to find if the username and authtoken match
            boolean foundUser = false;
            sql = "SELECT * FROM authToken WHERE username = ? AND authToken = ?";
            try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.setString(1, authToken);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    foundUser = true;
                }
            } catch (SQLException e) {
                newPerson = new PersonResult(false, "Internal Server error");
                e.printStackTrace();
            }

            //Did find the user and the authtoken, so continue
            if(foundUser) {
                sql = "SELECT * FROM person WHERE personID = ?";
                try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                    stmt.setString(1, personID);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        newPerson = new PersonResult(rs.getString("associatedUsername"),
                                rs.getString("personID"), rs.getString("firstName"),
                                rs.getString("lastName"), rs.getString("gender"),
                                rs.getString("fatherID"), rs.getString("motherID"),
                                rs.getString("spouseID"), true);
                    } else {
                        newPerson = new PersonResult(false, "Invalid personID parameter");
                    }
                } catch (SQLException e) {
                    newPerson = new PersonResult(false, "Internal Server error");
                    e.printStackTrace();
                }
            } else {
                newPerson = new PersonResult(false, "Requested person does not belong to this user");
            }
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        return newPerson;
    }
}