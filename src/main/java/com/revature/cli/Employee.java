package com.revature.cli;

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
public class Employee {
    private int id;
    private String username;
    private String password;
    private String email;
    private String firstname;
    private String lastname;
    private String role;

    public Employee(int id, String username, String password, String email, String firstname, String lastname,
            String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEmail() {
        return this.email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getRole() {
        return this.role;
    }

}
