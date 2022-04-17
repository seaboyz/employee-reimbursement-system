package com.revature.models;

/**
 * This concrete User class can include additional fields that can be used for
 * extended functionality of the ERS application.
 *
 * Example fields:
 * <ul>
 * <li>id</li>
 * <li>username</li>
 * <li>password</li>
 * <li>email</li>
 * <li>firstname</li>
 * <li>lastname</li>
 * </ul>
 *
 */
public class Employee extends User {

    public Employee(
            int id,
            String username,
            String password,
            String email,
            String firstname,
            String lastname,
            Role role) {
        super(id, username, password, email, firstname, lastname, Role.EMPLOYEE);
    }

}
