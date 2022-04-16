package com.revature;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.cli.Controller;
import com.revature.cli.View;
import com.revature.database.PostgreSQLDatabase;
import com.revature.repositories.UserDao;
import com.revature.services.AuthService;
import com.revature.services.UserService;

public class ErsDriver {

  static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    Connection connection = PostgreSQLDatabase.getConnection();
    UserDao userDao = new UserDao(connection);
    UserService userService = new UserService(userDao);
    AuthService authService = new AuthService(userService);
    Controller controller = new Controller(authService, userService);

    View console = new View(controller);
    console.init();
  }

}
