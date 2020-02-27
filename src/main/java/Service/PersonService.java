package Service;

import Results.PersonResult;

public class PersonService {
    /**
     * The getFamily method will return all all family members,
     * given an auth token as a parameter from the user.
     * @param authToken the authToken from the user
     * @return The personResult object
     */
    PersonResult getFamily(String authToken) {
        PersonResult newPerson = new PersonResult();
        return newPerson;
    }

    /**
     * The getPerson method will return a single person object with
     * a personID in a Person Result object.
     * @return the person result object
     */
    PersonResult getPerson(String authToken, String personID) {
        PersonResult newPerson = new PersonResult();
        return newPerson;
    }
}