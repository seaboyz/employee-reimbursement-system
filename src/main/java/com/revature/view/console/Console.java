package com.revature.view.console;

import java.sql.SQLException;
import java.util.Scanner;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;
import com.revature.services.AuthService;

public class Console {
  private final Scanner scan = ConsoleScanner.getInstance();
  private final AuthService authService;

  public Console(AuthService authService) {
    this.authService = authService;
  }

  public void init() {
    System.out.println("*********************************");
    System.out.println("*           Welcome to          *");
    System.out.println("* Employee Reimbursement System *");
    System.out.println("*        System Loading...      *");
    System.out.println("*********************************");
    boolean quit = false;

    while (!quit) {
      printOptionsPage();
      String choice = scan.nextLine();
      switch (choice) {
        case "1":
          loginPage();
          break;
        case "2":
          registerPage();
        case "q":
          quit = true;
          break;
      }
    }

    quit();
  }

  private void registerPage() {
    System.out.println("______________________________");
    System.out.println("|      Registration Page      |");
    System.out.println("|_____________________________|");

    System.out.println("Please Enter your First name:");
    String firstname = scan.nextLine();
    System.out.println("Your email is: " + firstname);

    System.out.println("Please Enter your Last name:");
    String lastname = scan.nextLine();
    System.out.println("Your email is: " + lastname);

    System.out.println("Please Enter your email:");
    String email = scan.nextLine();
    System.out.println("Your email is: " + email);

    System.out.println("Please Enter your password:");
    String password = scan.nextLine();
    System.out.println("Your password is: " + password);

    System.out.println("Please confirm your password:");
    String confirmPassword = scan.nextLine();
    System.out.println("Your password is: " + confirmPassword);

    while (!password.equals(confirmPassword)) {
      System.out.println("Passwords have to be same.");
      System.out.println("Please try again.");
      System.out.println("Please Enter your password:");
      password = scan.nextLine();
      System.out.println("Your password is: " + password);

      System.out.println("Please confirm your password:");
      confirmPassword = scan.nextLine();
      System.out.println("Your password is: " + confirmPassword);
    }

    try {
      User user = register(email, password, firstname, lastname);
      welcomePage(user);
    } catch (SQLException e) {
      System.out.println("Something wrong with the database");
      System.out.println("Try again Later");
      e.printStackTrace();
      registerPage();
      return;
    } catch (UsernameNotUniqueException e) {
      System.out.println("Your email is already in use, try to use another one");
      loginPage();

    }

  }

  private void loginPage() {
    System.out.println("Please Enter your username:");
    String username = scan.nextLine();
    System.out.println("Your username is: " + username);
    System.out.println("Please Enter your password: ");
    String password = scan.nextLine();
    System.out.println("Your password is: " + password);

    System.out.println("*********************************");
    System.out.println("*           Login...            *");
    System.out.println("*********************************");
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
      e.printStackTrace();
    }

    loginWithUsernameAndPassword(username, password);

  }

  private void printOptionsPage() {
    System.out.println("*********************************");
    System.out.println("*       Press 1 to login        *");
    System.out.println("*       press 2 to register     *");
    System.out.println("*       press q to quit         *");
    System.out.println("*********************************");
  }

  private void quit() {
    System.out.println("*********************************");
    System.out.println("*       Thanks for using ERS     *");
    System.out.println("*             Good Bye          *");
    System.out.println("*********************************");
  }

  private void welcomePage(User user) {

    System.out.println("Welcome " + user.getFirstname() + " " + user.getLastname());
    userMenu();
  }

  private void userMenu() {
    System.out.println("********************************");
    System.out.println("*         User Menu            *");
    System.out.println("********************************");
  }

  private User register(
      String email,
      String password,
      String firstname,
      String lastname) throws SQLException {
    User newUser = new User(email, password, firstname, lastname);

    return authService.register(newUser);
  }

  private void loginWithUsernameAndPassword(String username, String password) {
    // todo for REST API
    // HttpClient POST http://localhost:8080/login
    // https://hc.apache.org/httpcomponents-client-5.1.x/index.html

    // CloseableHttpClient client = HttpClients.createDefault();
    // HttpPost httpPost = new HttpPost("http://localhost:8080/api/user");
    // List<NameValuePair> params = new ArrayList<>();
    // params.add((new BasicNameValuePair("username", username)));
    // params.add((new BasicNameValuePair("password", password)));
    // httpPost.setEntity(new UrlEncodedFormEntity(params));
    // CloseableHttpResponse response = client.execute(httpPost);

    // if (response.getStatusLine().getStatusCode() == 200) {
    // System.out.println("_______________________________");
    // System.out.println("| Welcome |");
    // System.out.println("| User Menu |");
    // System.out.println("|_____________________________|");
    // } else if (response.getStatusLine().getStatusCode() == 403) {
    // System.out.println("The username and password was wrong, please try again.");
    // System.out.println("Press 0 to go back to the main manu");
    // }

    try {
      User user = authService.login(username, password);
      welcomePage(user);
    } catch (UserNotExistException e) {
      System.out.println("User with the username " + username + " does not exist, please try again.");
      loginPage();
    } catch (UserNamePasswordNotMatchException e) {
      System.out.println("The password was wrong, please try again.");
      loginPage();
    } catch (SQLException e) {
      System.out.println("Something wrong with database");
      e.printStackTrace();
    }
  }
}
