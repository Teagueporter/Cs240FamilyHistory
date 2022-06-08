package dao;

import model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class to help the Event class interact with the Database
 * by creating functions to insert, find and clear
 */

public class EventDao extends DatabaseManager{
    public Connection connection;

    public EventDao(){

    }

    public EventDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Function to insert event into the database
     * @param event event to be inserted into DB
     * @return return the success of the function
     */
    public void insert(Event event) throws DataAccessException {
        boolean success = false;
        String sql = "INSERT INTO Event (eventID, associatedUsername, personID, latitude, longitude, country, city, eventType, year)" +
                " VALUES(?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getAssociatedUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setFloat(4, event.getLatitude());
            stmt.setFloat(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting an Event into the database");
        }
    }

    /**
     * Function to find an event in the database
     * @param eventID unique id of event to be found
     * @return an Event if found
     */
    public Event find(String eventID)throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if(rs.next()){
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"), rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"), rs.getString("country"), rs.getString("city"), rs.getString("eventType"), rs.getInt("year"));
                return event;
            }
            else return null;
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }
    public boolean findEvent(String eventID)throws DataAccessException {
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE eventID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if(rs.next()){
                return true;
            }
            else return false;
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    public Event findMar(String personID)throws DataAccessException{
        Event event;
        ResultSet rs;
        String sql = "SELECT * FROM Event WHERE eventType= \"Marriage\" AND personID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()){
                event = new Event(rs.getString("eventID"), rs.getString("associatedUsername"), rs.getString("personID"), rs.getFloat("latitude"), rs.getFloat("longitude"), rs.getString("country"), rs.getString("city"), rs.getString("eventType"), rs.getInt("year"));
                return event;
            }
            else return null;
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    /**
     * Function to find all events associated with a userID
     * @param associatedUsername Unique ID of person
     * @return return an Array of Events tied to the userID
     */
    public Event[] findAll(String associatedUsername){
        ArrayList<Event> events = new ArrayList<Event>();

        try {
            ResultSet rs = null;
            String sql = "SELECT * FROM Event WHERE associatedUsername = ?;";
            try(PreparedStatement stmt  = connection.prepareStatement(sql)) {
                stmt.setString(1, associatedUsername);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    Event tempEvent = new Event();
                    tempEvent.setEventID(rs.getString(1));
                    tempEvent.setAssociatedUsername(rs.getString(2));
                    tempEvent.setPersonID(rs.getString(3));
                    tempEvent.setLatitude(rs.getFloat(4));
                    tempEvent.setLongitude(rs.getFloat(5));
                    tempEvent.setCountry(rs.getString(6));
                    tempEvent.setCity(rs.getString(7));
                    tempEvent.setEventType(rs.getString(8));
                    tempEvent.setYear(rs.getInt(9));
                    events.add(tempEvent);
                    //tempEvent = null;
                }
            } catch (SQLException e){
                e.printStackTrace();
                throw new DataAccessException("Error encountered while finding a person in the database");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        Event[] eventArray = new Event[events.size()];
        eventArray = events.toArray(eventArray);
        return eventArray;
    }

    /**
     * Function to clear all events from a person
     * @return return the success of the function
     */
    public boolean clear() throws DataAccessException {
        boolean success = true;
        String sql = "DELETE FROM Event";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }

        return success;
    }
}
