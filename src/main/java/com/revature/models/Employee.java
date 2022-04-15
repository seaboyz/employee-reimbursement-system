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
public class Employee extends User {

    public Employee(
            int id,
            String username,
            String password,
            String email,
            String firstname,
            String lastname) {

        super(id, username, password, email, firstname, lastname, Role.EMPLOYEE);
    }

}
