package Service;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import Model.AuthToken;
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
        LoginResult newLogin;
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
        String personID = null;
        try (PreparedStatement stmt = connect.prepareStatement(sql)) {
            stmt.setString(1, userName);
            stmt.setString(2, passWord);
            rs = stmt.executeQuery();
            if (rs.next()) {
                personID = rs.getString("personID");
            }
        } catch (SQLException e) {
            newLogin = new LoginResult(false, "Internal Server error");
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newLogin = new LoginResult(false, "Internal server error");
            e.printStackTrace();
            return newLogin;
        }

        if(personID != null) {
            String newAuthToken = GenerateID.genID();
            updateAuthToken(newAuthToken, userName);
            newLogin = new LoginResult(newAuthToken, userName, personID, true);
        } else {
            newLogin = new LoginResult(false, "Error: Request property missing or has invalid value");
        }
        return newLogin;
    }

    public void updateAuthToken(String authToken, String userName) {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        AuthToken newToken = new AuthToken(authToken, userName);
        AuthTokenDao aDao = new AuthTokenDao(connect);
        try {
            aDao.update(newToken);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
}
