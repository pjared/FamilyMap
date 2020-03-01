package DAOs;

import Model.User;

import java.sql.*;

public class UserDao {
    private final Connection conn;

    public UserDao(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * This function wil connect to the database and with a given User object,
     * will add the data to the database.
     * @param user the user object
     * @throws DataAccessException thrown when there is trouble with the database.
     */
    public void insert(User user) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO users(userName, passWord, email, firstName, lastName, gender, personID)" +
                " VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * Makes a new person object from the database with given person ID
     * @param userName the userName to find in the database
     * @return the event Object
     * @throws DataAccessException thrown now so not returned to the server
     */
    public User find(String userName) throws DataAccessException {
        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE userName = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userName);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("userName"), rs.getString("passWord"),
                        rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("personID"));
                return user;
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
     * This function will clear the users table from the database
     * @throws DataAccessException thrown when no connection can be made to the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM users";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while trying to clear table");
        }
    }

    public void login() {

    }

    /**
     * This function will generate a unique ID for the person and return
     * it to be sent to the database
     * @return the unique ID
     */
    public String generatePersonID() {
        return "";
    }

}
