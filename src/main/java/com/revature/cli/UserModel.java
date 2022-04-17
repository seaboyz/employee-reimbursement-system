package com.revature.cli;

import java.util.List;

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

  public void add(User user) {

  }

  public User getUserById(int id) {
    return null;
  }

  public User getUserByUsername(String username) {
    return null;
  }

  public List<User> getUsers() {
    return null;
  }

  public User update(User user) {
    return null;
  }

  public void remove(User user) {

  }

}
