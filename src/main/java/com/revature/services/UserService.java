package com.revature.services;

import java.util.Optional;

import com.revature.models.User;
import com.revature.repositories.UserDAOImplementation;

/**
 * The UserService should handle the processing and retrieval of Users for the
 * ERS application.
 * <p>
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 * <p>
 * Examples:
 * <ul>
 * <li>Create User</li>
 * <li>Update User Information</li>
 * <li>Get Users by ID</li>
 * <li>Get Users by Email</li>
 * <li>Get All Users</li>
 * </ul>
 */
public class UserService {

  private UserDAOImplementation userDaoImpl;

  public UserService(UserDAOImplementation userDaoImpl) {
    this.userDaoImpl = userDaoImpl;
  }

  /**
   * Should retrieve a User with the corresponding username or an empty optional
   * if there is no match.
   */
  public Optional<User> getByUsername(String username) {
    return userDaoImpl.getByUsername(username);
  }
}
