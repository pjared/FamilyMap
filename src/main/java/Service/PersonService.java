package Service;

import DAOs.AuthTokenDao;
import DAOs.Connect;
import DAOs.DataAccessException;
import DAOs.PersonDao;
import Model.AuthToken;
import Model.Person;
import Results.PersonResult;

import java.sql.Connection;

public class PersonService {
    private Connect db = new Connect();
    /**
     * The getFamily method will return all all family members,
     * given an auth token as a parameter from the user.
     * @param authToken the authToken from the user
     * @return The personResult object
     */
    public PersonResult getFamily(String authToken) {
        PersonResult newPerson = new PersonResult();
        //check the authToken and get the user from the authToken

        //open a connection to pass into DAOs
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        // Create DAO vars to find the data
        AuthTokenDao aDao = new AuthTokenDao(connect);
        AuthToken token = null;
        try {
            token = aDao.find(authToken);
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }
        // Check the token to see if it is valid
        if(token == null) {
            newPerson = new PersonResult(false, "error: Invalid request data");
        } else {
            //token is valid, get the family for the user
            PersonDao pDao = new PersonDao(connect);
            newPerson = new PersonResult(pDao.getFamily(token.getUserName()) ,true);
        }

        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return newPerson;
    }

    /**
     * The getPerson method will return a single person object with
     * a personID in a Person Result object.
     * @return the person result object
     */
    public PersonResult getPerson(String authToken, String personID) {
        PersonResult newPerson = new PersonResult();

        //Open a connection to pass to DAO's
        Connection connect = null;
        try {
            connect = db.openConnection();
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        AuthTokenDao aDao = new AuthTokenDao(connect);
        AuthToken token = null;
        //Use the DAOs to find the specific data we are looking for
        try {
            token = aDao.find(authToken);
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }
        if(token == null) {
            newPerson = new PersonResult(false, "error: Invalid auth token");
        } else {
            PersonDao pDao = new PersonDao(connect);
            try {
                Person person = pDao.find(personID);

                //Makes sure the personID username matches with AuthToken username
                if(!person.getAssociatedUsername().equals(token.getUserName())) {
                    newPerson = new PersonResult(false, "error:  Requested person does not belong to this user");
                } else {
                    //they match, good to make the new person Result
                    newPerson = new PersonResult(person.getAssociatedUsername(), person.getPersonID(),
                                            person.getFirstName(), person.getLastName(), person.getGender(),
                                            person.getFatherID(), person.getMotherID(), person.getSpouseID(),
                                    true);
                }
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
        }

        //Close the database
        try {
            db.closeConnection(true);
        } catch (DataAccessException e) {
            newPerson = new PersonResult(false, "Internal server error");
            e.printStackTrace();
        }

        return newPerson;
    }
}