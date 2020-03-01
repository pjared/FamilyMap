package Model;

import java.util.Objects;
import java.util.Set;

public class Person {
    private String associatedUsername;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    Set<Model.Event> personEvents;

    public Person() {
    }

    public Person(String userName, String personID, String firstName, String lastName, String gender) {
        this.associatedUsername = userName;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public Person(String userName, String personID, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID) {
        this.associatedUsername = userName;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    /**
     * Returns the person's user name
     * @return the person's user name
     */
    public String getAssociatedUsername() {
        return associatedUsername;
    }

    /**
     * Returns the generated Person ID
     * @return the generated person ID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Returns the person's inputted first name
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the person's last name
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the gender of the person
     * @return the person's gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * Returns the person's Father's ID
     * @return the person's Father's ID
     */
    public String getFatherID() {
        return fatherID;
    }

    /**
     * Returns the person's mother's ID
     * @return the person's mother's ID
     */
    public String getMotherID() {
        return motherID;
    }

    /**
     * Returns the ID of the person's spouse
     * @return the ID of the person's spouse
     */
    public String getSpouseID() {
        return spouseID;
    }

    /**
     * Sets the inputted user name of the person
     * @param associatedUsername the person's user name
     */
    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
    }

    /**
     * Sets the ID of the person
     * @param personID the person's ID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Sets the first name of the person
     * @param firstName the person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the last name of the person
     * @param lastName the person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the gender of the person
     * @param gender the gender of the person
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the ID of the person's father
     * @param fatherID the father's ID
     */
    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    /**
     * Sets the ID of the person's mother
     * @param motherID the person's mother
     */
    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    /**
     * Sets the ID of the person's spouse
     * @param spouseID the ID of the spouse
     */
    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    /**
     * the person toString() function
     * @return Returns a string of all the data objects in a person class
     */
    @Override
    public String toString() {
        return "Person{" +
                "userName='" + associatedUsername + '\'' +
                ", personID='" + personID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", fatherID='" + fatherID + '\'' +
                ", motherID='" + motherID + '\'' +
                ", spouseID='" + spouseID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return associatedUsername.equals(person.associatedUsername) &&
                personID.equals(person.personID) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                gender.equals(person.gender) &&
                Objects.equals(fatherID, person.fatherID) &&
                Objects.equals(motherID, person.motherID) &&
                Objects.equals(spouseID, person.spouseID) &&
                Objects.equals(personEvents, person.personEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(associatedUsername, personID, firstName, lastName, gender, fatherID, motherID, spouseID, personEvents);
    }
}
