package results;

/**
 * Returns the result of the Clear request
 * Success: returns success message
 * Failure: returns error message
 */
public class ClearResult extends DefaultResponse{

    public ClearResult(String message, boolean success){
        this.message = message;
        this.success = success;
    }

}
