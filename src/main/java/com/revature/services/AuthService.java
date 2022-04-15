package com.revature.services;

import java.sql.SQLException;
import java.util.Optional;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;

/**
 * The AuthService should handle login and registration for the ERS application.
 * <p>
 * {@code login} and {@code register} are the minimum methods required; however,
 * additional methods can be added.
 * <p>
 * Examples:
 * <ul>
 * <li>Retrieve Currently Logged-in User</li>
 * <li>Change Password</li>
 * <li>Logout</li>
 * </ul>
 */
public class AuthService {
  private UserService userService;

  public AuthService(UserService userService) {
    this.userService = userService;
  }

  /**
   * <ul>
   * <li>Needs to check for existing users with username/email provided.</li>
   * <li>Must throw exception if user does not exist.</li>
   * <li>Must compare password provided and stored password for that user.</li>
   * <li>Should throw exception if the passwords do not match.</li>
   * <li>Must return user object if the user logs in successfully.</li>
   * </ul>
   */
  public User login(String username, String password)
      throws UserNotExistException, UserNamePasswordNotMatchException, SQLException {

    Optional<User> optionalUser = userService.getByUsername(username);

    if (!optionalUser.isPresent()) {
      throw new UserNotExistException();
    }
    if (!optionalUser.get().getPassword().equals(password)) {
      throw new UserNamePasswordNotMatchException();
    }

    return optionalUser.get();

  }

  /**
   * <ul>
   * <li>Should ensure that the username/email provided is unique.</li>
   * <li>Must throw exception if the username/email is not unique.</li>
   * <li>Should persist the user object upon successful registration.</li>
   * <li>Must throw exception if registration is unsuccessful.</li>
   * <li>Must return user object if the user registers successfully.</li>
   * <li>Must throw exception if provided user has a non-zero ID</li>
   * </ul>
   * <p>
   * Note: userToBeRegistered will have an id=0, additional fields may be null.
   * After registration, the id will be a positive integer.
   */
  public User register(User userToBeRegistered) throws SQLException, UsernameNotUniqueException {
    Optional<User> optionalUser = userService.getByUsername(userToBeRegistered.getUsername());

    if (optionalUser.isPresent()) {
      throw new UsernameNotUniqueException();
    }

    return userService.addUser(userToBeRegistered);

  }

}
