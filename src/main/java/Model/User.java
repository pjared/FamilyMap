package Model;

import java.util.Objects;

public class User {
    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String personID;

    public User() {}

    public User(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public User(String userName, String password, String email, String firstName, String lastName, String gender, String personID) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    /**
     * Returns the user's user name
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user's user name
     * @param userName user's inputted user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Returns user's password
     * @return the user's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the user's password
     * @param password user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the user's email
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email
     * @param email the user' inputted email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's first name
     * @return the user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of user
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the user's last name
     * @return the user's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of user
     * @param lastName the user's inputted last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the gender of the user
     * @return the user's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user
     * @param gender the inputted gender of the user
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Returns the person's generated ID
     * @return the person's ID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Sets the ID of the person
     * @param personID the person's ID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * the user toString() function
     * @return string of all the data objects in the user class
     */
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", personID='" + personID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                gender.equals(user.gender) &&
                personID.equals(user.personID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, password, email, firstName, lastName, gender, personID);
    }
}
