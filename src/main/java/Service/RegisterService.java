package Service;

import Requests.RegisterRequest;
import Results.RegisterResult;

public class RegisterService {
    /**
     * This will take an RegisterRequest object to add a new user to the database
     * @param r the object made from the API call
     * @return the result of the function call
     */
    public RegisterResult register(RegisterRequest r) {
        RegisterResult newRegister = new RegisterResult();
        //need to write this to make sure it works
        return newRegister;
    }

}
