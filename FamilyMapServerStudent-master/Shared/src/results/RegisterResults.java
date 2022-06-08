package results;

/**
 * EventResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */

public class RegisterResults extends DefaultResponse{
    private String username;
    private String authtoken;
    private String personID;

    public RegisterResults(String authtoken, String username, String personID, boolean success){
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
        this.success = success;
    }
    public RegisterResults(String message, boolean success){
        this.success = false;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
