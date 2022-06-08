package results;
/**
 * FillResult Class returns success, or failure
 * Success: returns success message
 * Failure: returns error message
 */

public class FillResult extends DefaultResponse{
    public FillResult(String message, boolean success){
        this.message = message;
        this.success = success;
    }
}
