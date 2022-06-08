package results;

/**
 * Default response for all Results
 * has a success boolean and error message
 */

public class DefaultResponse {

    protected String message = null;
    protected boolean success = false;


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}

