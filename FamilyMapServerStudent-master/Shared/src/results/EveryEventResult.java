package results;

import model.Event;

/**
 * EveryEventResult Class returns success, or failure
 * Success: returns all the variables
 * Failure: returns error message
 */

public class EveryEventResult extends DefaultResponse{
    private Event[] data;

    public EveryEventResult(Event[] data, boolean success){
        this.data = data;
        this.success = success;
        this.message = null;
    }
    public EveryEventResult(String message, boolean success){
        this.data = null;
        this.message = message;
        this.success = success;
    }

    public Event[] getData() {
        return data;
    }

    public EveryEventResult() {

    }
}
