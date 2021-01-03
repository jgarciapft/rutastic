package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Definition of User model as a JavaBean
 */
@XmlRootElement
public class User implements Serializable {

    private static final long SerialVersionUID = 1L;

    private long id;
    private String username;

    public User() {
    }

    /**
     * @param id User ID to be validated
     * @return If the ID is valid. A valid ID is greater than 0
     */
    public static boolean validateID(long id) {
        return id > 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Validate this user bean parsed from a login attempt. The user bean is valid if the username has been set.
     * Due to security concerns there's no additional validation error messages sent back to the user.
     *
     * @return If the user bean is valid for a login attempt
     */
    public boolean validateLoginAttempt() {
        return (username != null && !getUsername().isEmpty());
    }

}
