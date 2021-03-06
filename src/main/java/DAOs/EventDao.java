package DAOs;

import Model.City;
import Model.Event;
import Model.Person;
import Service.GenerateID;
import Service.RandomDataGenerator;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

public class EventDao {
    private final Connection conn;

    public EventDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * The insert method takes an event object to be added to the database
     * @param event the event object
     * @throws DataAccessException thrown when no connection can be made to the database
     */
    public void insert(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO event(eventID, associatedUsername, personID, latitude, longitude, " +
                "country, city, eventType, year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Makes a new event object from the database with given event ID
     * @param eventID the eventID to find in the database
     * @return the event Object
     * @throws DataAccessException thrown now so not returned to the server
     */
    public Event find(String eventID) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM event WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                //Round up to 4 decimals
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"),
                        rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"), rs.getString("eventType"),
                        rs.getInt("year"));
                return event;
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
     * the clear method will clear the event table from the database
     * @throws DataAccessException thrown when no connection is established
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM event";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to clear table");
        }
    }

    /**
     * This method will get a list of events for the specified user
     * @Param the user's authToken
     * @return the list of events
     */
    public ArrayList<Event> getEvents(String userName) {
        ArrayList<Event> events = new ArrayList<>();
        ResultSet rs;
        Event event;

        String sql = "SELECT * FROM event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            while(rs.next()) {
                event = new Event(rs.getString("eventID"),
                        rs.getString("associatedUsername"), rs.getString("personID"),
                        rs.getFloat("latitude"), rs.getFloat("longitude"),
                        rs.getString("country"), rs.getString("city"),
                        rs.getString("eventType"),rs.getInt("year"));
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

    public void deleteUserData(String userName) {
        String sql = "DELETE FROM event WHERE associatedUsername = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.executeUpdate(); // this might need to be executeUpdate()
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    RandomDataGenerator generator = new RandomDataGenerator();
    City newCity = null;
    public void rollCity() {
        if(newCity == null) {
            try {
                newCity = generator.getCity();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Random rand = new Random();
        //1 in 4 chance that they will move somewhere new
        if(rand.nextInt(4) == 0) {
            try {
                newCity = generator.getCity();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    //It's hard to test these because they're void functions, so I leave it to fill
    public void MakeMarriage(String motherID, String fatherID, String userName, int year) {
        //give event the connection from here, then add
        EventDao eDao = new EventDao(conn);
        rollCity();

            try {
            eDao.insert(new Event(GenerateID.genID(), userName, fatherID, newCity.getLatitude(), newCity.getLongitude(),
                    newCity.getCountry(), newCity.getCity(),  "marriage", year));
            eDao.insert(new Event(GenerateID.genID(), userName, motherID, newCity.getLatitude(), newCity.getLongitude(),
                    newCity.getCountry(), newCity.getCity(), "marriage", year));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void createDeath(Person person, int year) {
        String eventID = GenerateID.genID();
        EventDao eDao = new EventDao(conn);
        rollCity();

        try {
            eDao.insert(new Event(eventID, person.getAssociatedUsername(), person.getPersonID(), newCity.getLatitude(),
                    newCity.getLongitude(), newCity.getCountry(), newCity.getCity(),  "birth", year));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    public void createBirth(Person person, int year) {
        String eventID = GenerateID.genID();
        EventDao eDao = new EventDao(conn);
        rollCity();

        try {
            eDao.insert(new Event(eventID, person.getAssociatedUsername(), person.getPersonID(), newCity.getLatitude(),
                    newCity.getLongitude(), newCity.getCountry(), newCity.getCity(),  "death", year));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
