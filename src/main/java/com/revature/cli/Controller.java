package com.revature.cli;

import com.revature.models.User;

public class Controller {
  UserModel userModel;

  public Controller(UserModel userModel) {
    this.userModel = userModel;
  }

  public User updateUser(int userId, String username, String password, String email) {

    User userTobeUpdated = new User();
    userTobeUpdated.setId(userId);
    userTobeUpdated.setUsername(username);
    userTobeUpdated.setPassword(password);
    userTobeUpdated.setEmail(email);

    return userModel.update(userTobeUpdated);
  }

  public User register(
      String email,
      String password,
      String firstname,
      String lastname) {

    User newUser = new User(email, password, firstname, lastname);

    return userModel.add(newUser);
  }

  public User login(String username, String password) {
    return userModel.auth(username, password);
  }

  public User removeUser(User userTobeDeleted) {
    return userModel.remove(userTobeDeleted);
  }

}
