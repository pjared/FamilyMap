package Service;

import Requests.LoginRequest;
import Results.LoginResult;

public class LoginService {
    /**
     * This function will take in a LoginRequest object and pull the data
     * to get a new AuthToken for the user and return a LoginResult object
     * @param r the LoginRequest object
     * @return the LoginResult object with data.
     */
    public LoginResult login(LoginRequest r) {
        LoginResult newLogin;
        String userName = r.getUserName();
        String password = r.getPassword();
        //somehow need to check that both match up in the same entry
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        newLogin = new LoginResult(true, "Error");
        newLogin = new LoginResult("ABC", userName, "123", true);
        return newLogin;
    }
}
