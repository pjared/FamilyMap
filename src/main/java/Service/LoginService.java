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
    LoginResult login(LoginRequest r) {
        LoginResult newLogin = new LoginResult();
        return newLogin;
    }
}
