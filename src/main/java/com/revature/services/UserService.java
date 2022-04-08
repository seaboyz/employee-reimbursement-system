package com.revature.services;

import com.revature.models.User;

import java.util.Optional;

/**
 * The UserService should handle the processing and retrieval of Users for the ERS application.
 * <p>
 * {@code getByUsername} is the only method required;
 * however, additional methods can be added.
 * <p>
 * Examples:
 * <ul>
 *     <li>Create User</li>
 *     <li>Update User Information</li>
 *     <li>Get Users by ID</li>
 *     <li>Get Users by Email</li>
 *     <li>Get All Users</li>
 * </ul>
 */
public class UserService {
  private MockDB mockDB = MockDB.getInstance();

  /**
   * Should retrieve a User with the corresponding username or an empty optional if there is no match.
   */
  public Optional<User> getByUsername(String username) {
    User user = mockDB.getUserByUsername(username);
    return user!=null ? Optional.of(user) : Optional.empty();
  }
}
