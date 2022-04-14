package com.revature;

import java.sql.Connection;
import java.util.Scanner;

import com.revature.database.PostgreSQLDatabase;
import com.revature.repositories.UserDAO;
import com.revature.repositories.UserDAOImplementation;
import com.revature.services.AuthService;
import com.revature.services.UserService;
import com.revature.view.console.Console;

public class ErsDriver {

  static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    Connection connection = PostgreSQLDatabase.getConnection();
    UserDAO userDao = new UserDAOImplementation(connection);
    UserService userService = new UserService(userDao);
    AuthService authService = new AuthService(userService);
    Console console = new Console(authService);
    console.init();
  }

}
