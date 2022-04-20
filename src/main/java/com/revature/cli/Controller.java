package com.revature.cli;

import com.revature.models.User;

public class Controller {
  UserModel userModel;
  Mapper mapper;

  public Controller(UserModel userModel, Mapper mapper) {
    this.userModel = userModel;
    this.mapper = mapper;
  }

  public Employee updateUser(int userId, String username, String password, String email) {

    User userTobeUpdated = new User();
    userTobeUpdated.setId(userId);
    userTobeUpdated.setUsername(username);
    userTobeUpdated.setPassword(password);
    userTobeUpdated.setEmail(email);

    User updatedUser = userModel.update(userTobeUpdated);

    if (updatedUser != null) {
      return mapper.userToEmployee(updatedUser);
    }
    return null;
  }

  public Employee register(
      String email,
      String password,
      String firstname,
      String lastname) {

    User newUser = new User(email, password, firstname, lastname);

    User registedUser = userModel.add(newUser);

    if (registedUser != null) {
      return mapper.userToEmployee(registedUser);
    }
    return null;
  }

  public Employee login(String username, String password) {
    User authenticatedUser = userModel.auth(username, password);
    if (authenticatedUser != null) {
      return mapper.userToEmployee(authenticatedUser);
    }
    return null;

  }

  public Employee removeUser(User userTobeDeleted) {
    User removedUser = userModel.remove(userTobeDeleted);

    if (removedUser != null) {
      return mapper.userToEmployee(removedUser);
    }

    return null;
  }

}
