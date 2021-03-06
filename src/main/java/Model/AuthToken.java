package Model;

import java.util.Objects;

public class AuthToken {
    //need to make an authtoken table
    private String userName;
    private String userAuthToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return userName.equals(authToken.userName) &&
                userAuthToken.equals(authToken.userAuthToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, userAuthToken);
    }

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
