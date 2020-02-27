package Requests;

public class RegisterRequest {
    //services are going to check json for files and then pass it onto DAO
    String userName;
    String password;
    String email;
    String firstName;
    String lastName;
    char gender;

    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, char gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * This function will take a JSON when a user creates a new account and
     * will generate a response returned in JSON format if everything is correct
     * @param jsonString the JSON string given by API call
     * @return the response of the call in JSON format
     */
    public String register(String jsonString) {

        return "";
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }
}
