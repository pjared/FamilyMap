package Service;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.UserDao;
import Model.AuthToken;
import Model.User;
import Requests.LoginRequest;
import Results.LoginResult;

import java.sql.Connection;

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

        //Check first that user is in the databse
        if(checkValidUser(userName, passWord)) {
            //Get the userID from the database
            String personID = getPersonID(userName, passWord);
            if (personID != null) {
                String newAuthToken = GenerateID.genID();
                updateAuthToken(newAuthToken, userName);
                newLogin = new LoginResult(newAuthToken, userName, personID, true);
            } else {
                newLogin = new LoginResult(false, "Error: Request property missing or has invalid value");
            }
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

    public String getPersonID(String userName, String passWord) {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        UserDao uDao = new UserDao(connect);
        String personID = null;
        try {
            personID = uDao.find(userName).getPersonID();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return personID;
    }

    public boolean checkValidUser(String userName, String passWord) {
        boolean foundUser = false;

        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        UserDao uDao = new UserDao(connect);
        User user = null;
        try {
            user = uDao.find(userName);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        if(user != null) {
            if(user.getPassword().equals(passWord)) {
                foundUser = true;
            }
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return foundUser;
    }
}
