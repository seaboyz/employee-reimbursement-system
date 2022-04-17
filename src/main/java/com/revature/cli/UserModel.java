package com.revature.cli;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;
import com.revature.services.AuthService;
import com.revature.services.UserService;

public class UserModel {
  private AuthService authService;
  private UserService userService;

  public UserModel(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public User auth(String username, String password) {
    try {
      return authService.login(username, password);
    } catch (SQLException e) {
      System.out.println("status 500");
      System.out.println("Server Error");
    } catch (UserNamePasswordNotMatchException e) {
      System.out.println("Wrong password");
    } catch (UserNotExistException e) {
      System.out.println("User not exit");
    }
    return null;

  }

  public User add(User user) {
    try {
      return authService.register(user);
    } catch (SQLException e) {
      System.out.println("status 500");
      System.out.println("Server Error");
    } catch (UsernameNotUniqueException e) {
      System.out.println("User name is already been taken, try another one");
    }
    return null;
  }

  public User getUserById(int id) {
    return null;
  }

  public User getUserByUsername(String username) {
    try {
      Optional<User> user = userService.getByUsername(username);
      if (user.isPresent()) {
        return user.get();
      }
      return null;
    } catch (SQLException e) {
      System.out.println("status 500");
      System.out.println("Server Error");
    }
    return null;
  }

  public List<User> getAllUsers() {
    return null;
  }

  public User update(User user) {
    try {
      return userService.updateUser(user);
    } catch (SQLException e) {
      System.out.println("status 500");
      System.out.println("Server Error");
    }
    return null;
  }

  public User remove(User user) {
    try {
      return userService.removeUser(user);
    } catch (SQLException e) {
      System.out.println("status 500");
      System.out.println("Server Error");
    }
    return null;
  }

}
