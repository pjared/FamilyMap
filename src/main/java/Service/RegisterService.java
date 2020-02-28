package Service;

import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.UserDao;
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
        User newUser = new User(r.getUserName(), r.getPassword(),r.getEmail(),
                                r.getFirstName(),r.getLastName(),String.valueOf(r.getGender()));

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            success = false;
            newRegister.setMessage("Internal Server Error");
            newRegister.setSuccess(false);
            e.printStackTrace();
        }

        try {
            uDao.insert(newUser);
        } catch (DataAccessException e) {
            success = false;
            //TODO: Need to add another error message for non unique userName
            newRegister.setMessage("Invalid Data");
            newRegister.setSuccess(false);
            e.printStackTrace();
        }

        if(success = true) {
            //TODO: Data Generation for Auth Token and person ID
            newRegister.setAuthToken("ASD");
            newRegister.setUserName(r.getUserName());
            newRegister.setPersonID("123");
            newRegister.setSuccess(true);
        }
        return newRegister;
    }

}
