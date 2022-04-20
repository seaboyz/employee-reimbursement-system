package com.revature.cli;

import java.sql.Connection;

import com.revature.database.PostgreSQLDatabase;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.UserService;

public class App {

  private static Controller controller;

  public static void main(String[] args) {
    init();
    View view = new View(controller);
    view.init();
    ;
  }

  private static void init() {
    Connection connection = PostgreSQLDatabase.getConnection();
    UserDao userDao = new UserDao(connection);
    UserService userService = new UserService(userDao);
    AuthService authService = new AuthService(userService);
    UserModel userModel = new UserModel(authService, userService);
    Mapper mapper = new Mapper();
    controller = new Controller(userModel, mapper);
  }

}
