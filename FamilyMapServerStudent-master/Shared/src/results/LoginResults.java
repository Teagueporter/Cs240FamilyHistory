package results;

/**
 * LoginResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */

public class LoginResults extends DefaultResponse{
    private String authtoken;
    private String username;
    private String personID;

    //successful response
    public LoginResults(String authToken, String username, String personID, boolean success) {
        this.authtoken = authToken;
        this.username = username;
        this.personID = personID;
        this.success = true;
        this.message = null;
    }
    //failed response
    public LoginResults(String message, boolean success){
        this.authtoken = null;
        this.username = null;
        this.personID = null;
        this.success = success;
        this.message = message;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public String getUsername() {
        return username;
    }

    public String getPersonID() {
        return personID;
    }
}
