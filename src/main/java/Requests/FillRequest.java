package Requests;

public class FillRequest {
    String userName;
    int generations;

    public FillRequest(String userName, int generations) {
        this.userName = userName;
        this.generations = generations;
    }
    //user getters and setters

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }
}
