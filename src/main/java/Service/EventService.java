package Service;

import Results.EventResult;

public class EventService {
    /**
     * The getEvent method will get a single event when given an
     * eventID and the users authToken
     * @param authToken the user's authToke
     * @param eventID the eventID the user is requesting
     * @return the event result object
     */
    EventResult getEvent(String authToken, String eventID) {
        EventResult newEvent = new EventResult();
        return newEvent;
    }
    /**
     * This function will return all events for all family members
     * of the current user.
     * @return the event result object
     */
    EventResult getAllEvents(String authToken) {
        EventResult newEvent = new EventResult();
        return newEvent;
    }
}