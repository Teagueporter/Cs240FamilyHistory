package dao;

import model.User;

import java.sql.*;

/**
 * The userDao class helps the User class interact with the database
 * without it getting messed up if things change
 */

public class UserDao extends DatabaseManager{
    public Connection connection;

    public UserDao(){

    }

    public UserDao(Connection connection){
        this.connection = connection;
    }

    /**
     * Insert a user into the database
     * @param user this is the User of the Database
     * @return the success of the function, success or fail
     */
    public void insert(User user) throws DataAccessException{
        //boolean success = false;
        String sql = "INSERT INTO User (username, password, email, firstName, lastName, gender, personID)" +
                " VALUES(?,?,?,?,?,?,?)";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
            //success = true;
        } catch(SQLException e){
            e.printStackTrace();
            //success = false;
            throw new DataAccessException("Error encountered while inserting a user into the database");
        }
    }

    /**
     * Find a user in the database
     * @param username the username that we want to find
     * @return we return the user if found
     */
    public User find(String username) throws DataAccessException{
        User user;
        ResultSet rs;
        String sql = "SELECT * FROM User WHERE username = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if(rs.next()){
                user = new User(rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("firstName"), rs.getString("lastName"), rs.getString("gender"), rs.getString("personID"));
                return user;
            }
            else{
                return null;
            }
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an event in the database");
        }
    }

    /**
     * clear the users from the tables
     * @return the success of the function
     */
    public void clear() throws DataAccessException {
        boolean success = false;
        String sql = "DELETE FROM User";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }

}
