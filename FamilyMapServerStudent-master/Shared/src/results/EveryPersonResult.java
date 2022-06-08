package results;

import model.Person;

/**
 * EveryPersonResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */

public class EveryPersonResult extends DefaultResponse{
    private Person[] data;

    public EveryPersonResult(Person[] data, boolean success){
        this.data = data;
        this.success = success;
        this.message = null;
    }
    public EveryPersonResult(String message, boolean success){
        this.data = null;
        this.message = message;
        this.success = success;
    }

    public Person[] getData() {
        return data;
    }

    public EveryPersonResult() {

    }
}
