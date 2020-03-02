package Service;

import DAOs.Connect;
import DAOs.DataAccessException;
import Model.Event;
import Results.EventResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

        //get the username from the event ID, make sure username is with authtoken
        String sql = "SELECT * FROM event WHERE eventID = ?";
        ResultSet rs = null;
        String userName = null;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("associatedUsername");
            } else {
                newEvent = new EventResult(false, "Invalid eventID parameter");
            }
        } catch (SQLException e) {
            newEvent = new EventResult(false, "Internal Server error");
            e.printStackTrace();
        }

        if(!(userName == null)) {
            //Check to find if the username and authtoken match
            boolean foundUser = false;
            sql = "SELECT * FROM authToken WHERE username = ? AND authToken = ?";
            try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                stmt.setString(1, userName);
                stmt.setString(2, authToken);
                rs = stmt.executeQuery();
                if (rs.next()) {
                    foundUser = true;
                }
            } catch (SQLException e) {
                newEvent = new EventResult(false, "Internal Server error");
                e.printStackTrace();
            }

            //Did find the user and the authtoken, so continue
            if(foundUser) {
                sql = "SELECT * FROM event WHERE eventID = ?";
                try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                    stmt.setString(1, eventID);
                    rs = stmt.executeQuery();
                    if (rs.next()) {
                        newEvent = new EventResult(rs.getString("eventID"),
                                rs.getString("associatedUsername"), rs.getString("personID"),
                                rs.getFloat("latitude"), rs.getFloat("longitude"),
                                rs.getString("country"), rs.getString("city"),
                                rs.getString("eventType"),rs.getInt("year"), true);
                    } else {
                        newEvent = new EventResult(false, "Invalid personID parameter");
                    }
                } catch (SQLException e) {
                    newEvent = new EventResult(false, "Internal Server error");
                    e.printStackTrace();
                }
            } else {
                newEvent = new EventResult(false, "Requested person does not belong to this user");
            }
        }

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

        String sql = "SELECT * FROM authToken WHERE authToken = ?";
        ResultSet rs;
        String userName = null;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                userName = rs.getString("username");
            }
        } catch (SQLException e) {
            newEvent = new EventResult(false, "Internal Server error");
            e.printStackTrace();
        }

        ArrayList<Event>allEvents = new ArrayList();
        Event event;
        if(userName != null) {
            sql = "SELECT * FROM event WHERE associatedUsername = ?";
            try (PreparedStatement stmt = connect.prepareStatement(sql)) {
                stmt.setString(1, userName);
                rs = stmt.executeQuery();
                while(rs.next()) {
                    event = new Event(rs.getString("eventID"),
                            rs.getString("associatedUsername"), rs.getString("personID"),
                            rs.getFloat("latitude"), rs.getFloat("longitude"),
                            rs.getString("country"), rs.getString("city"),
                            rs.getString("eventType"),rs.getInt("year"));
                    allEvents.add(event);
                }
            } catch (SQLException e) {
                newEvent = new EventResult(false, "Internal Server error");
                e.printStackTrace();
            }
        } else {
            newEvent = new EventResult(false, "Invalid auth token");
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newEvent = new EventResult(false, "Internal server error");
            e.printStackTrace();
        }

        if(allEvents.size() > 0) {
            newEvent = new EventResult(allEvents, true);
        }
        return newEvent;
    }
}