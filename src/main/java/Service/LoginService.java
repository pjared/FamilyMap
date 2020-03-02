package Service;

import DAOs.Connect;
import DAOs.DataAccessException;
import Requests.LoginRequest;
import Results.LoginResult;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginService {
    private Connect db = new Connect();

    /**
     * This function will take in a LoginRequest object and pull the data
     * to get a new AuthToken for the user and return a LoginResult object
     * @param r the LoginRequest object
     * @return the LoginResult object with data.
     */
    public LoginResult login(LoginRequest r) {
        LoginResult newLogin = new LoginResult();
        String userName = r.getUserName();
        String passWord = r.getPassword();

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newLogin = new LoginResult(false, "Internal server error");
            e.printStackTrace();
        }

        ResultSet rs = null;
        String sql = "SELECT * FROM users WHERE userName = ? AND passWord = ?";
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, passWord);
            rs = stmt.executeQuery();
            if (rs.next()) {
                newLogin = new LoginResult("ABC", userName,  rs.getString("personID"), true);//generate a new authToken here
            } else {
                newLogin = new LoginResult(false, "Did not find user");
            }
        } catch (SQLException e) {
            newLogin = new LoginResult(false, "Did not find user");
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newLogin = new LoginResult(false, "Internal server error");
            e.printStackTrace();
        }

        return newLogin;
    }
}
