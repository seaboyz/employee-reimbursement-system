package com.revature;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.controller.UserController;
import com.revature.database.PostgreSQLDatabase;
import com.revature.repositories.Dao;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.view.console.Console;

public class ErsDriver {

  static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    Connection connection = PostgreSQLDatabase.getConnection();
    Dao userDao = new UserDao(connection);
    UserService userService = new UserService(userDao);
    AuthService authService = new AuthService(userService);
    UserController userController = new UserController(authService);

    Console console = new Console(userController);
    console.init();
  }

}
