package com.revature.models;

/**
 * This concrete User class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 * <li>First Name</li>
 * <li>Last Name</li>
 * <li>Email</li>
 * <li>Phone Number</li>
 * <li>Address</li>
 * </ul>
 *
 */
public class User extends AbstractUser {

    public User(int id, String username, String password, String email, String firstname, String lastname, Role role) {
        super(id, username, password, email, firstname, lastname, role);
    }

    public User() {
    }

    /**
     * This includes the minimum parameters needed for the
     * {@link com.revature.models.AbstractUser} class.
     * If other fields are needed, please create additional constructors.
     */

}
