package dao;

import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * AuthTokenDao class to help the model interact with the database
 * by creating an insert, find and clear function.
 */
public class AuthTokenDao  extends DatabaseManager{

    public Connection connection;

    public AuthTokenDao(){

    }

    public AuthTokenDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Function to insert the Authtoken into the DB
     * @param token, takes in an authoken
     * @return returns the success of the insert
     */
    public void insert(AuthToken token) throws DataAccessException {
        String sql = "INSERT INTO AuthToken (authtoken, username) VALUES(?,?)";

        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, token.getAuthtoken());
            stmt.setString(2, token.getUsername());
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting authToken");
        }
    }

    /**
     * Function to find and return an AuthToken
     * @param token takes in a token to be found
     * @return returns an authoken
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken Where authtoken = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));
                return authToken;
            }
            else return null;
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an authToken");
        }
    }

    public boolean findToken(String authToken) throws DataAccessException{
        ResultSet rs;
        String sql = "SELECT * FROM AuthToken Where authtoken = ?;";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return true;
            }
            else return false;
        } catch (SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding an authToken");
        }
    }

    /**
     * Function to clear the authtoken from user
     * @return returns the success of the clear
     */
    public void clear() throws DataAccessException {
        boolean success = false;
        String sql = "DELETE FROM AuthToken";
        try(PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.executeUpdate();
        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing the user table");
        }
    }
}
