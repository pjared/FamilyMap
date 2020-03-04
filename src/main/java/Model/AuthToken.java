package Model;

public class AuthToken {
    //need to make an authtoken table
    private String userName;
    private String userAuthToken;

    public AuthToken(String authToken, String userName) {
        this.userAuthToken = authToken;
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAuthToken() {
        return userAuthToken;
    }

    public void setUserAuthToken(String userAuthToken) {
        this.userAuthToken = userAuthToken;
    }

    @Override
    public String toString() {
        return "AuthToken{" +
                "userName='" + userName + '\'' +
                ", userAuthToken='" + userAuthToken + '\'' +
                '}';
    }
}
