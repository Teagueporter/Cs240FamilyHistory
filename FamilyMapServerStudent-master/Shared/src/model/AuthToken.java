package model;

import java.util.Objects;

/**
 * This class is for the Authentication Tokens
 * when a user logs into the database
 * so we know who it is
 */

public class AuthToken {
    /**
     * Unique authoken
     */
    private String authtoken;
    /**
     * Username that is associated with the authtoken
     */
    private String username;

    /**
     * AuthToken Constructor
     * @param authtoken this is a unique string that authenicates the user
     * @param username this is the username that is associated with the authtoken
     */

    public AuthToken(String authtoken, String username) {
        this.authtoken = authtoken;
        this.username = username;
    }

    public String getAuthtoken() {
        return authtoken;
    }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(authtoken, authToken.authtoken) && Objects.equals(username, authToken.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authtoken, username);
    }
}
