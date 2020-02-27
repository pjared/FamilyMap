package Service;

import Requests.registerRequest;
import Results.RegisterResult;

public class RegisterService {
    /**
     * This will take an RegisterRequest object to add a new user to the database
     * @param r the object made from the API call
     * @return the result of the function call
     */
    RegisterResult register(registerRequest r) {
        RegisterResult newRegister = new RegisterResult();
        return newRegister;
    }

}
