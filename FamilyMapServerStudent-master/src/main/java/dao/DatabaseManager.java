package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * The Database Manager class helps the other Dao classes
 * by opening, closing, creating and clearing the DB
 */

public class DatabaseManager {
    private Connection connection;
    boolean success;

    private EventDao eventDao;
    private PersonDao personDao;
    private UserDao userDao;
    private AuthTokenDao authDao;

    /**
     * Opens the Database
     */
    public Connection openDB() throws DataAccessException{
        try{
            final String CONNECTION_URL = "jdbc:sqlite:FamHistDB.db";
            connection = DriverManager.getConnection(CONNECTION_URL);
            connection.setAutoCommit(false);

        } catch(SQLException e){
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        eventDao = new EventDao(connection);
        personDao = new PersonDao(connection);
        userDao = new UserDao(connection);
        authDao = new AuthTokenDao(connection);

        return connection;
    }

    public Connection getConnection() throws DataAccessException{
        if(connection == null){
            return openDB();
        }
        else{
            return connection;
        }
    }

    /**
     * closes the Database
     */
    public void closeDB(boolean commit){
        try{
            if(commit){
                connection.commit();
            }
            else{
                connection.rollback();
            }
            connection.close();;
            connection = null;
        } catch (SQLException e){
            e.printStackTrace();
        }
        eventDao = null;
        personDao = null;
        userDao = null;
        authDao = null;
    }

    /**
     * Initializes the Database
     * @return returns the success
     */
//    private boolean initializedDB(){
//
//        return success;
//    }
/*    private boolean clearTables(){
        return success;
    }*/
/*    private boolean createTables(){
        return success;
    }*/

    public EventDao getEventDao() {
        return eventDao;
    }

    public PersonDao getPersonDao() {
        return personDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public AuthTokenDao getAuthDao() {
        return authDao;
    }

    /**
     * Function to delete all tables
     * @return returns the success
     */


    public boolean isSuccess() {
        return success;
    }

}
