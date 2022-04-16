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
  private User loggedInUser;

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
      if (loggedInUser == null) {
        homePage();
      } else {
        mainMenuPage();
      }
      String choice = scan.nextLine();
      switch (choice) {
        case "1":
          loginPage();
          break;
        case "2":
          registerPage();
          break;
        case "3":
          logout();
          break;
        case "4":
          updateInfoPage();
          break;
        case "q":
          quit = true;
          break;
      }
    }

    goodBuyPage();
  }

  private void registerPage() {
    System.out.println("______________________________");
    System.out.println("|      Registration Page      |");
    System.out.println("|_____________________________|");

    System.out.println("Please Enter your First name:");
    String firstname = scan.nextLine();
    System.out.println("Your first name is: " + firstname);

    System.out.println("Please Enter your Last name:");
    String lastname = scan.nextLine();
    System.out.println("Your last name is: " + lastname);

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
      System.out.println("Please login ");
      loginPage();
    } catch (SQLException e) {
      System.out.println("Something wrong with the database");
      System.out.println("Try again Later");
      e.printStackTrace();
      registerPage();
    } catch (UsernameNotUniqueException e) {
      System.out.println("Your email is already in use, try to use another one");
      registerPage();
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

    loginWithUsernameAndPassword(username, password);

  }

  private void homePage() {
    System.out.println("*********************************");
    System.out.println("*       Press 1 to login        *");
    System.out.println("*       press 2 to register     *");
    System.out.println("*       press q to quit         *");
    System.out.println("*********************************");
  }

  private void goodBuyPage() {
    System.out.println("*********************************");
    System.out.println("*       Thanks for using ERS     *");
    System.out.println("*             Good Bye          *");
    System.out.println("*********************************");
  }

  private void welcomePage(User user) {
    System.out.println("Welcome " + user.getFirstname() + " " + user.getLastname());
  }

  private void mainMenuPage() {
    System.out.println("********************************");
    System.out.println("*          User Menu            *");
    System.out.println("*       press 3 to logout       *");
    System.out.println("*   press 4 to update your info *");
    System.out.println("*       press q to quit         *");
    System.out.println("********************************");
    // TODO
  }

  private void updateInfoPage() {
    System.out.println("Please Enter new username:");
    String newUsername = scan.nextLine();
    newUsername = newUsername.equals("") ? loggedInUser.getUsername() : newUsername;
    System.out.println("Please Enter your new password: ");
    String newPassword = scan.nextLine();
    newPassword = newPassword.equals("") ? loggedInUser.getPassword() : newPassword;
    System.out.println("Please Enter your new email: ");
    String newEmail = scan.nextLine();
    newEmail = newEmail.equals("") ? loggedInUser.getEmail() : newEmail;

    update(newUsername, newPassword, newEmail);
  }

  private void update(String username, String password, String email) {
    User userToBeUpdated = new User(
        loggedInUser.getId(),
        username,
        password,
        email,
        loggedInUser.getFirstname(),
        loggedInUser.getLastname(),
        loggedInUser.getRole());
    System.out.println(userToBeUpdated);
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
      loggedInUser = user;
    } catch (UserNotExistException e) {
      System.out.println("User with the username " + username + " does not exist, please try again.");
    } catch (UserNamePasswordNotMatchException e) {
      System.out.println("The password was wrong, please try again.");
    } catch (SQLException e) {
      System.out.println("Something wrong with database");
      e.printStackTrace();
    }
  }

  private void logout() {
    loggedInUser = null;
  }
}
