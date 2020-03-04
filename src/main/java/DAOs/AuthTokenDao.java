package DAOs;

import Model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * The insert method will take an AuthToken object to add it to the database
     * @param authToken the authtoken object
     * @throws DataAccessException thrown when the database cannot be accessed
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO authToken(username, authToken) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, authToken.getUserName());
            stmt.setString(2, authToken.getUserAuthToken());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * This function will connect to the database to update the
     * users auth token
     */
    public void update(AuthToken authToken) throws DataAccessException {
        String sql = "UPDATE authToken SET authToken = ? WHERE username = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getUserAuthToken());
            stmt.setString(2, authToken.getUserName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * This function will clear the Auth Token table.
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM authToken";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to clear table");
        }
    }

    public AuthToken find(String authToken) throws DataAccessException {
        AuthToken token;
        ResultSet rs = null;

        String sql = "SELECT * FROM authToken WHERE authToken = ?";
        //get the user associated with the authToken
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if(rs.next()) {
                token = new AuthToken(rs.getString("authToken"), rs.getString("username"));
                return token;
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

}
