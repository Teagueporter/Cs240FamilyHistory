package results;

/**
 * EventResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */
public class EventResult extends DefaultResponse{
    String eventID;
    String associatedUsername;
    String personID;
    Float latitude;
    Float longitude;
    String country;
    String city;
    String eventType;
    Integer year;

    public EventResult(String eventID, String associatedUsername, String personID, float latitude, float longitude, String country, String city, String eventType, int year, boolean success) {
        super();
        this.eventID = eventID;
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.message = null;
        this.success = true;
    }

    public EventResult(String message, boolean success){
        this.success = success;
        this.message = message;
        this.eventID = null;
        this.associatedUsername = null;
        this.personID = null;
        this.latitude = null;
        this.longitude = null;
        this.country = null;
        this.city = null;
        this.eventType = null;
        this.year = null;
    }

    public EventResult() {

    }

    public String getEventID() {
        return eventID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }
}
