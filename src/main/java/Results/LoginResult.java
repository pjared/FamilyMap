package Results;

public class LoginResult {
    String authToken;
    String userName;
    String personID;
    boolean success;
    String message;

    public LoginResult() {
    }

    public LoginResult(String authToken, String userName, String personID, boolean success) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.success = success;
    }

    public LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
