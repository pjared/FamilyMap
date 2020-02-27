package Results;

import Model.Person;

import java.util.ArrayList;

public class PersonResult {
    ArrayList<Person> data;

    String associateUsername;
    String personID;
    String firstName;
    String lastName;
    char gender;
    String fatherID;
    String motherID;
    String spouseID;

    boolean success;
    String message;

    public PersonResult() {
    }

    public PersonResult(String associateUsername, String personID, String firstName, String lastName, char gender, String fatherID, String motherID, String spouseID, boolean success) {
        this.associateUsername = associateUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
    }

    public PersonResult(ArrayList<Person> data, boolean success) {
        this.data = data;
        this.success = success;
    }

    public PersonResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public String getAssociateUsername() {
        return associateUsername;
    }

    public void setAssociateUsername(String associateUsername) {
        this.associateUsername = associateUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
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
