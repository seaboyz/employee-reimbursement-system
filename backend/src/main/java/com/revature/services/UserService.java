package com.revature.services;

import java.sql.SQLException;
import java.util.Optional;

import com.revature.exceptions.UserNotExistException;
import com.revature.models.User;
import com.revature.repositories.UserDao;

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

  private UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public User addUser(User newUser) throws SQLException {
    return userDao.add(newUser);
  }

  /**
   * Should retrieve a User with the corresponding username or an empty optional
   * if there is no match.
   */
  public Optional<User> getByUsername(String username) throws SQLException {
    return userDao.get(username);
  }

  public User getByUserId(int id) throws SQLException, UserNotExistException {
    Optional<User> user = userDao.get(id);
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UserNotExistException();
    }
  }

  public User updateUser(User userTobeUpdated) throws SQLException {
    return userDao.update(userTobeUpdated);
  }

  public User removeUser(User currentEmployee) throws SQLException {
    return null;
  }
}
