package dao;

import model.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the personDoa which helps us interact with the
 * person data in the database
 */

public class PersonDao extends DatabaseManager{
    private Connection connection;

    public PersonDao(){

    }

    public PersonDao(Connection connection){
        this.connection = connection;
    }
    /**
     * Function to insert a Person into the database
     * @param person person to be inserted into the database
     * @return return the success of the function
     */
    public void insert(Person person) throws DataAccessException {
        boolean success = false;
        String sql = "INSERT INTO Person (personID, associatedUsername, firstName, lastName, gender, fatherID, motherID, spouseID)" +
                " VALUES(?,?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting a person into the database");
        }

        //return false;
    }

    /**
     * Function to find person in database
     * @param personID ID of person to be found
     * @return returns the person object if found
     */
    public Person find(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if(rs.next()){
                person = new Person(rs.getString("personID"), rs.getString("associatedUsername"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("fatherID"), rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
            else return null;
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding a person in the database");
        }
    }

    public boolean findPerson(String personID) throws DataAccessException {
        Person person;
        ResultSet rs;
        String sql = "SELECT * FROM Person WHERE personID = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, personID);
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

    /**
     * Function to find all people connected to the user
     * @param associatedUsername Name a user, so we can find all people connected to them
     * @return return an arraylist of people
     */
    public Person[] findAll(String associatedUsername) throws DataAccessException {
        ArrayList<Person> people = new ArrayList<Person>();

        try {
            ResultSet rs = null;
            String sql = "SELECT * FROM Person WHERE associatedUsername = ?;";
            try(PreparedStatement stmt  = connection.prepareStatement(sql)) {
                stmt.setString(1, associatedUsername);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    Person tempPerson = new Person();
                    tempPerson.setPersonID(rs.getString(1));
                    tempPerson.setAssociatedUsername(rs.getString(2));
                    tempPerson.setFirstName(rs.getString(3));
                    tempPerson.setLastName(rs.getString(4));
                    tempPerson.setGender(rs.getString(5));
                    tempPerson.setFatherID(rs.getString(6));
                    tempPerson.setMotherID(rs.getString(7));
                    tempPerson.setSpouseID(rs.getString(8));
                    people.add(tempPerson);
                    //tempPerson = null;
                }
            } catch (SQLException e){
                e.printStackTrace();
                throw new DataAccessException("Error encountered while finding a person in the database");
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        Person[] peopleArray = new Person[people.size()];
        peopleArray = people.toArray(peopleArray);
        return peopleArray;
    }

    /**
     * Function to delete all Persons in table
     * @return the success of the function
     */
    public boolean clearAll() throws DataAccessException {
        boolean success = true;
        String sql = "DELETE FROM Person";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the person table");
        }

        return success;
    }

    /**
     * Function to clear all Persons from specific user
     * @param username username associataed with the persons
     * @return return the success of the function
     */
    public boolean clearAll(String username){
        boolean success = false;
        return false;
    }
}
