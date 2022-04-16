package com.revature.cli;

import java.sql.SQLException;
import java.util.Scanner;

import com.revature.exceptions.UserNamePasswordNotMatchException;
import com.revature.exceptions.UserNotExistException;
import com.revature.exceptions.UsernameNotUniqueException;
import com.revature.models.User;
import com.revature.util.Util;

public class View {
  private final Scanner scan = ConsoleScanner.getInstance();

  private Controller controller;
  private User loggedInUser;

  public View(Controller controller) {
    this.controller = controller;
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

  private void logout() {
    loggedInUser = null;
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
      User user = controller.register(email, password, firstname, lastname);
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

    try {
      loggedInUser = controller.login(username, password);
    } catch (UserNotExistException e) {
      System.out.println("user not exist");
    } catch (UserNamePasswordNotMatchException e) {
      System.out.println("Wrong password");
    } catch (SQLException e) {
      System.out.println("server error");
    } finally {
      loginPage();
    }

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
  }

  private void updateInfoPage() {
    System.out.println("Please Enter new username:");
    String newUsername = scan.nextLine();
    System.out.println("Please Enter your new password: ");
    String newPassword = scan.nextLine();
    System.out.println("Please Enter your new email: ");
    String newEmail = scan.nextLine();

    newUsername = newUsername.equals("") ? loggedInUser.getUsername() : newUsername;
    newPassword = newPassword.equals("") ? loggedInUser.getPassword() : newPassword;
    newEmail = newEmail.equals("") ? loggedInUser.getEmail() : newEmail;

    User userToBeUpdated = Util.shallowCloneUser(loggedInUser);
    userToBeUpdated.setUsername(newUsername);
    userToBeUpdated.setPassword(newEmail);
    userToBeUpdated.setPassword(newEmail);

    try {
      User updatedUser = controller.update(userToBeUpdated);
      if (updatedUser != null) {
        loggedInUser = updatedUser;
      }
    } catch (SQLException e) {
      System.out.println("server error");
    } finally {
      System.out.println("Update failed");
    }
  }

}
