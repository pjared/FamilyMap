package Service;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.UserDao;
import Model.AuthToken;
import Model.User;
import Requests.RegisterRequest;
import Results.RegisterResult;

import java.sql.Connection;

public class RegisterService {
    private Connect db = new Connect();

    /**
     * This will take an RegisterRequest object to add a new user to the database
     * @param r the object made from the API call
     * @return the result of the function call
     */
    public RegisterResult register(RegisterRequest r) {
        RegisterResult newRegister = new RegisterResult();
        boolean success = true;
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        UserDao uDao = new UserDao(connect);
        String personID =  GenerateID.genID();
        User newUser = new User(r.getUserName(), r.getPassword(),r.getEmail(),
                                r.getFirstName(),r.getLastName(),String.valueOf(r.getGender()), personID);

        try {
            uDao.insert(newUser);
        } catch (DataAccessException e) {
            success = false;
            //TODO: Need to add another error message for non unique userName
            newRegister.setMessage("Error: Username already taken by another user");
            newRegister.setSuccess(false);
            e.printStackTrace();
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            success = false;
            newRegister.setMessage("Internal Server Error");
            newRegister.setSuccess(false);
            e.printStackTrace();
        }

        if(success) {
            //TODO: Data Generation for Auth Token and person ID
            String newAuthToken = GenerateID.genID();
            newRegister.setAuthToken(newAuthToken);
            newRegister.setUserName(r.getUserName());
            addAuthToken(newAuthToken, r.getUserName());
            newRegister.setPersonID(personID);
            newRegister.setSuccess(true);
        }
        return newRegister;
    }

    public void addAuthToken(String authToken, String userName) {
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        AuthToken newToken = new AuthToken(authToken, userName);
        AuthTokenDao aDao = new AuthTokenDao(connect);
        try {
            aDao.insert(newToken);
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
