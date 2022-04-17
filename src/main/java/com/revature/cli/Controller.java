package com.revature.cli;

import java.sql.SQLException;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;

public class Controller {

  private AuthService authService;
  private UserService userService;

  public Controller(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public User update(int userId, String username, String password, String email) throws SQLException {
    User userTobeUpdated = new User();
    userTobeUpdated.setId(userId);
    userTobeUpdated.setUsername(username);
    userTobeUpdated.setPassword(password);
    userTobeUpdated.setEmail(email);
    return userService.updateUser(userTobeUpdated);

  }

  public User register(
      String email,
      String password,
      String firstname,
      String lastname) throws SQLException {
    User newUser = new User(email, password, firstname, lastname);

    return authService.register(newUser);
  }

  public User login(String username, String password)
      throws UserNamePasswordNotMatchException, UserNotExistException, SQLException {
    return authService.login(username, password);
  }

  public User removeUser(User userTobeDeleted) throws SQLException {
    return userService.removeUser(userTobeDeleted);
  }

}
