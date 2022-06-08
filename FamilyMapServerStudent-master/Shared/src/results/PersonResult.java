package results;

/**
 * PersonResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */

public class PersonResult extends DefaultResponse{

    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private String gender;
    private String fatherID;
    private String motherID;
    private String spouseID;

    public PersonResult(String personID, String associatedUsername, String firstName, String lastName, String gender, String fatherID, String motherID, String spouseID, boolean success) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
        this.success = success;
        this.message = null;
    }

    public PersonResult(String message, boolean success){
        this.personID = null;
        this.associatedUsername = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.fatherID = null;
        this.motherID = null;
        this.spouseID = null;
        this.success = success;
        this.message = message;
    }

    public PersonResult() {

    }

    public String getPersonID() {
        return personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}
