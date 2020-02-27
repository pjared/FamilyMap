package Model;

public class Event {
    private String eventID;
    private String associatedUser;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    public Event() {}

    public Event(String eventID, String associatedUser, String personID, float latitude, float longitude, String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.associatedUser = associatedUser;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }
    /**
     * Returns the generated ID of the event
     * @return the event ID
     */
    public String getEventID() {
        return eventID;
    }

    /**
     * Returns the associated users with an event
     * @return the associated users
     */
    public String getAssociatedUser() {
        return associatedUser;
    }

    /**
     * Returns the ID of the person
     * @return the person's ID
     */
    public String getPersonID() {
        return personID;
    }

    /**
     * Returns the latitude coordinate of the event
     * @return the latitude coordinate
     */
    public float getLatitude() {
        return latitude;
    }

    /**
     * Returns the longitude coordinate of the event
     * @return the longitude coordinate
     */
    public float getLongitude() {
        return longitude;
    }

    /**
     * Returns the country the even occured in
     * @return the event's country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns the city the event happened in
     * @return the event's city
     */
    public String getCity() {
        return city;
    }

    /**
     * Returns the type of event that happened (i.e. marriage, baptism)
     * @return the event type
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * Returns the year of the event
     * @return the event's year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the generated ID of the event
     * @param eventID the ID of the event
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    /**
     * Sets the associated users with an event
     * @param associatedUser the associated users
     */
    public void setAssociatedUser(String associatedUser) {
        this.associatedUser = associatedUser;
    }

    /**
     * Sets the ID of the person in the event
     * @param personID the person's ID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    /**
     * Sets the latitude coordinate of the event
     * @param latitude the latitude coordinate
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    /**
     * Sets the longitude coordinate of the event
     * @param longitude the longitude coordinate
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    /**
     * Sets the country where the event took place
     * @param country the event's country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Sets the city where the event took place
     * @param city the event's city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Sets the type of event that occurred
     * @param eventType the event type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * Sets the year the event took place
     * @param year the events year
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "Event{" +
                ", eventID='" + eventID + '\'' +
                ", associatedUsers='" + associatedUser + '\'' +
                ", personID='" + personID + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", eventType='" + eventType + '\'' +
                ", year='" + year + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Event)) return false;
        Event event = (Event) o;
        return Float.compare(event.latitude, latitude) == 0 &&
                Float.compare(event.longitude, longitude) == 0 &&
                year == event.year &&
                eventID.equals(event.eventID) &&
                associatedUser.equals(event.associatedUser) &&
                personID.equals(event.personID) &&
                country.equals(event.country) &&
                city.equals(event.city) &&
                eventType.equals(event.eventType);
    }

    //add an equals function to all?
}
